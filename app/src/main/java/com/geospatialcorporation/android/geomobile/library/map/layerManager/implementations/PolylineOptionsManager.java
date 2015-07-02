package com.geospatialcorporation.android.geomobile.library.map.layerManager.implementations;

import com.geospatialcorporation.android.geomobile.library.map.layerManager.OptionsManagerBase;
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
        List<HashMap<UUID, PolylineOptions>> CachedOptions = getOption();

        for (HashMap<UUID, PolylineOptions> cachedOptions : CachedOptions) {
            for (Map.Entry<UUID, PolylineOptions> entry : cachedOptions.entrySet()) {

                PolylineOptions option = entry.getValue();
                UUID key = entry.getKey();

                if (!mVisibleLayers.containsKey(key)) {
                    mVisibleLayers.put(key, map.addPolyline(option));
                }
            }
        }
    }

    @Override
    public int getLayerId(int id) {
        return 0;
    }

    @Override
    protected void removeLayer(UUID key) {
        mVisibleLayers.get(key).remove();
    }
}
