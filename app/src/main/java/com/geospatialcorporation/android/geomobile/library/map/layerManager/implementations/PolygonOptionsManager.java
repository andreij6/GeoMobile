package com.geospatialcorporation.android.geomobile.library.map.layerManager.implementations;

import com.geospatialcorporation.android.geomobile.library.map.layerManager.IOptionsManager;
import com.geospatialcorporation.android.geomobile.library.map.layerManager.OptionsManagerBase;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static java.util.UUID.randomUUID;

/**
 * Created by andre on 6/30/2015.
 */
public class PolygonOptionsManager extends OptionsManagerBase<PolygonOptions, Polygon> {

    @Override
    public void showLayers(GoogleMap map) {

        List<HashMap<UUID, PolygonOptions>> CachedOptions = getOption();

        for (HashMap<UUID, PolygonOptions> cachedOptions : CachedOptions) {
            for (Map.Entry<UUID, PolygonOptions> entry : cachedOptions.entrySet()) {

                PolygonOptions option = entry.getValue();
                UUID key = entry.getKey();

                if (!mVisibleLayers.containsKey(key)) {
                    mVisibleLayers.put(key, map.addPolygon(option));
                }

            }
        }
    }

    @Override
    public int getLayerId(int id) {
        return 0;
    }

    @Override
    protected void removeLayer(UUID key) {mVisibleLayers.get(key).remove(); }

}
