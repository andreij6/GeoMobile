package com.geospatialcorporation.android.geomobile.library.map.layerManager;

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
public abstract class OptionsManagerBase<T, S> implements IOptionsManager<T> {

    protected HashMap<Integer, HashMap<UUID, T>> mLayerOptions;
    protected HashMap<UUID, S> mVisibleLayers;
    protected HashMap<String, String[]> mIdFeatureIdMap;

    public OptionsManagerBase(){
        mLayerOptions = new HashMap<>();
        mVisibleLayers = new HashMap<>();
        mIdFeatureIdMap = new HashMap<>();
    }

    //region Interface Methods
    @Override
    public void addOption(int layerId, T mapOption) {
        if (mLayerOptions.containsKey(layerId)) {
            mLayerOptions.get(layerId).put(randomUUID(), mapOption);
        } else {
            HashMap<UUID, T> p = new HashMap<>();
            p.put(randomUUID(), mapOption);
            mLayerOptions.put(layerId, p);
        }
    }

    @Override
    public Collection<T> getOptionsById(int id) {
        return mLayerOptions.get(id).values();
    }

    @Override
    public List<HashMap<UUID, T>> getOption() {
        List<HashMap<UUID, T>> values = new ArrayList<>();
        Collection<HashMap<UUID, T>> um = mLayerOptions.values();

        for (HashMap<UUID, T> m : um) {
            values.add(m);
        }

        return values;
    }

    @Override
    public void remove(int layerId) {
        List<HashMap<UUID, T>> values = new ArrayList<>();
        Collection<HashMap<UUID, T>> um = mLayerOptions.values();

        for (HashMap<UUID, T> m : um) {
            values.add(m);
        }

        for (HashMap<UUID, T> cachedOptions : values) {
            for (Map.Entry<UUID, T> entry : cachedOptions.entrySet()) {

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
    public String[] getFeatureId(String id) {
        return mIdFeatureIdMap.get(id);
    }
}
