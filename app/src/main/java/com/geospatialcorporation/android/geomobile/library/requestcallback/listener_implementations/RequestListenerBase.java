package com.geospatialcorporation.android.geomobile.library.requestcallback.listener_implementations;

import android.support.v4.app.Fragment;
import android.util.Log;
import android.widget.Toast;

import com.geospatialcorporation.android.geomobile.R;
import com.geospatialcorporation.android.geomobile.application;
import com.geospatialcorporation.android.geomobile.ui.Interfaces.IGeoMainActivity;

import retrofit.RetrofitError;

public abstract class RequestListenerBase<T> {
    private static final String TAG = RequestListenerBase.class.getSimpleName();

    protected Fragment mContentFragment;
    protected Boolean mShouldRefresh;
    //IGeoAnalytics mAnalytics;

    public RequestListenerBase(Boolean shouldRefresh){
        mShouldRefresh = shouldRefresh;
        //mAnalytics = application.getAnalyticsComponent().provideGeoAnalytics();
    }

    public void onSuccess(T response) {
        mContentFragment = getMainActivity().getContentFragment();
    }

    public void onFailure(RetrofitError error) {
        Log.d(TAG, error.getMessage());

        Toaster(getString(R.string.error));
    }

    protected IGeoMainActivity getMainActivity(){
        return application.getMainActivity();
    }

    protected void Toaster(String message){
        Toast.makeText(application.getMainActivity(), message, Toast.LENGTH_LONG).show();
    }

    protected String getString(int stringId){
        return application.getMainActivity().getString(stringId);
    }

}
