package com.geospatialcorporation.android.geomobile.models.Query.quickSearch;

import com.geospatialcorporation.android.geomobile.models.Query.point.CenterPoint;

public class QuickSearchResult {
    //region Getters & Setters
    public CenterPoint getCenterPoint() {
        return mCenterPoint;
    }

    public void setCenterPoint(CenterPoint centerPoint) {
        mCenterPoint = centerPoint;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public int getLayerId() {
        return LayerId;
    }

    public void setLayerId(int layerId) {
        LayerId = layerId;
    }

    public String getLayerName() {
        return LayerName;
    }

    public void setLayerName(String layerName) {
        LayerName = layerName;
    }

    public String getValue() {
        return Value;
    }

    public void setValue(String value) {
        Value = value;
    }

    public String getExt() {
        return Ext;
    }

    public void setExt(String ext) {
        Ext = ext;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }
    //endregion

    CenterPoint mCenterPoint;
    String Id;
    int LayerId;
    String LayerName;
    String Value;
    String Ext;
    String Name;
}
