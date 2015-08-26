package com.geospatialcorporation.android.geomobile.library.map.layerManager.implementations;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.geospatialcorporation.android.geomobile.application;
import com.geospatialcorporation.android.geomobile.library.DI.UIHelpers.Interfaces.IMapStatusBarManager;
import com.geospatialcorporation.android.geomobile.library.map.Models.GeoClusterMarker;
import com.geospatialcorporation.android.geomobile.library.map.Models.GeoNonHierarchicalDistanceBasedAlgorithm;
import com.geospatialcorporation.android.geomobile.library.map.Models.IconRenderer;
import com.geospatialcorporation.android.geomobile.library.map.layerManager.OptionFeature;
import com.geospatialcorporation.android.geomobile.library.map.layerManager.OptionsManagerBase;
import com.geospatialcorporation.android.geomobile.models.Layers.FeatureInfo;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.ClusterManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class ClusterExtentMarkerOptionsManager extends OptionsManagerBase<MarkerOptions, GeoClusterMarker> {
    private static final String TAG = ClusterExtentMarkerOptionsManager.class.getSimpleName();

    ClusterManager<GeoClusterMarker> mClusterManager;
    IconRenderer mIconRenderer;

    public ClusterExtentMarkerOptionsManager(ClusterManager<GeoClusterMarker> clusterManager){
        mClusterManager = clusterManager;
    }

    @Override
    protected void removeMapObject(UUID key) {
        GeoClusterMarker marker =  mVisibleLayers.get(key);

        if(mClusterManager == null){
            mClusterManager = application.getClusterManager();
        }

        mClusterManager.setAlgorithm(new GeoNonHierarchicalDistanceBasedAlgorithm<GeoClusterMarker>());
        mClusterManager.removeItem(marker);
    }

    @Override
    public void showLayers(GoogleMap map) {
        mClusterManager = application.getClusterManager();

        clearVisibleLayers();
        mIdFeatureIdMap.clear();

        mIconRenderer = new IconRenderer(application.getAppContext(), map, mClusterManager);
        LatLngBounds bounds = map.getProjection().getVisibleRegion().latLngBounds;

        mClusterManager.setRenderer(mIconRenderer);

        mClusterManager.clearItems();

        new ShowLayersAsync(bounds).execute();
    }

    public List<Map.Entry<UUID, OptionFeature<MarkerOptions>>> getOptionsWithinExtent(HashMap<UUID, OptionFeature<MarkerOptions>> cachedOptions, LatLngBounds bounds){
        List<Map.Entry<UUID, OptionFeature<MarkerOptions>>> result = new ArrayList<>();

        for (Map.Entry<UUID, OptionFeature<MarkerOptions>> entry : cachedOptions.entrySet()) {
            MarkerOptions option = entry.getValue().getOption();

            if (bounds.contains(option.getPosition())) {
                result.add(entry);
            }
        }

        return result;
    }

    protected class ShowLayersAsync extends AsyncTask<Void, Void, List<GeoClusterMarker>>{

        LatLngBounds mBounds;
        Boolean mShowZoomMessage;
        HashMap<UUID, GeoClusterMarker> tempVisibleLayers;      //Here to fix ConcurrentModificationException


        public ShowLayersAsync(LatLngBounds bounds){
            mBounds = bounds;
            mShowZoomMessage = false;
            tempVisibleLayers = new HashMap<>();
        }



        @Override
        protected List<GeoClusterMarker> doInBackground(Void... params) {

            List<GeoClusterMarker> result = new ArrayList<>();

            List<HashMap<UUID, OptionFeature<MarkerOptions>>> CachedOptions = getOption();

            for (HashMap<UUID, OptionFeature<MarkerOptions>> cachedOptions : CachedOptions) {

                List<Map.Entry<UUID, OptionFeature<MarkerOptions>>> entriesWithinExtent = getOptionsWithinExtent(cachedOptions, mBounds);

                int max = 75;

                if(entriesWithinExtent.size() > max){
                    mShowZoomMessage = true;

                }

                int count = 0;

                for (Map.Entry<UUID, OptionFeature<MarkerOptions>> entry : entriesWithinExtent) {

                    UUID key = entry.getKey();

                    if(!mVisibleLayers.containsKey(key)) {
                        MarkerOptions option = entry.getValue().getOption();

                        GeoClusterMarker clusterMarker = new GeoClusterMarker(option);

                        result.add(clusterMarker);

                        FeatureInfo featureInfo = entry.getValue().getFeatureInfo();

                        mIdFeatureIdMap.put(clusterMarker.getId(), featureInfo);
                        tempVisibleLayers.put(key, clusterMarker);

                        count++;

                        if(count > max){
                            return  result;
                        }
                    }


                }

            }

            return result;
        }

        @Override
        protected void onPostExecute(List<GeoClusterMarker> geoClusterMarkers) {
            Log.d(TAG, "Markers: " + geoClusterMarkers.size());
            mClusterManager.addItems(geoClusterMarkers);

            mClusterManager.cluster();

            if(mShowZoomMessage){
                Toast.makeText(application.getAppContext(), "Zoom in to see more features", Toast.LENGTH_SHORT).show();
            }

            mVisibleLayers.putAll(tempVisibleLayers);
        }
    }
}
