package com.geospatialcorporation.android.geomobile.library.services.LayerEditor;

public abstract class ShapeModel {
    protected Integer GeometryTypeCode;

    public Integer getGeometryTypeCode() {
        return GeometryTypeCode;
    }

    public void setGeometryTypeCode(Integer geometryTypeCode) {
        GeometryTypeCode = geometryTypeCode;
    }
}
