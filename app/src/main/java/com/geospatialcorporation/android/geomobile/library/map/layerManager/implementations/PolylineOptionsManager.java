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
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.PolyUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class PolylineOptionsManager extends OptionsManagerBase<PolylineOptions, Polyline> {

    private static final String TAG = Polyline.class.getSimpleName();
    GoogleMap mMap;
    IMapStatusBarManager mMapStatusBarManager;

    public PolylineOptionsManager(){
        mMapStatusBarManager = application.getStatusBarManager();
    }

    public void showLayers(GoogleMap map) {
        LatLngBounds bounds = map.getProjection().getVisibleRegion().latLngBounds;
        mMap = map;
        new ShowLayersAsync(map, bounds, null).execute();
    }

    @Override
    public void showAllLayers(GoogleMap map, UUID uniqeId) {
        LatLngBounds bounds = map.getProjection().getVisibleRegion().latLngBounds;
        mMap = map;
        new ShowLayersAsync(map, bounds, uniqeId).execute();
    }

    @Override
    public void removeMapObject(UUID key){
        try {
            if (mVisibleLayers.get(key) != null) {
                mVisibleLayers.get(key).remove();
            }
        } catch (Exception e){
            Log.e(TAG, e.getMessage());
        }
    }

    protected class ShowLayersAsync extends AsyncTask<Void, Void, PostParameters> {

        GoogleMap mGoogleMap;
        LatLngBounds mBounds;
        PostParameters mResult;
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
             mResult = new PostParameters();

            List<HashMap<UUID, OptionFeature<PolylineOptions>>> CachedOptions = getOption();

            for (HashMap<UUID, OptionFeature<PolylineOptions>> cachedOptions : CachedOptions) {

                if(cachedOptions != null) {

                    for (Map.Entry<UUID, OptionFeature<PolylineOptions>> entry : cachedOptions.entrySet()) {

                        UUID key = entry.getKey();
                        PolylineOptions option = entry.getValue().getOption();
                        Boolean pointInBounds = false;



                        if (GeoPolyUtil.BoundsIntersect(option.getPoints(), mBounds)) {
                            if (!mVisibleLayers.containsKey(key)) {

                                FeatureInfo featureInfo = entry.getValue().getFeatureInfo();

                                mResult.addPolyline(option, featureInfo, key);

                                //getActivity().runOnUiThread(new Runnable() {
                                //    @Override
                                //    public void run() {
                                //        if (!mStatusBarVisible && mUUID != null) {
                                //            mMapStatusBarManager.ensureStatusBarVisible();
                                //            mStatusBarVisible = true;
                                //        }
                                //    }
                                //});
                            }

                            pointInBounds = true;
                        }

                        if (!pointInBounds) {
                            mResult.remove(key);
                        }
                    }
                }

            }

            return mResult;
        }

        @Override
        protected void onPostExecute(PostParameters options) {
            IMapStatusBarManager mapStatusBarManager = application.getStatusBarManager();

            if(contentFragmentIsGoogleMapFragment() || application.getIsTablet()) {
                options.mapFeatures();

                if(mUUID != null) {
                    mapStatusBarManager.finished(StatusBarManagerBase.LINES, mUUID);
                }
            }


        }
    }

    protected boolean contentFragmentIsGoogleMapFragment() {
        if(application.getIsTablet()){
            return false;
        }else {
            return application.getMainActivity().getContentFragment() instanceof GoogleMapFragment;
        }
    }

    protected class PostParameters {

        List<PolylineFeatures> mFeatures;
        List<UUID> mLinesToRemove;

        public PostParameters(){
            mFeatures = new ArrayList<>();
            mLinesToRemove = new ArrayList<>();
        }

        public void mapFeatures() {
            for (PolylineFeatures pparams: mFeatures) {
                Polyline polyline = mMap.addPolyline(pparams.getOptions());

                mIdFeatureIdMap.put(polyline.getId(), pparams.getFeatureInfo());
                mVisibleLayers.put(pparams.getKey(), polyline);
            }

            mStatusBarManager.FinishLoading(GeometryTypeCodes.Line);

            for(UUID key : mLinesToRemove){
                removeMapObject(key);
                mVisibleLayers.remove(key);
            }



        }

        public void addPolyline(PolylineOptions option, FeatureInfo featureInfo, UUID key) {
            mFeatures.add(new PolylineFeatures(option, featureInfo, key));
        }

        public void remove(UUID key) {
            mLinesToRemove.add(key);
        }

        public boolean hasFeatures() {
            return mFeatures.size() > 0;
        }
    }

    protected class PolylineFeatures {
        PolylineOptions mOptions;
        FeatureInfo mFeatureInfo;
        UUID mKey;

        public PolylineFeatures(PolylineOptions option, FeatureInfo featureInfo, UUID key){
            mOptions = option;
            mFeatureInfo = featureInfo;
            mKey = key;
        }

        public UUID getKey() {
            return mKey;
        }

        public PolylineOptions getOptions() {
            return mOptions;
        }

        public FeatureInfo getFeatureInfo() {
            return mFeatureInfo;
        }
    }
}
