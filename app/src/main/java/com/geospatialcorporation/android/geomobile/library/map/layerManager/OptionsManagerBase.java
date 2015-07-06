package com.geospatialcorporation.android.geomobile.library.map.layerManager;

import com.geospatialcorporation.android.geomobile.models.Layers.FeatureInfo;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static java.util.UUID.randomUUID;

/**
 * Created by andre on 6/30/2015.
 */
public abstract class OptionsManagerBase<T, S> implements IOptionsManager<T, S> {

    protected HashMap<Integer, HashMap<UUID, OptionFeature<T>>> mLayerOptions;
    protected HashMap<UUID, S> mVisibleLayers;
    protected HashMap<String, FeatureInfo> mIdFeatureIdMap;

    public OptionsManagerBase(){
        mLayerOptions = new HashMap<>();
        mVisibleLayers = new HashMap<>();
        mIdFeatureIdMap = new HashMap<>();
    }

    //region Interface Methods
    @Override
    public void addOption(int layerId, T mapOption, FeatureInfo featureInfo) {
        if (mLayerOptions.containsKey(layerId)) {
            mLayerOptions.get(layerId).put(randomUUID(), new OptionFeature<>(mapOption, featureInfo));
        } else {
            HashMap<UUID, OptionFeature<T>> p = new HashMap<>();
            p.put(randomUUID(), new OptionFeature<>(mapOption, featureInfo));
            mLayerOptions.put(layerId, p);
        }
    }

    @Override
    public Collection<OptionFeature<T>> getOptionsById(int id) {
        return mLayerOptions.get(id).values();
    }

    @Override
    public List<HashMap<UUID, OptionFeature<T>>> getOption() {
        List<HashMap<UUID, OptionFeature<T>>> values = new ArrayList<>();
        Collection<HashMap<UUID, OptionFeature<T>>> um = mLayerOptions.values();

        for (HashMap<UUID, OptionFeature<T>> m : um) {
            values.add(m);
        }

        return values;
    }

    @Override
    public void remove(int layerId) {
        List<HashMap<UUID, OptionFeature<T>>> values = new ArrayList<>();
        Collection<HashMap<UUID, OptionFeature<T>>> um = mLayerOptions.values();

        for (HashMap<UUID, OptionFeature<T>> m : um) {
            values.add(m);
        }

        for (HashMap<UUID, OptionFeature<T>> cachedOptions : values) {
            for (Map.Entry<UUID, OptionFeature<T>> entry : cachedOptions.entrySet()) {

                UUID key = entry.getKey();

                if (mVisibleLayers.containsKey(key)) {

                    removeLayer(key);
                }

            }
        }


        mLayerOptions.remove(layerId);
    }
    //endregion

    protected abstract void removeLayer(UUID key);

    @Override
    public FeatureInfo getFeatureIdLayerId(String id) {
        return mIdFeatureIdMap.get(id);
    }

    @Override
    public Iterable<S> getShowingLayers(){
        return mVisibleLayers.values();
    }


}
