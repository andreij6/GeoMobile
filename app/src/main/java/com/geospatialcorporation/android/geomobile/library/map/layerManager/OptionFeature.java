package com.geospatialcorporation.android.geomobile.library.map.layerManager;

import com.geospatialcorporation.android.geomobile.models.Layers.FeatureInfo;

public class OptionFeature<T> implements Comparable<OptionFeature> {
    T Option;
    FeatureInfo mFeatureInfo;

    //region Gs & Ss
    public FeatureInfo getFeatureInfo() {
        return mFeatureInfo;
    }

    public void setFeatureInfo(FeatureInfo featureInfo) {
        mFeatureInfo = featureInfo;
    }

    public T getOption() {
        return Option;
    }

    public void setOption(T option) {
        Option = option;
    }
    //endregion

    public OptionFeature(T option, FeatureInfo featureInfo){
        Option = option;
        mFeatureInfo = featureInfo;
    }

    @Override
    public int compareTo(OptionFeature another) {

        int tID = Integer.parseInt(mFeatureInfo.getFeatureId());
        int aId = Integer.parseInt(another.getFeatureInfo().getFeatureId());

        return tID - aId;
    }
}
