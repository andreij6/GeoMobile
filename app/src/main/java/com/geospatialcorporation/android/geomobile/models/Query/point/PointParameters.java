package com.geospatialcorporation.android.geomobile.models.Query.point;

import com.geospatialcorporation.android.geomobile.models.Query.ParametersBase;

public class PointParameters extends ParametersBase {

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
    //endregion

    final double mBuffer = 0.7;
    Point mPoint;
}
