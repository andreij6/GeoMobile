package com.geospatialcorporation.android.geomobile.library.map.layerManager.implementations;

import com.geospatialcorporation.android.geomobile.library.map.layerManager.OptionFeature;
import com.geospatialcorporation.android.geomobile.library.map.layerManager.OptionsManagerBase;
import com.geospatialcorporation.android.geomobile.models.Layers.FeatureInfo;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;


/**
 * Created by andre on 6/30/2015.
 */
public class PolygonOptionsManager extends OptionsManagerBase<PolygonOptions, Polygon> {

    @Override
    public void showLayers(GoogleMap map) {

        List<HashMap<UUID, OptionFeature<PolygonOptions>>> CachedOptions = getOption();

        for (HashMap<UUID, OptionFeature<PolygonOptions>> cachedOptions : CachedOptions) {
            for (Map.Entry<UUID, OptionFeature<PolygonOptions>> entry : cachedOptions.entrySet()) {

                PolygonOptions option = entry.getValue().getOption();
                FeatureInfo featureInfo = entry.getValue().getFeatureInfo();
                UUID key = entry.getKey();

                if (!mVisibleLayers.containsKey(key)) {

                    Polygon polygon = map.addPolygon(option);

                    mIdFeatureIdMap.put(polygon.getId(), featureInfo);
                    mVisibleLayers.put(key, polygon);
                }

            }
        }
    }

    @Override
    protected void removeMapObject(UUID key) {
        mVisibleLayers.get(key).remove();
    }
}
