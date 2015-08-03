package com.geospatialcorporation.android.geomobile.library.map.layerManager;

import com.geospatialcorporation.android.geomobile.models.Layers.FeatureInfo;

/**
 * Created by andre on 7/6/2015.
 */
public class OptionFeature<T> {
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
}
