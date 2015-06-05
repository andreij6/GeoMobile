package com.geospatialcorporation.android.geomobile.models.Query.box;

/**
 * Created by andre on 6/4/2015.
 */
public class BoxQueryRequest {
    //region Getters & Setters
    public Options getOptions() {
        return mOptions;
    }

    public void setOptions(Options options) {
        mOptions = options;
    }

    public Parameters getParameters() {
        return mParameters;
    }

    public void setParameters(Parameters parameters) {
        mParameters = parameters;
    }
    //endregion

    Options mOptions;
    Parameters mParameters;
}
