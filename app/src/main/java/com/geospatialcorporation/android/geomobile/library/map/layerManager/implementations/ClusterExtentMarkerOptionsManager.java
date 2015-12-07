package com.geospatialcorporation.android.geomobile.library.map.layerManager.implementations;

/*
public class ClusterExtentMarkerOptionsManager extends OptionsManagerBase<MarkerOptions, GeoClusterMarker> {
    private static final String TAG = ClusterExtentMarkerOptionsManager.class.getSimpleName();

    ClusterManager<GeoClusterMarker> mClusterManager;
    IconRenderer mIconRenderer;
    GoogleMap mMap;

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

        mMap = map;

        clearVisibleLayers();
        mIdFeatureIdMap.clear();

        mIconRenderer = new IconRenderer(application.getAppContext(), mMap, mClusterManager);
        LatLngBounds bounds = mMap.getProjection().getVisibleRegion().latLngBounds;

        mClusterManager.setRenderer(mIconRenderer);

        mClusterManager.clearItems();

        new ShowLayersAsync(bounds).execute();
    }

    @Override
    public void showAllLayers(GoogleMap map, UUID uniqeId) {

    }

    @Override
    public LatLng getNextPosition(LatLng highightedCenter) {
        return null;
    }

    public List<Map.Entry<UUID, OptionFeature<MarkerOptions>>> getOptionsWithinExtent(HashMap<UUID, OptionFeature<MarkerOptions>> cachedOptions, LatLngBounds bounds){
        List<Map.Entry<UUID, OptionFeature<MarkerOptions>>> result = new ArrayList<>();

        if(cachedOptions != null) {
            for (Map.Entry<UUID, OptionFeature<MarkerOptions>> entry : cachedOptions.entrySet()) {
                MarkerOptions option = entry.getValue().getOption();

                if (bounds.contains(option.getPosition())) {
                    result.add(entry);
                }
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
            mClusterManager.addItems(geoClusterMarkers);

            mClusterManager.cluster();

            if(mShowZoomMessage){
                Toast.makeText(application.getAppContext(), "Zoom in to see more features", Toast.LENGTH_SHORT).show();
            }

            Log.d(TAG, "Cluster Extent Post Execute");

            mVisibleLayers.putAll(tempVisibleLayers);
        }
    }
}
*/