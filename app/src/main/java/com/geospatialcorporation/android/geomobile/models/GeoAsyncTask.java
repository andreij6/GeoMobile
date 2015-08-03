package com.geospatialcorporation.android.geomobile.models;

import android.os.AsyncTask;

import com.geospatialcorporation.android.geomobile.ui.Interfaces.IPostExecuter;

public abstract class GeoAsyncTask<Params, Progress, Result> extends AsyncTask<Params, Progress, Result> {
    IPostExecuter mExecuter;

    public GeoAsyncTask(IPostExecuter executer){
        mExecuter = executer;
    }

    @Override
    protected void onPostExecute(Result result) {
        super.onPostExecute(result);
        mExecuter.onPostExecute(result);
    }
}
