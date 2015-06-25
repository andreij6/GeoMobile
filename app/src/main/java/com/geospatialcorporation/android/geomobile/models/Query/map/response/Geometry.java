package com.geospatialcorporation.android.geomobile.models.Query.map.response;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

public class Geometry {
    Integer GeometryTypeCode;
    double X;
    double Y;
    double Z;
    double M;

    List<Geometry> Points;

    //region Getters & Setters
    public Integer getGeometryTypeCode() {
        return GeometryTypeCode;
    }

    public void setGeometryTypeCode(Integer geometryTypeCode) {
        GeometryTypeCode = geometryTypeCode;
    }

    public double getX() {
        return X;
    }

    public void setX(double x) {
        X = x;
    }

    public double getY() {
        return Y;
    }

    public void setY(double y) {
        Y = y;
    }

    public double getZ() {
        return Z;
    }

    public void setZ(double z) {
        Z = z;
    }

    public double getM() {
        return M;
    }

    public void setM(double m) {
        M = m;
    }

    public List<Geometry> getPoints() {
        return Points;
    }

    public void setPoints(List<Geometry> points) {
        Points = points;
    }

    public LatLng getLatLng() {
        return new LatLng(getY(), getX());
    }
    //endregion
}
