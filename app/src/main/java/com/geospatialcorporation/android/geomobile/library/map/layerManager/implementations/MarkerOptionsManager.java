package com.geospatialcorporation.android.geomobile.library.map.layerManager.implementations;

/*
public class MarkerOptionsManager extends OptionsManagerBase<MarkerOptions, Marker> {

    private static final String TAG = MarkerOptionsManager.class.getSimpleName();

    public void showLayers(GoogleMap map) {

        List<HashMap<UUID, OptionFeature<MarkerOptions>>> CachedOptions = getOption();
        for (HashMap<UUID, OptionFeature<MarkerOptions>> cachedOptions : CachedOptions) {

            if(cachedOptions != null) {
                for (Map.Entry<UUID, OptionFeature<MarkerOptions>> entry : cachedOptions.entrySet()) {

                    UUID key = entry.getKey();

                    if (!mVisibleLayers.containsKey(key)) {

                        MarkerOptions option = entry.getValue().getOption();
                        FeatureInfo featureInfo = entry.getValue().getFeatureInfo();

                        Marker marker = map.addMarker(option);

                        mIdFeatureIdMap.put(marker.getId(), featureInfo);
                        mVisibleLayers.put(key, marker);
                    }
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

    @Override
    protected void removeMapObject(UUID key) {
        mVisibleLayers.get(key).remove();
    }
}
*/
