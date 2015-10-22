package com.geospatialcorporation.android.geomobile.library.map.layerManager.implementations;

import com.geospatialcorporation.android.geomobile.application;
import com.geospatialcorporation.android.geomobile.library.map.Models.GeoClusterMarker;
import com.geospatialcorporation.android.geomobile.library.map.Models.GeoNonHierarchicalDistanceBasedAlgorithm;
import com.geospatialcorporation.android.geomobile.library.map.Models.IconRenderer;
import com.geospatialcorporation.android.geomobile.library.map.layerManager.OptionFeature;
import com.geospatialcorporation.android.geomobile.library.map.layerManager.OptionsManagerBase;
import com.geospatialcorporation.android.geomobile.models.Layers.FeatureInfo;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.ClusterManager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/*
public class ClusterMarkerOptionsManager extends OptionsManagerBase<MarkerOptions, GeoClusterMarker> {

    ClusterManager<GeoClusterMarker> mClusterManager;

    public ClusterMarkerOptionsManager(ClusterManager<GeoClusterMarker> clusterManager){
        mClusterManager = clusterManager;
    }

    @Override
    public void removeMapObject(UUID key){
        GeoClusterMarker marker =  mVisibleLayers.get(key);

        if(mClusterManager == null){
            mClusterManager = application.getClusterManager();
        }

        mClusterManager.setAlgorithm(new GeoNonHierarchicalDistanceBasedAlgorithm<GeoClusterMarker>());
        mClusterManager.removeItem(marker);
    }


    @Override
    public void showLayers(GoogleMap map) {
        List<HashMap<UUID, OptionFeature<MarkerOptions>>> CachedOptions = getOption();
        IconRenderer iconRenderer = null;

        if(mClusterManager == null){
            mClusterManager = application.getClusterManager();
        }

        for (HashMap<UUID, OptionFeature<MarkerOptions>> cachedOptions : CachedOptions) {
            for(Map.Entry<UUID, OptionFeature<MarkerOptions>> entry : cachedOptions.entrySet()){

                MarkerOptions option = entry.getValue().getOption();
                FeatureInfo featureInfo = entry.getValue().getFeatureInfo();

                UUID key = entry.getKey();

                if(!mVisibleLayers.containsKey(key)) {
                    if(iconRenderer == null){
                        iconRenderer = new IconRenderer(application.getAppContext(), map, mClusterManager);
                    }

                    mClusterManager.setRenderer(iconRenderer);

                    GeoClusterMarker clusterMarker = new GeoClusterMarker(option);

                    mClusterManager.addItem(clusterMarker);

                    //Marker marker = map.addMarker(option);
                    mIdFeatureIdMap.put(clusterMarker.getId(), featureInfo);
                    mVisibleLayers.put(key, clusterMarker);
                }


            }
        }
    }

    @Override
    public void showAllLayers(GoogleMap map, UUID uniqeId) {

    }

    @Override
    public LatLng getNextPosition(LatLng highightedCenter) {
        return null;
    }
}
*/
