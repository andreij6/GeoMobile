package com.geospatialcorporation.android.geomobile.models.Layers;

import com.google.android.gms.maps.model.LatLng;

public class Extent {

    //region Properties
    private Integer GeometryType;
    private Point Min;
    private Point Max;
    //endregion

    //region Getters & Setters
    public Integer getGeometryType() {
        return GeometryType;
    }

    public void setGeometryType(Integer geometryType) {
        GeometryType = geometryType;
    }

    public Point getMin() {
        return Min;
    }

    public void setMin(Point min) {
        Min = min;
    }

    public Point getMax() {
        return Max;
    }

    public void setMax(Point max) {
        Max = max;
    }
    //endregion

    public LatLng getMinLatLng() {
        return new LatLng(Min.getY(), Min.getX());
    }

    public LatLng getMaxLatLng() {
        return new LatLng(Max.getY(), Max.getX());
    }

}
