package com.geospatialcorporation.android.geomobile.ui.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.geospatialcorporation.android.geomobile.R;
import com.geospatialcorporation.android.geomobile.ui.adapters.ClientAdapter;
import com.geospatialcorporation.android.geomobile.models.Client;
import com.google.gson.Gson;

import java.util.List;

public class ClientFragment extends Fragment {
    private static final String TAG = "ClientFragment";
    private RecyclerView mRecyclerView;
    private List<Client> mDataSet;
    private View mRootView;
    private Gson gson = new Gson();

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mRootView = inflater.inflate(R.layout.fragment_clientitems, container, false);

        mRecyclerView = (RecyclerView)mRootView.findViewById(R.id.clientitem_recyclerView);

        ClientAdapter adapter = new ClientAdapter(getActivity(), mDataSet);

        mRecyclerView.setAdapter(adapter);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(container.getContext());

        mRecyclerView.setLayoutManager(layoutManager);

        return mRootView;
    }
}