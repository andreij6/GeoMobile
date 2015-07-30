package com.geospatialcorporation.android.geomobile.library.map.layerManager.implementations;

import com.geospatialcorporation.android.geomobile.library.map.layerManager.OptionFeature;
import com.geospatialcorporation.android.geomobile.library.map.layerManager.OptionsManagerBase;
import com.geospatialcorporation.android.geomobile.models.Layers.FeatureInfo;
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
        List<HashMap<UUID, OptionFeature<MarkerOptions>>> CachedOptions = getOption();

        for (HashMap<UUID, OptionFeature<MarkerOptions>> cachedOptions : CachedOptions) {
            for(Map.Entry<UUID, OptionFeature<MarkerOptions>> entry : cachedOptions.entrySet()){

                MarkerOptions option = entry.getValue().getOption();
                FeatureInfo featureInfo = entry.getValue().getFeatureInfo();

                UUID key = entry.getKey();

                if(!mVisibleLayers.containsKey(key)) {
                    Marker marker = map.addMarker(option);

                    mIdFeatureIdMap.put(marker.getId(), featureInfo);
                    marker.setTitle("");
                    mVisibleLayers.put(key, marker);
                }


            }
        }
    }
    @Override
    public void removeMapObject(UUID key){
        mVisibleLayers.get(key).remove();
    }


}
