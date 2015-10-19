package com.geospatialcorporation.android.geomobile.library.map.layerManager.implementations;

import com.geospatialcorporation.android.geomobile.library.map.layerManager.OptionFeature;
import com.geospatialcorporation.android.geomobile.library.map.layerManager.OptionsManagerBase;
import com.geospatialcorporation.android.geomobile.models.Layers.FeatureInfo;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

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
    protected void removeMapObject(UUID key) {
        mVisibleLayers.get(key).remove();
    }
}
