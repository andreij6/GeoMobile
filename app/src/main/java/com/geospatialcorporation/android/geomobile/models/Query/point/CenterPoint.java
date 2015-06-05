package com.geospatialcorporation.android.geomobile.models.Query.point;

/**
 * Created by andre on 6/4/2015.
 */
public class CenterPoint {
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
}


