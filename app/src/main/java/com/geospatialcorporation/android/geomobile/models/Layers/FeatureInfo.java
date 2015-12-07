package com.geospatialcorporation.android.geomobile.models.Layers;


public class FeatureInfo {
    String FeatureId;
    int LayerId;

    public String getFeatureId() {
        return FeatureId;
    }

    public void setFeatureId(String featureId) {
        FeatureId = featureId;
    }

    public int getLayerId() {
        return LayerId;
    }

    public void setLayerId(int layerId) {
        LayerId = layerId;
    }

    public FeatureInfo(int layerId, String featureId){
        FeatureId = featureId;
        LayerId = layerId;
    }
}
