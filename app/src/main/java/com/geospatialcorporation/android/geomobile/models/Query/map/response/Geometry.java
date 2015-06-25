package com.geospatialcorporation.android.geomobile.models.Query.map.response;

/**
 * Created by andre on 6/24/2015.
 */
public class Geometry {
    Integer GeometryTypeCode;
    double X;
    double Y;
    double Z;
    double M;

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
    //endregion
}
