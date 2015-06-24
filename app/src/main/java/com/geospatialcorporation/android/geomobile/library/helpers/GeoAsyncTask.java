package com.geospatialcorporation.android.geomobile.library.helpers;

import android.os.AsyncTask;

/**
 * Created by andre on 6/22/2015.
 */
//was built to show a progress bar during slower async tasks.  We dont currently use it but i this class still could be useful at some point
public abstract class GeoAsyncTask<Params, Progress, Result> extends AsyncTask<Params, Progress, Result> {

    ProgressDialogHelper mProgressHelper;

    public GeoAsyncTask(){
        //mProgressHelper = new ProgressDialogHelper(application.getMainActivity());
    }

    @Override
    protected void onPreExecute(){
        //mProgressHelper.toggleProgressDialog();
    }

    protected abstract Result doInBackground(Params... params);

    @Override
    protected void onPostExecute(Result result) {
       //mProgressHelper.toggleProgressDialog();
    }
}
