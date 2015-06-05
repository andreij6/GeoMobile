package com.geospatialcorporation.android.geomobile.models.Query.point;

import com.google.android.gms.maps.model.Marker;

/**
 * Created by andre on 6/4/2015.
 */
public class Min {
    //region Getters & Setters
    public int getGeometryTypeCode() {
        return GeometryTypeCode;
    }

    public void setGeometryTypeCode(int geometryTypeCode) {
        GeometryTypeCode = geometryTypeCode;
    }

    public double getY() {
        return Y;
    }

    public void setY(double y) {
        Y = y;
    }

    public double getX() {
        return X;
    }

    public void setX(double x) {
        X = x;
    }

    //endregion

    int GeometryTypeCode;
    double X;
    double Y;

    public Min(Marker marker){
        GeometryTypeCode = 1;
        X = marker.getPosition().latitude;
        Y = marker.getPosition().longitude;
    }

    @Override
    public String toString(){
        return "Latitude: " + X + " Longitude: " + Y;
    }

}
