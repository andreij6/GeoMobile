package com.geospatialcorporation.android.geomobile.library.map.layerManager;

import com.geospatialcorporation.android.geomobile.models.Layers.FeatureInfo;
import com.google.android.gms.maps.GoogleMap;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public interface IOptionsManager<T, S> {

    void showLayers(GoogleMap map);

    void addOption(int layerId, T mapOption, FeatureInfo featureInfo);

    List<HashMap<UUID, OptionFeature<T>>> getOption();

    Collection<OptionFeature<T>> getOptionsById(int id);

    void remove(int layerId);

    FeatureInfo getFeatureIdLayerId(String id);

    Iterable<S> getShowingLayers();

    void clearVisibleLayers();

    Iterable<Integer> getShowingLayerIds();

    boolean isLayerCached(int layerId);

    void showAllLayers(GoogleMap map, UUID uniqeId);

}
