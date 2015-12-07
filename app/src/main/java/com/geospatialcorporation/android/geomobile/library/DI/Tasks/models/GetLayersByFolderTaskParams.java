package com.geospatialcorporation.android.geomobile.library.DI.Tasks.models;

import android.content.Context;
import android.support.v4.app.FragmentManager;

import com.geospatialcorporation.android.geomobile.ui.Interfaces.IPostExecuter;

public class GetLayersByFolderTaskParams {
    private FragmentManager mManager;
    private Context mContext;
    private IPostExecuter mExecuter;

    public GetLayersByFolderTaskParams(FragmentManager manager, Context context, IPostExecuter fragment){
        mManager = manager;
        mContext = context;
        mExecuter = fragment;
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
