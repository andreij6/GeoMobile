package com.geospatialcorporation.android.geomobile.ui.fragments;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.geospatialcorporation.android.geomobile.R;
import com.geospatialcorporation.android.geomobile.application;
import com.geospatialcorporation.android.geomobile.library.rest.LoginService;
import com.geospatialcorporation.android.geomobile.models.Client;
import com.geospatialcorporation.android.geomobile.ui.adapters.ClientAdapter;

import java.util.ArrayList;
import java.util.List;

import retrofit.RetrofitError;

/**
 * Created by andre on 6/1/2015.
 */
public class ClientSelectorFragment extends Fragment {
    int mClientTypeCode;
    Context mContext;

    public ClientSelectorFragment initialize(int clientTypeCode, Context context){
        this.mClientTypeCode = clientTypeCode;
        this.mContext = context;
        return this;
    }

    private static final String TAG = ClientSelectorFragment.class.getName();

    private RecyclerView mRecyclerView;
    private List<Client> mDataSet;
    private LoginService service;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View mRootView;

        mRootView = inflater.inflate(R.layout.fragment_clientitems, container, false);

        mRecyclerView = (RecyclerView)mRootView.findViewById(R.id.clientitem_recyclerView);

        service = application.getRestAdapter().create(LoginService.class);

        new GetClientsTask().execute();

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(container.getContext());

        mRecyclerView.setLayoutManager(layoutManager);

        return mRootView;
    }

    private class GetClientsTask extends AsyncTask<Void, Void, List<Client>> {
        @Override
        protected List<Client> doInBackground(Void... params) {
            try {
                mDataSet = filter(service.getClients(), mClientTypeCode);
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

        @Override
        protected void onPostExecute(List<Client> clients){
            ClientAdapter adapter = new ClientAdapter(getActivity(), mDataSet);

            mRecyclerView.setAdapter(adapter);

        }
    }
}
