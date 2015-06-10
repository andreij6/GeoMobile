package com.geospatialcorporation.android.geomobile.models.Layers;

import com.geospatialcorporation.android.geomobile.models.NodeDetailsVm;

/**
 * Created by andre on 6/8/2015.
 */
public class LayerDetailsVm extends NodeDetailsVm {
    public long getFeatureCount() {
        return FeatureCount;
    }

    public void setFeatureCount(long featureCount) {
        FeatureCount = featureCount;
    }

    long FeatureCount;
}
