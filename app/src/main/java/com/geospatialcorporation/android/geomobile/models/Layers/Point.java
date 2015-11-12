package com.geospatialcorporation.android.geomobile.models.Layers;

import com.geospatialcorporation.android.geomobile.library.constants.GeometryTypeCodes;
import com.google.android.gms.maps.model.LatLng;

public class Point {
    //region Properties
    private Integer GeometryTypeCode;
    private double X;
    private double Y;
    //private double Z;
    //private double M;
    //endregion

    //region Constructors
    public Point(){}

    public Point(Integer geometryType, double x, double y){
        GeometryTypeCode = geometryType;
        X = x;
        Y = y;
    }

    public Point(LatLng position){
        GeometryTypeCode = GeometryTypeCodes.Point;
        X = position.longitude;
        Y = position.latitude;
    }
    //endregion

    //region Getters & Setters
    public Integer getGeometryType() {
        return GeometryTypeCode;
    }

    public void setGeometryType(Integer geometryType) {
        GeometryTypeCode = geometryType;
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

    public LatLng getLatLng() {
        return new LatLng(getY(), getX());
    }

    //public double getM() {
    //    return M;
    //}
    //
    //public void setM(double m) {
    //    M = m;
    //}
    //
    //public double getZ() {
    //    return Z;
    //}
    //
    //public void setZ(double z) {
    //    Z = z;
    //}
    //endregion
}