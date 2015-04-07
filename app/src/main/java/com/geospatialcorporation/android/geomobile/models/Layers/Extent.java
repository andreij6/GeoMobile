package com.geospatialcorporation.android.geomobile.models.Layers;

/**
 * Created by andre on 4/7/2015.
 */
public class Extent {

    //region Properties
    private Integer mGeometryType;
    private Point mMin;
    private Point mMax;
    //endregion

    //region Getters & Setters
    public Integer getGeometryType() {
        return mGeometryType;
    }

    public void setGeometryType(Integer geometryType) {
        mGeometryType = geometryType;
    }

    public Point getMin() {
        return mMin;
    }

    public void setMin(Point min) {
        mMin = min;
    }

    public Point getMax() {
        return mMax;
    }

    public void setMax(Point max) {
        mMax = max;
    }
    //endregion

}
