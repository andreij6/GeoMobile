package com.geospatialcorporation.android.geomobile.models.Query.point;

import com.geospatialcorporation.android.geomobile.models.Query.Options;

public class PointQueryRequest {
    PointParameters mParameters;
    Options mOptions;

    //region Getters & Setters
    public Options getOptions() {
        return mOptions;
    }

    public void setOptions(Options options) {
        mOptions = options;
    }

    public PointParameters getParameters() {
        return mParameters;
    }

    public void setParameters(PointParameters parameters) {
        mParameters = parameters;
    }
    //endregion

}
