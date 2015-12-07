package com.geospatialcorporation.android.geomobile.library.map.layerManager.implementations;

import android.os.AsyncTask;
import android.util.Log;

import com.geospatialcorporation.android.geomobile.application;
import com.geospatialcorporation.android.geomobile.library.DI.UIHelpers.Implementations.StatusBarManager.MapStatusBarManager;
import com.geospatialcorporation.android.geomobile.library.DI.UIHelpers.Implementations.StatusBarManager.StatusBarManagerBase;
import com.geospatialcorporation.android.geomobile.library.DI.UIHelpers.Interfaces.IMapStatusBarManager;
import com.geospatialcorporation.android.geomobile.library.map.layerManager.OptionFeature;
import com.geospatialcorporation.android.geomobile.library.map.layerManager.OptionsManagerBase;
import com.geospatialcorporation.android.geomobile.models.Layers.FeatureInfo;
import com.geospatialcorporation.android.geomobile.models.Layers.LegendLayer;
import com.geospatialcorporation.android.geomobile.ui.fragments.GoogleMapFragment;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
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

    @Override
    public void getNextFeature(String featureId, int layerId, LatLng center, boolean isNext){
        new GetNextFeatureAsync(center, isNext).execute();
    }

    @Override
    public void showLayer(GoogleMap map, LegendLayer legendLayer) {
        mMap = map;

        HashMap<UUID, OptionFeature<MarkerOptions>> removedOptions = mRemovedLayerOptions.get(legendLayer.getLayer().getId());
        HashMap<UUID, OptionFeature<MarkerOptions>> layerOptions = mLayerOptions.get(legendLayer.getLayer().getId());

        if(removedOptions != null){
            new ShowLayerTask(removedOptions).execute();
        } else {

            if (layerOptions != null) {
                new ShowLayerTask(layerOptions).execute();
            }
        }
    }

    @Override
    public void getNextFeature(String featureId, int layerId, boolean isNext) {
        HashMap<UUID, OptionFeature<MarkerOptions>> optionFeatures = mLayerOptions.get(layerId);

        Collection<OptionFeature<MarkerOptions>> values = optionFeatures.values();

        List<OptionFeature<MarkerOptions>> valuesList = new ArrayList<>(values);

        Collections.sort(valuesList);

        for (int i = 0; i < valuesList.size(); i++) {

            if(valuesList.get(i).getFeatureInfo().getFeatureId().equals(featureId)){

                int next = 0;

                if(isNext){
                    next = i + 1;
                } else {
                    next = i - 1;
                }

                if(next < 0){
                    next = valuesList.size() - 1;
                }

                if(next >= valuesList.size()){
                    next = 0;
                }

                LatLng position = valuesList.get(next).getOption().getPosition();

                GoogleMapFragment mapFragment = (GoogleMapFragment)application.getMainActivity().getContentFragment();
                mapFragment.simulateClick(position);
            }
        }
    }

    //region ShowLayersTask
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

            if(contentFragmentIsGoogleMapFragment()) {
                options.mapMarkers();

                if (mUUID != null) {
                    mapStatusBarManager.finished(StatusBarManagerBase.MARKERS, mUUID);
                }
            }

            mStatusBarManager.FinishLoading(MapStatusBarManager.MARKERS);
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
    //endregion

    protected class ShowLayerTask extends AsyncTask<Void, Void, MarkerPostParamerters>{

        HashMap<UUID, OptionFeature<MarkerOptions>> mOptionsMap;

        public ShowLayerTask(HashMap<UUID, OptionFeature<MarkerOptions>> options) {
            mOptionsMap = options;
        }

        @Override
        protected MarkerPostParamerters doInBackground(Void... params) {
            MarkerPostParamerters result = new MarkerPostParamerters();

            for (Map.Entry<UUID, OptionFeature<MarkerOptions>> entry : mOptionsMap.entrySet()) {

                UUID key = entry.getKey();

                MarkerOptions option = entry.getValue().getOption();

                FeatureInfo featureInfo = entry.getValue().getFeatureInfo();

                result.addMarker(option, featureInfo, key);
            }

            return result;
        }

        @Override
        protected void onPostExecute(MarkerPostParamerters options) {
            if(contentFragmentIsGoogleMapFragment()) {
                options.mapMarkers();
            }

            mStatusBarManager.FinishLoading(MapStatusBarManager.MARKERS);
        }

    }

    //region Attempt
    protected class GetNextFeatureAsync extends AsyncTask<Void, Void, LatLng>{

        LatLng mHighlightedCenter;
        Boolean mIsNext;

        public GetNextFeatureAsync(LatLng center, Boolean isNext){
            mHighlightedCenter = center;
            mIsNext = isNext;
        }

        @Override
        protected LatLng doInBackground(Void... params) {
            int Radius = 6371;
            int closest = -1;
            int furthest = -1;
            MarkerOptions closestMarker = null;
            MarkerOptions furthestMarker = null;

            HashMap<Integer, Double> distances = new HashMap<>();

            List<HashMap<UUID, OptionFeature<MarkerOptions>>> CachedOptions = getOption();
            int counter = 0;
            for (HashMap<UUID, OptionFeature<MarkerOptions>> cachedOptions : CachedOptions) {

                if (cachedOptions != null) {
                    for (Map.Entry<UUID, OptionFeature<MarkerOptions>> entry : cachedOptions.entrySet()) {
                        MarkerOptions option = entry.getValue().getOption();

                        LatLng pos = option.getPosition();

                        if(pos.longitude == mHighlightedCenter.longitude && pos.latitude == mHighlightedCenter.latitude){
                            continue;
                        }

                        boolean condition = true;

                        if(mIsNext){
                            condition = pos.longitude > mHighlightedCenter.longitude;
                        } else {
                            condition = pos.longitude < mHighlightedCenter.longitude;
                        }

                        if(condition) {
                            double dLat = rad(pos.latitude - mHighlightedCenter.latitude);
                            double dLong = rad(pos.longitude - mHighlightedCenter.longitude);

                            double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) + Math.cos(rad(mHighlightedCenter.latitude)) * Math.cos(rad(mHighlightedCenter.latitude))
                                    * Math.sin(dLong / 2) * Math.sin(dLong / 2);

                            double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
                            double d = Radius * c;

                            distances.put(counter, d);

                            if (closest == -1 || d < distances.get(closest)) {
                                closest = counter;
                                closestMarker = option;
                            }
                        } else {
                            double dLat = rad(pos.latitude - mHighlightedCenter.latitude);
                            double dLong = rad(pos.longitude - mHighlightedCenter.longitude);

                            double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) + Math.cos(rad(mHighlightedCenter.latitude)) * Math.cos(rad(mHighlightedCenter.latitude))
                                    * Math.sin(dLong / 2) * Math.sin(dLong / 2);

                            double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
                            double d = Radius * c;

                            distances.put(counter, d);

                            if (furthest == -1 || d > distances.get(furthest)) {
                                furthest = counter;
                                furthestMarker = option;
                            }
                        }



                        counter++;
                    }
                }
            }

            if(closestMarker != null){
                return closestMarker.getPosition();
            } else {
                if(furthestMarker != null){
                    return furthestMarker.getPosition();
                }

                return null;
            }
        }

        public double rad(double x){
            return x * Math.PI / 180;
        }

        @Override
        protected void onPostExecute(LatLng latLng) {
            super.onPostExecute(latLng);

            GoogleMapFragment mapFragment = (GoogleMapFragment)application.getMainActivity().getContentFragment();

            mapFragment.simulateClick(latLng);

        }
    }
    //endregion
}
