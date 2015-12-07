package com.geospatialcorporation.android.geomobile.library.DI.Tasks.models;

import com.geospatialcorporation.android.geomobile.ui.Interfaces.IPostExecuter;

public class GetLayersTaskParams {
    private IPostExecuter mExecuter;

    public GetLayersTaskParams(IPostExecuter executer){
        mExecuter = executer;
    }

    public IPostExecuter getExecuter() {
        return mExecuter;
    }
}
