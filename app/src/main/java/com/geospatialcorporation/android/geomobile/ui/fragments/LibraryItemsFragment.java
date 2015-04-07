package com.geospatialcorporation.android.geomobile.ui.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.provider.LiveFolders;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.geospatialcorporation.android.geomobile.R;
import com.geospatialcorporation.android.geomobile.models.LibraryItem;
import com.geospatialcorporation.android.geomobile.ui.adapters.LibraryItemAdapter;

import java.util.ArrayList;

/**
 * Created by andre on 4/7/2015.
 */
public class LibraryItemsFragment extends Fragment{

    //region Properties
    private RecyclerView mRecyclerView;
    private ArrayList<LibraryItem> mDataSet;
    private View mRootView;
    //endregion

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mRootView = inflater.inflate(R.layout.fragment_libraryitems, container, false);

        mRecyclerView = (RecyclerView)mRootView.findViewById(R.id.libraryitem_recyclerView);

        //TODO: API CALL to set mDataSet
        mDataSet = new ArrayList<>();

        LibraryItemAdapter adapter = new LibraryItemAdapter(getActivity(), mDataSet);

        mRecyclerView.setAdapter(adapter);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(container.getContext());

        mRecyclerView.setLayoutManager(layoutManager);

        return mRootView;
    }

}
