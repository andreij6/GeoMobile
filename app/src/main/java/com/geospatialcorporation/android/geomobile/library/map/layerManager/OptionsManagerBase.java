package com.geospatialcorporation.android.geomobile.library.map.layerManager;

import android.app.Activity;
import android.util.Log;

import com.geospatialcorporation.android.geomobile.application;
import com.geospatialcorporation.android.geomobile.library.DI.UIHelpers.Interfaces.IMapStatusBarManager;
import com.geospatialcorporation.android.geomobile.models.Layers.FeatureInfo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static java.util.UUID.randomUUID;

public abstract class OptionsManagerBase<T, S> implements IOptionsManager<T, S> {

    protected HashMap<Integer, HashMap<UUID, OptionFeature<T>>> mLayerOptions;
    protected HashMap<Integer, HashMap<UUID, OptionFeature<T>>> mRemovedLayerOptions;
    protected HashMap<UUID, S> mVisibleLayers;
    protected HashMap<String, FeatureInfo> mIdFeatureIdMap;
    protected IMapStatusBarManager mStatusBarManager;

    public OptionsManagerBase(){
        mLayerOptions = new HashMap<>();
        mVisibleLayers = new HashMap<>();
        mIdFeatureIdMap = new HashMap<>();
        mRemovedLayerOptions = new HashMap<>();
        mStatusBarManager = application.getStatusBarManager();

    }
    public Activity getActivity() {
        return application.getMainActivity();
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
    public Iterable<Integer> getShowingLayerIds() {
        return mLayerOptions.keySet();
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
        HashMap<UUID, OptionFeature<T>> layerHashMap = mLayerOptions.get(layerId);

        if(layerHashMap != null) {
            for (UUID m : layerHashMap.keySet()) {
                HashMap<UUID, OptionFeature<T>> map = new HashMap<>();
                map.put(m, layerHashMap.get(m));
                values.add(map);
            }
        }

        for (HashMap<UUID, OptionFeature<T>> cachedOptions : values) {
            for (Map.Entry<UUID, OptionFeature<T>> entry : cachedOptions.entrySet()) {

                UUID key = entry.getKey();

                if (mVisibleLayers.containsKey(key)) {

                    removeLayer(key);
                }

            }
        }

        mRemovedLayerOptions.put(layerId, mLayerOptions.get(layerId));
        mLayerOptions.remove(layerId);
    }

    @Override
    public void clearVisibleLayers() {

        try {
            for (UUID key : mVisibleLayers.keySet()) {
                removeMapObject(key);
            }
            mVisibleLayers.clear();
        } catch (Exception e) {
            Log.e("Base", e.getMessage());
        }

    }

    @Override
    public boolean isLayerCached(int layerId){
        if(mRemovedLayerOptions.containsKey(layerId)){
            mLayerOptions.put(layerId, mRemovedLayerOptions.get(layerId));
            return true;
        }

        if(mLayerOptions.containsKey(layerId)){
            return true;
        }

        return false;
    }

    //endregion

    protected void removeLayer(UUID key) {
        removeMapObject(key);
        mVisibleLayers.remove(key);
    }

    protected abstract void removeMapObject(UUID key);

    @Override
    public FeatureInfo getFeatureIdLayerId(String markerId) {
        return mIdFeatureIdMap.get(markerId);
    }

    public List<String> getMarkerIds(String FeatureId){
        List<String> result = new ArrayList<>();

        for(String key : mIdFeatureIdMap.keySet()){
            if(mIdFeatureIdMap.get(key).getFeatureId().equals(FeatureId)){
                result.add(key);
            }
        }

        return result;
    }

    @Override
    public Iterable<S> getShowingLayers(){
        return mVisibleLayers.values();
    }


}
