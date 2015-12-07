package com.geospatialcorporation.android.geomobile.library.map.layerManager.implementations;

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
