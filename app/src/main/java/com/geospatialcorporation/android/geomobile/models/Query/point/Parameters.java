package com.geospatialcorporation.android.geomobile.models.Query.point;

import android.os.Bundle;

import com.geospatialcorporation.android.geomobile.models.Query.ParametersBase;

/**
 * Created by andre on 6/9/2015.
 */
public class Parameters extends ParametersBase {

    //region Getters and Setters
    public Point getPoint() {
        return mPoint;
    }

    public void setPoint(Point point) {
        mPoint = point;
    }

    public double getBuffer() {
        return mBuffer;
    }

    public void setBuffer(double buffer) {
        mBuffer = buffer;
    }
    //endregion

    double mBuffer;
    Point mPoint;
}
