package com.geospatialcorporation.android.geomobile.ui.fragments;

import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
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

import java.util.List;

import butterknife.InjectView;
import retrofit.RetrofitError;

public class ClientFragment extends Fragment {

    private static final String TAG = ClientFragment.class.getName();

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
                mDataSet = service.getClients();
            } catch (RetrofitError e) {
                if (e.getResponse() != null) {
                    Log.d(TAG, e.getResponse().getStatus() + " : Line 112");
                }
            }

            return mDataSet;
        }

        @Override
        protected void onPostExecute(List<Client> clients){
            ClientAdapter adapter = new ClientAdapter(getActivity(), mDataSet);

            mRecyclerView.setAdapter(adapter);

        }
    }
}