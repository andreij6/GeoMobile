package com.geospatialcorporation.android.geomobile.library.DI.Tasks.models;

import com.geospatialcorporation.android.geomobile.ui.Interfaces.IPostExecuter;

public abstract class ExecuterParamsBase {

    protected IPostExecuter mExecuter;

    public ExecuterParamsBase(IPostExecuter executer) {
        mExecuter = executer;
    }

    public IPostExecuter getExecuter(){
        return mExecuter;
    }
}
