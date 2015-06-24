package com.geospatialcorporation.android.geomobile.library.requestcallback.listener_implementations;

import android.support.v4.app.Fragment;
import android.util.Log;
import android.widget.Toast;

import com.geospatialcorporation.android.geomobile.R;
import com.geospatialcorporation.android.geomobile.application;
import com.geospatialcorporation.android.geomobile.ui.MainActivity;

import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by andre on 6/20/2015.
 */
public abstract class RequestListenerBase {
    private static final String TAG = RequestListenerBase.class.getSimpleName();

    protected Fragment mContentFragment;
    protected Boolean mShouldRefresh;

    public RequestListenerBase(Boolean shouldRefresh){
        mShouldRefresh = shouldRefresh;
    }

    public void onSuccess(Response response) {
        Toaster(getString(R.string.success));

        MainActivity activity = getMainActivity();

        mContentFragment = activity.getContentFragment();
    }

    public void onFailure(RetrofitError error) {
        Log.d(TAG, error.getMessage());

        Toaster(getString(R.string.error));
    }

    protected MainActivity getMainActivity(){
        return application.getMainActivity();
    }

    protected void Toaster(String message){
        Toast.makeText(getMainActivity(), message, Toast.LENGTH_LONG).show();
    }

    protected String getString(int stringId){
        return getMainActivity().getString(stringId);
    }


}
