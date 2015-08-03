package com.geospatialcorporation.android.geomobile.library.DI.Tasks.Implementations;

import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.geospatialcorporation.android.geomobile.application;
import com.geospatialcorporation.android.geomobile.library.DI.Tasks.Interfaces.IGetClientsTask;
import com.geospatialcorporation.android.geomobile.library.DI.Tasks.models.GetClientsTaskParams;
import com.geospatialcorporation.android.geomobile.library.rest.LoginService;
import com.geospatialcorporation.android.geomobile.models.Client;
import com.geospatialcorporation.android.geomobile.models.GeoAsyncTask;
import com.geospatialcorporation.android.geomobile.ui.adapters.recycler.ClientAdapter;

import java.util.ArrayList;
import java.util.List;

import retrofit.RetrofitError;

public class GetClientsTask implements IGetClientsTask {

    private static final String TAG = GetClientsTask.class.getSimpleName();

    public void getClients(GetClientsTaskParams params) {
        new GetClientsAsync(params).execute();
    }

    protected class GetClientsAsync extends GeoAsyncTask<Void, Void, List<Client>> {

        List<Client> mDataSet;
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
        protected List<Client> doInBackground(Void... params) {
            try {
                mDataSet = filter(mLoginService.getClients(), mClientTypeCode);
            } catch (RetrofitError e) {
                if (e.getResponse() != null) {
                    Log.d(TAG, e.getResponse().getStatus() + " : Line 112");
                }
            }

            return mDataSet;
        }

        private List<Client> filter(List<Client> clients, int clientTypeCode) {
            List<Client> filtered  = new ArrayList<>();

            for(Client client : clients){
                if(client.getType() == mClientTypeCode){
                    filtered.add(client);
                }
            }

            return filtered;
        }

    }
}
