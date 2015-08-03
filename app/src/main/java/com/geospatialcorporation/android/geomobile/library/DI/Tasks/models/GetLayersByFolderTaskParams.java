package com.geospatialcorporation.android.geomobile.library.DI.Tasks.models;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;

import com.geospatialcorporation.android.geomobile.ui.Interfaces.IPostExecuter;
import com.geospatialcorporation.android.geomobile.ui.fragments.GeoViewFragmentBase;

public class GetLayersByFolderTaskParams {
    private RecyclerView mRecycler;
    private FragmentManager mManager;
    private Context mContext;
    private IPostExecuter mExecuter;

    public GetLayersByFolderTaskParams(RecyclerView recycler, FragmentManager manager, Context context, IPostExecuter fragment){
        mRecycler = recycler;
        mManager = manager;
        mContext = context;
        mExecuter = fragment;
    }

    public RecyclerView getRecycler() {
        return mRecycler;
    }

    public FragmentManager getManager() {
        return mManager;
    }

    public Context getContext() {
        return mContext;
    }

    public IPostExecuter getFragment() {
        return mExecuter;
    }
}
