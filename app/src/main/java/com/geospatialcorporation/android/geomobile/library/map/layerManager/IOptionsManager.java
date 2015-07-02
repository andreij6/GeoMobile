package com.geospatialcorporation.android.geomobile.library.map.layerManager;

import com.google.android.gms.maps.GoogleMap;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public interface IOptionsManager<T> {
    void showLayers(GoogleMap map);

    void addOption(int layerId, T mapOption);

    List<HashMap<UUID, T>> getOption();

    Collection<T> getOptionsById(int id);

    void remove(int layerId);

    String[] getFeatureId(String id);

    int getLayerId(int id);
}
