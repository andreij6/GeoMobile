package com.geospatialcorporation.android.geomobile.library.map.layerManager.implementations;

import android.os.AsyncTask;
import android.util.Log;

import com.geospatialcorporation.android.geomobile.application;
import com.geospatialcorporation.android.geomobile.library.DI.UIHelpers.Implementations.StatusBarManager.MapStatusBarManager;
import com.geospatialcorporation.android.geomobile.library.DI.UIHelpers.Implementations.StatusBarManager.StatusBarManagerBase;
import com.geospatialcorporation.android.geomobile.library.DI.UIHelpers.Interfaces.IMapStatusBarManager;
import com.geospatialcorporation.android.geomobile.library.constants.GeometryTypeCodes;
import com.geospatialcorporation.android.geomobile.library.map.layerManager.OptionFeature;
import com.geospatialcorporation.android.geomobile.library.map.layerManager.OptionsManagerBase;
import com.geospatialcorporation.android.geomobile.models.Layers.FeatureInfo;
import com.geospatialcorporation.android.geomobile.models.Query.map.response.mapquery.Geometry;
import com.geospatialcorporation.android.geomobile.ui.fragments.GoogleMapFragment;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class ExtentMarkerOptionsManager extends OptionsManagerBase<MarkerOptions, Marker> {

    private static final String TAG = ExtentMarkerOptionsManager.class.getSimpleName();
    GoogleMap mMap;
    IMapStatusBarManager mMapStatusBarManager;

    public ExtentMarkerOptionsManager(){
        mMapStatusBarManager = application.getStatusBarManager();
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

    @Override
    public void showLayers(GoogleMap map) {
        mMap = map;
        LatLngBounds bounds = mMap.getProjection().getVisibleRegion().latLngBounds;

        new ShowLayersAsync(bounds, null).execute();
    }

    @Override
    public void showAllLayers(GoogleMap map, UUID uniqueId) {
        mMap = map;
        LatLngBounds bounds = mMap.getProjection().getVisibleRegion().latLngBounds;

        new ShowLayersAsync(bounds, uniqueId).execute();
    }

    protected class ShowLayersAsync extends AsyncTask<Void, Void, MarkerPostParamerters>{

        LatLngBounds mBounds;
        UUID mUUID;
        Boolean mStatusBarVisible;

        public ShowLayersAsync(LatLngBounds bounds, UUID uniqueId){
            mBounds =  bounds;
            mUUID = uniqueId;
            mStatusBarVisible = false;
        }

        @Override
        protected MarkerPostParamerters doInBackground(Void... params) {
            MarkerPostParamerters result = new MarkerPostParamerters();

            List<HashMap<UUID, OptionFeature<MarkerOptions>>> CachedOptions = getOption();
            for (HashMap<UUID, OptionFeature<MarkerOptions>> cachedOptions : CachedOptions) {

                if(cachedOptions != null) {
                    for (Map.Entry<UUID, OptionFeature<MarkerOptions>> entry : cachedOptions.entrySet()) {

                        UUID key = entry.getKey();

                        MarkerOptions option = entry.getValue().getOption();

                        if (mBounds.contains(option.getPosition())) {

                            if(!mVisibleLayers.containsKey(key)) {
                                FeatureInfo featureInfo = entry.getValue().getFeatureInfo();

                                result.addMarker(option, featureInfo, key);

                                //getActivity().runOnUiThread(new Runnable() {
                                //    @Override
                                //    public void run() {
                                //        if(!mStatusBarVisible && mUUID != null){
                                //            mMapStatusBarManager.ensureStatusBarVisible();
                                //            mStatusBarVisible = true;
                                //        }
                                //    }
                                //});
                            }

                        } else {
                            if(mVisibleLayers.containsKey(key)) {
                                result.remove(key);
                            }
                        }
                    }
                }
            }

            return result;
        }

        @Override
        protected void onPostExecute(MarkerPostParamerters options) {
            IMapStatusBarManager mapStatusBarManager = application.getStatusBarManager();

            if(contentFragmentIsGoogleMapFragment() || application.getIsTablet()) {
                options.mapMarkers();

                if(mUUID != null) {
                    mapStatusBarManager.finished(StatusBarManagerBase.MARKERS, mUUID);
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

    protected class MarkerPostParamerters{

        List<MarkerFeature> mMarkerOptions;
        List<UUID> mMarkersToRemove;

        public MarkerPostParamerters(){
            mMarkerOptions = new ArrayList<>();
            mMarkersToRemove = new ArrayList<>();
        }

        public void mapMarkers() {
            for (MarkerFeature markerFeature : mMarkerOptions) {
                Marker marker = mMap.addMarker(markerFeature.getOption());

                mIdFeatureIdMap.put(marker.getId(), markerFeature.getFeatureInfo());
                mVisibleLayers.put(markerFeature.getKey(), marker);
            }

            mStatusBarManager.FinishLoading(MapStatusBarManager.MARKERS);

            for(UUID key : mMarkersToRemove){
                removeMapObject(key);
                mVisibleLayers.remove(key);
            }
        }

        public void addMarker(MarkerOptions option, FeatureInfo featureInfo, UUID key) {
            mMarkerOptions.add(new MarkerFeature(option, featureInfo, key));
        }

        public void remove(UUID key) {
            mMarkersToRemove.add(key);
        }
    }

    public class MarkerFeature {

        MarkerOptions mOptions;
        FeatureInfo mFeatureInfo;
        UUID mKey;

        public MarkerFeature(MarkerOptions options, FeatureInfo info, UUID key){
            mOptions = options;
            mFeatureInfo = info;
            mKey = key;
        }

        public MarkerOptions getOption() {
            return mOptions;
        }

        public FeatureInfo getFeatureInfo() {
            return mFeatureInfo;
        }

        public UUID getKey() {
            return mKey;
        }
    }
}
