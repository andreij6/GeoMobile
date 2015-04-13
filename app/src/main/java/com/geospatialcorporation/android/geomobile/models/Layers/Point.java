package com.geospatialcorporation.android.geomobile.models.Layers;

/**
 * Created by andre on 4/7/2015.
 */
public class Point {
    //region Properties
    private Integer GeometryType;
    private double X;
    private double Y;
    //endregion

    //region Constructors
    public Point(){}

    public Point(Integer geometryType, double x, double y){
        GeometryType = geometryType;
        X = x;
        Y = y;
    }
    //endregion

    //region Getters & Setters
    public Integer getGeometryType() {
        return GeometryType;
    }

    public void setGeometryType(Integer geometryType) {
        GeometryType = geometryType;
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
    //endregion

}
