package com.geospatialcorporation.android.geomobile.library.map.layerManager.implementations;

import com.geospatialcorporation.android.geomobile.library.map.layerManager.OptionsManagerBase;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;


public class MarkerOptionsManager extends OptionsManagerBase<MarkerOptions, Marker> {



    @Override
    public void showLayers(GoogleMap map) {
        LatLngBounds bounds = map.getProjection().getVisibleRegion().latLngBounds;
        List<HashMap<UUID, MarkerOptions>> CachedOptions = getOption();

        for (HashMap<UUID, MarkerOptions> cachedOptions : CachedOptions) {
            for(Map.Entry<UUID, MarkerOptions> entry : cachedOptions.entrySet()){

                MarkerOptions option = entry.getValue();
                UUID key = entry.getKey();

                if (bounds.contains(new LatLng(option.getPosition().latitude, option.getPosition().longitude))) {
                    if(!mVisibleLayers.containsKey(key)) {
                        Marker marker = map.addMarker(option);

                        String featureId = getFeatureIdFromOption(option.getTitle());
                        String layerId = getLayerIdFromOption(option.getTitle());

                        String[] featureIdlayerId = new String[2];

                        featureIdlayerId[0] = featureId;
                        featureIdlayerId[1] = layerId;

                        mIdFeatureIdMap.put(marker.getId(), featureIdlayerId);
                        marker.setTitle("");
                        mVisibleLayers.put(key, marker);
                    }
                } else {
                    if(mVisibleLayers.containsKey(key)){

                        mVisibleLayers.get(key).remove();

                        mVisibleLayers.remove(key);
                    }
                }

            }
        }
    }

    private String getLayerIdFromOption(String title) {
        int start = title.indexOf("_") + 1;
        return title.substring(start);
    }

    private String getFeatureIdFromOption(String title) {
        int index = title.indexOf("_");
        return title.substring(0, index);
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
