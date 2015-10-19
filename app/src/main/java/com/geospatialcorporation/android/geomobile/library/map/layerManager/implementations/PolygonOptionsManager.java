package com.geospatialcorporation.android.geomobile.library.map.layerManager.implementations;

import android.os.AsyncTask;
import android.util.Log;

import com.geospatialcorporation.android.geomobile.application;
import com.geospatialcorporation.android.geomobile.library.DI.UIHelpers.Implementations.StatusBarManager.StatusBarManagerBase;
import com.geospatialcorporation.android.geomobile.library.DI.UIHelpers.Interfaces.IMapStatusBarManager;
import com.geospatialcorporation.android.geomobile.library.constants.GeometryTypeCodes;
import com.geospatialcorporation.android.geomobile.library.map.layerManager.OptionFeature;
import com.geospatialcorporation.android.geomobile.library.map.layerManager.OptionsManagerBase;
import com.geospatialcorporation.android.geomobile.library.util.GeoPolyUtil;
import com.geospatialcorporation.android.geomobile.models.Layers.FeatureInfo;
import com.geospatialcorporation.android.geomobile.ui.fragments.GoogleMapFragment;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class PolygonOptionsManager extends OptionsManagerBase<PolygonOptions, Polygon> {
    private static final String TAG = PolygonOptionsManager.class.getSimpleName();

    GoogleMap mMap;
    IMapStatusBarManager mMapStatusBarManager;

    public PolygonOptionsManager(){
        mMapStatusBarManager = application.getStatusBarManager();
    }

    public void showLayers(GoogleMap map) {
        mMap = map;
        LatLngBounds bounds = map.getProjection().getVisibleRegion().latLngBounds;

        new ShowLayersAsync(map, bounds, null).execute();
    }

    @Override
    public void showAllLayers(GoogleMap map, UUID uniqueId) {
        mMap = map;
        LatLngBounds bounds = map.getProjection().getVisibleRegion().latLngBounds;

        new ShowLayersAsync(map, bounds, uniqueId).execute();
    }

    @Override
    protected void removeMapObject(UUID key) {
        try {
            if (mVisibleLayers.get(key) != null) {
                mVisibleLayers.get(key).remove();
            }
        } catch (Exception e){
            Log.e(TAG, e.getMessage());
        }
    }

    protected class ShowLayersAsync extends AsyncTask<Void, Void, PostParameters>{

        GoogleMap mGoogleMap;
        LatLngBounds mBounds;
        UUID mUUID;
        Boolean mStatusBarVisible;

        public ShowLayersAsync(GoogleMap map, LatLngBounds bounds, UUID uniqueId){
            mGoogleMap = map;
            mBounds = bounds;
            mUUID = uniqueId;
            mStatusBarVisible = false;
        }

        @Override
        protected PostParameters doInBackground(Void... params) {
            PostParameters result = new PostParameters();

            List<HashMap<UUID, OptionFeature<PolygonOptions>>> CachedOptions = getOption();

            for (HashMap<UUID, OptionFeature<PolygonOptions>> cachedOptions : CachedOptions) {
                if(cachedOptions != null) {
                    for (Map.Entry<UUID, OptionFeature<PolygonOptions>> entry : cachedOptions.entrySet()) {


                        UUID key = entry.getKey();
                        PolygonOptions option = entry.getValue().getOption();
                        Boolean pointInBounds = false;

                        if (GeoPolyUtil.BoundsIntersect(option.getPoints(), mBounds)) {
                            if (!mVisibleLayers.containsKey(key)) {

                                FeatureInfo featureInfo = entry.getValue().getFeatureInfo();

                                result.addPolygon(option, featureInfo, key);

                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (!mStatusBarVisible && mUUID != null) {
                                            mMapStatusBarManager.ensureStatusBarVisible();
                                            mStatusBarVisible = true;
                                        }
                                    }
                                });
                            }

                            pointInBounds = true;
                        }

                        if(!pointInBounds){
                            result.remove(key);
                        }
                    }
                }
            }

            return result;
        }

        @Override
        protected void onPostExecute(PostParameters options) {
            IMapStatusBarManager mapStatusBarManager = application.getStatusBarManager();

            if(contentFragmentIsGoogleMapFragment() || application.getIsTablet()) {
                options.mapFeatures();

                if(mUUID != null) {

                    mapStatusBarManager.finished(StatusBarManagerBase.POLYGONS, mUUID);
                }
            }
        }

    }

    protected boolean contentFragmentIsGoogleMapFragment() {
        if(application.getMainActivity() == null){
            return false;
        }else {
            return application.getMainActivity().getContentFragment() instanceof GoogleMapFragment;
        }
    }

    protected class PostParameters {

        List<PolygonFeature> mPolygonFeatures;
        List<UUID> mPolygonsToRemove;

        public PostParameters(){
            mPolygonFeatures = new ArrayList<>();
            mPolygonsToRemove = new ArrayList<>();
        }

        public void mapFeatures(){
            for (PolygonFeature pparams: mPolygonFeatures) {
                Polygon polygon = mMap.addPolygon(pparams.getOptions());
                mIdFeatureIdMap.put(polygon.getId(), pparams.getFeatureInfo());
                mVisibleLayers.put(pparams.getKey(), polygon);
            }

            mStatusBarManager.FinishLoading(GeometryTypeCodes.Polygon);

            for(UUID key : mPolygonsToRemove){
                removeMapObject(key);
                mVisibleLayers.remove(key);
            }
        }

        public void addPolygon(PolygonOptions options, FeatureInfo info, UUID key){
            mPolygonFeatures.add(new PolygonFeature(options, info, key));
        }

        public void remove(UUID key){
            mPolygonsToRemove.add(key);
        }

        public boolean hasFeatures() {
            return mPolygonFeatures.size() > 0;
        }
    }

    protected class PolygonFeature {
        PolygonOptions mOptions;
        FeatureInfo mFeatureInfo;
        UUID mKey;

        public PolygonFeature(PolygonOptions option, FeatureInfo featureInfo, UUID key) {
            mOptions = option;
            mFeatureInfo = featureInfo;
            mKey = key;
        }

        public UUID getKey() {
            return mKey;
        }

        public PolygonOptions getOptions() {
            return mOptions;
        }

        public FeatureInfo getFeatureInfo() {
            return mFeatureInfo;
        }
    }


}
