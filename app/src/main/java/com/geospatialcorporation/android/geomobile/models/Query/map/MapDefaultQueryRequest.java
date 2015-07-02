package com.geospatialcorporation.android.geomobile.models.Query.map;


import java.util.List;

/**
 * Created by andre on 6/24/2015.
 */
public class MapDefaultQueryRequest {

    public MapDefaultQueryRequest(List<Layers> layers, String optionsCode){
        Parameters = new Parameters(layers);
        Options = new Options(optionsCode);
    }

    //region Getters & Setters
    public Parameters getParameters() {
        return Parameters;
    }

    public void setParameters(Parameters parameters) {
        Parameters = parameters;
    }

    public Options getOptions() {
        return Options;
    }

    public void setOptions(Options options) {
        Options = options;
    }
    //endregion

    Parameters Parameters;
    Options Options;

}
