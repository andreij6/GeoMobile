package com.geospatialcorporation.android.geomobile.ui.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;

import com.geospatialcorporation.android.geomobile.R;
import com.geospatialcorporation.android.geomobile.models.LayerItem;
import com.geospatialcorporation.android.geomobile.ui.adapters.LayerItemAdapter;
import com.geospatialcorporation.android.geomobile.ui.adapters.RecyclerAdapter;

import java.util.ArrayList;

/**
 * Created by andre on 4/7/2015.
 */
public class LayerItemsFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private ArrayList<LayerItem> mDataSet;
    private View mRootView;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mRootView = inflater.inflate(R.layout.fragment_layeritems, container, false);

        mRecyclerView = (RecyclerView)mRootView.findViewById(R.id.layeritem_recyclerView);

        //TODO: API CALL to set mDataSet
        mDataSet = new ArrayList<>();

        LayerItemAdapter adapter = new LayerItemAdapter(getActivity(), mDataSet);

        mRecyclerView.setAdapter(adapter);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(container.getContext());

        mRecyclerView.setLayoutManager(layoutManager);

        return mRootView;
    }

}
