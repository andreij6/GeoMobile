package com.geospatialcorporation.android.geomobile.models.Query.point;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by andre on 6/9/2015.
 */
public class Point {
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

    public Point(LatLng ll){
        setGeometryTypeCode(1);
        setX(ll.latitude);
        setY(ll.longitude);
    }

    public Point(double lat, double lng){
        setGeometryTypeCode(1);
        setX(lat);
        setY(lng);
    }

    @Override
    public String toString(){
        return "Latitude: " + X + " Longitude: " + Y;
    }

}
