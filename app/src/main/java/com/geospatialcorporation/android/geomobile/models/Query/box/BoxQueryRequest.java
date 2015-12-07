package com.geospatialcorporation.android.geomobile.models.Query.box;

import com.geospatialcorporation.android.geomobile.models.Query.Options;


public class BoxQueryRequest {
    //region Getters & Setters
    public Options getOptions() {
        return mOptions;
    }

    public void setOptions(Options options) {
        mOptions = options;
    }

    public BoxParameters getBoxParameters() {
        return mBoxParameters;
    }

    public void setBoxParameters(BoxParameters boxParameters) {
        mBoxParameters = boxParameters;
    }
    //endregion

    Options mOptions;
    BoxParameters mBoxParameters;
}
