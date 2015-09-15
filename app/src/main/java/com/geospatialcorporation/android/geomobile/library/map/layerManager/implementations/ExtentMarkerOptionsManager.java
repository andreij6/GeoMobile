package com.geospatialcorporation.android.geomobile.library.map.layerManager.implementations;

import android.os.AsyncTask;
import android.util.Log;

import com.geospatialcorporation.android.geomobile.library.map.layerManager.OptionFeature;
import com.geospatialcorporation.android.geomobile.library.map.layerManager.OptionsManagerBase;
import com.geospatialcorporation.android.geomobile.models.Layers.FeatureInfo;
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

    @Override
    protected void removeMapObject(UUID key) {
        Marker marker = mVisibleLayers.get(key);

        if(marker != null) {
            marker.remove();

            mVisibleLayers.remove(key);
        }
    }

    @Override
    public void showLayers(GoogleMap map) {
        mMap = map;
        LatLngBounds bounds = mMap.getProjection().getVisibleRegion().latLngBounds;

        new ShowLayersAsync(bounds).execute();
    }

    protected class ShowLayersAsync extends AsyncTask<Void, Void, MarkerPostParamerters>{

        LatLngBounds mBounds;

        public ShowLayersAsync(LatLngBounds bounds){
            mBounds =  bounds;
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
        protected void onPostExecute(MarkerPostParamerters markerPostParamerters) {
            markerPostParamerters.mapMarkers();
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

            for(UUID key : mMarkersToRemove){
                removeMapObject(key);
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
