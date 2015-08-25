package com.geospatialcorporation.android.geomobile.library.DI.Tasks.Implementations;

import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.geospatialcorporation.android.geomobile.application;
import com.geospatialcorporation.android.geomobile.library.DI.Tasks.Interfaces.IGetClientsTask;
import com.geospatialcorporation.android.geomobile.library.DI.Tasks.models.GetClientsTaskParams;
import com.geospatialcorporation.android.geomobile.library.rest.LoginService;
import com.geospatialcorporation.android.geomobile.models.ClientSearchFilter;
import com.geospatialcorporation.android.geomobile.models.ClientSearchResponse;
import com.geospatialcorporation.android.geomobile.models.Subscription;
import com.geospatialcorporation.android.geomobile.models.GeoAsyncTask;

import java.util.ArrayList;
import java.util.List;

import retrofit.RetrofitError;

public class GetClientsTask implements IGetClientsTask {

    private static final String TAG = GetClientsTask.class.getSimpleName();

    public void getClients(GetClientsTaskParams params) {
        new GetClientsAsync(params).execute();
    }

    protected class GetClientsAsync extends GeoAsyncTask<Void, Void, List<Subscription>> {

        List<Subscription> mDataSet;
        int mClientTypeCode;
        FragmentActivity mContext;
        LoginService mLoginService;

        public GetClientsAsync(GetClientsTaskParams params) {
            super(params.getExecuter());
            mDataSet = params.getDataSet();
            mClientTypeCode = params.getClientTypeCode();
            mContext = params.getContext();
            mLoginService = application.getRestAdapter().create(LoginService.class);
        }

        @Override
        protected List<Subscription> doInBackground(Void... params) {
            try {
                //ClientSearchResponse response = mLoginService.searchClients(new ClientSearchFilter(mClientTypeCode));
                //List<Subscription> Items = response.getItems();
                //for (Subscription item: Items) {
                //    mDataSet.add(item);
                //}

                mDataSet = mLoginService.getClients();

            } catch (RetrofitError e) {
                if (e.getResponse() != null) {
                    Log.d(TAG, e.getResponse().getStatus() + " : Line 112");
                }
            }

            return mDataSet;
        }

    }
}
