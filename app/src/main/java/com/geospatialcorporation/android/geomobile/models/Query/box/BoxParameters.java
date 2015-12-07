package com.geospatialcorporation.android.geomobile.models.Query.box;

import com.geospatialcorporation.android.geomobile.models.Query.ParametersBase;


public class BoxParameters extends ParametersBase {
    //region Getters & Setters
    public Box getBox() {
        return mBox;
    }

    public void setBox(Box box) {
        mBox = box;
    }
    //endregion

    Box mBox;
}
