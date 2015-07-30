package com.geospatialcorporation.android.geomobile.library.map.layerManager.implementations;

import com.geospatialcorporation.android.geomobile.library.map.layerManager.OptionFeature;
import com.geospatialcorporation.android.geomobile.library.map.layerManager.OptionsManagerBase;
import com.geospatialcorporation.android.geomobile.models.Layers.FeatureInfo;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Created by andre on 6/30/2015.
 */
public class PolylineOptionsManager extends OptionsManagerBase<PolylineOptions, Polyline> {

    @Override
    public void showLayers(GoogleMap map) {
        List<HashMap<UUID, OptionFeature<PolylineOptions>>> CachedOptions = getOption();

        for (HashMap<UUID, OptionFeature<PolylineOptions>> cachedOptions : CachedOptions) {
            for (Map.Entry<UUID, OptionFeature<PolylineOptions>> entry : cachedOptions.entrySet()) {

                PolylineOptions option = entry.getValue().getOption();
                FeatureInfo featureInfo = entry.getValue().getFeatureInfo();
                UUID key = entry.getKey();

                if (!mVisibleLayers.containsKey(key)) {
                    Polyline polyline = map.addPolyline(option);
                    mIdFeatureIdMap.put(polyline.getId(), featureInfo);
                    mVisibleLayers.put(key, polyline);

                }
            }
        }
    }

    @Override
    protected void removeLayer(UUID key) {
        mVisibleLayers.get(key).remove();
    }
}
