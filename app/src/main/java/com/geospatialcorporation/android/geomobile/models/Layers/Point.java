package com.geospatialcorporation.android.geomobile.models.Layers;

/**
 * Created by andre on 4/7/2015.
 */
public class Point {
    //region Properties
    private Integer mGeometryType;
    private double mX;
    private double mY;
    //endregion

    //region Constructors
    public Point(){}

    public Point(Integer geometryType, double x, double y){
        mGeometryType = geometryType;
        mX = x;
        mY = y;
    }
    //endregion

    //region Getters & Setters
    public Integer getGeometryType() {
        return mGeometryType;
    }

    public void setGeometryType(Integer geometryType) {
        mGeometryType = geometryType;
    }

    public double getX() {
        return mX;
    }

    public void setX(double x) {
        mX = x;
    }

    public double getY() {
        return mY;
    }

    public void setY(double y) {
        mY = y;
    }
    //endregion

}
