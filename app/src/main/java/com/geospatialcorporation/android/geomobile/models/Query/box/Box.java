package com.geospatialcorporation.android.geomobile.models.Query.box;

import com.geospatialcorporation.android.geomobile.models.Query.point.Max;
import com.geospatialcorporation.android.geomobile.models.Query.point.Min;

/**
 * Created by andre on 6/4/2015.
 */
public class Box {
    //region Getters & Setters
    public int getGeometryTypeCode() {
        return GeometryTypeCode;
    }

    public void setGeometryTypeCode(int geometryTypeCode) {
        GeometryTypeCode = geometryTypeCode;
    }

    public Max getMax() {
        return mMax;
    }

    public void setMax(Max max) {
        mMax = max;
    }

    public Min getMin() {
        return mMin;
    }

    public void setMin(Min min) {
        mMin = min;
    }
    //endregion

    int GeometryTypeCode;
    Max mMax;
    Min mMin;

    public Box(Max max, Min min){
        mMax = max;
        mMin = min;
        GeometryTypeCode = 8;
    }
}
