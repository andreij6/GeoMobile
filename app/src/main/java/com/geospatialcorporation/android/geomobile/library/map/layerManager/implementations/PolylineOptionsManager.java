package com.geospatialcorporation.android.geomobile.library.map.layerManager.implementations;

import android.os.AsyncTask;
import android.util.Log;

import com.geospatialcorporation.android.geomobile.application;
import com.geospatialcorporation.android.geomobile.library.DI.UIHelpers.Implementations.MapStatusBarManager;
import com.geospatialcorporation.android.geomobile.library.constants.GeometryTypeCodes;
import com.geospatialcorporation.android.geomobile.library.map.layerManager.OptionFeature;
import com.geospatialcorporation.android.geomobile.library.map.layerManager.OptionsManagerBase;
import com.geospatialcorporation.android.geomobile.models.Layers.FeatureInfo;
import com.geospatialcorporation.android.geomobile.models.Query.map.response.mapquery.Feature;
import com.geospatialcorporation.android.geomobile.models.Query.map.response.mapquery.Geometry;
import com.geospatialcorporation.android.geomobile.ui.fragments.GoogleMapFragment;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class PolylineOptionsManager extends OptionsManagerBase<PolylineOptions, Polyline> {

    private static final String TAG = Polyline.class.getSimpleName();
    GoogleMap mMap;

    public void showLayers(GoogleMap map) {
        LatLngBounds bounds = map.getProjection().getVisibleRegion().latLngBounds;
        mMap = map;
        new ShowLayersAsync(map, bounds).execute();
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


        public ShowLayersAsync(GoogleMap map, LatLngBounds bounds){
            mGoogleMap = map;
            mBounds = bounds;
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

                        for (LatLng point : option.getPoints()) {
                            if (mBounds.contains(point)) {
                                if (!mVisibleLayers.containsKey(key)) {

                                    FeatureInfo featureInfo = entry.getValue().getFeatureInfo();

                                    mResult.addPolyline(option, featureInfo, key);
                                }

                                pointInBounds = true;
                                break;
                            }
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
            if(contentFragmentIsGoogleMapFragment() || application.getIsTablet()) {
                options.mapFeatures();
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
