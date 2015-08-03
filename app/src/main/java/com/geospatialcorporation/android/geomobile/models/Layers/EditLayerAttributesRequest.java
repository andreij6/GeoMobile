package com.geospatialcorporation.android.geomobile.models.Layers;

import java.util.List;

public class EditLayerAttributesRequest {
    public List<AttributeFeatures> getFeatures() {
        return Features;
    }

    public void setFeatures(List<AttributeFeatures> features) {
        Features = features;
    }

    List<AttributeFeatures> Features;

}


