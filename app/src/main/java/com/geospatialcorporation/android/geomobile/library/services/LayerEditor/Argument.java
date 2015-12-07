package com.geospatialcorporation.android.geomobile.library.services.LayerEditor;


public class Argument {
    int TypeCode;
    ShapeModel MapFeature;
    String MapFeatureId;
    ShapeModel OldFeature;
    int LayerId;

    //region G's & S's
    public int getTypeCode() {
        return TypeCode;
    }

    public void setTypeCode(int typeCode) {
        TypeCode = typeCode;
    }

    public ShapeModel getMapFeature() {
        return MapFeature;
    }

    public void setMapFeature(ShapeModel mapFeature) {
        MapFeature = mapFeature;
    }

    public String getMapFeatureId() {
        return MapFeatureId;
    }

    public void setMapFeatureId(String mapFeatureId) {
        MapFeatureId = mapFeatureId;
    }

    public ShapeModel getOldFeature() {
        return OldFeature;
    }

    public void setOldFeature(ShapeModel oldFeature) {
        OldFeature = oldFeature;
    }

    public int getLayerId() {
        return LayerId;
    }

    public void setLayerId(int layerId) {
        LayerId = layerId;
    }

    //endregion
}
