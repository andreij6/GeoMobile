package com.geospatialcorporation.android.geomobile.ui.fragments;

import android.app.Fragment;
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
import com.geospatialcorporation.android.geomobile.ui.adapters.ClientAdapter;
import com.geospatialcorporation.android.geomobile.models.Client;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import retrofit.client.Response;

public class ClientFragment extends Fragment {
    private static final String TAG = "ClientFragment";
    private RecyclerView mRecyclerView;
    private ArrayList<Client> mDataSet;
    private View mRootView;
    private LoginService service;
    private Gson gson = new Gson();

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        service = application.getRestAdapter().create(LoginService.class);

        mRootView = inflater.inflate(R.layout.fragment_clientitems, container, false);

        mRecyclerView = (RecyclerView)mRootView.findViewById(R.id.clientitem_recyclerview);

        Response clients = service.getClients();

        Type clientListType = new TypeToken<ArrayList<Client>>(){}.getType();
        mDataSet = gson.fromJson(clients.getBody().toString(), clientListType);

        ClientAdapter adapter = new ClientAdapter(getActivity(), mDataSet);

        mRecyclerView.setAdapter(adapter);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(container.getContext());

        mRecyclerView.setLayoutManager(layoutManager);

        return mRootView;
    }
}