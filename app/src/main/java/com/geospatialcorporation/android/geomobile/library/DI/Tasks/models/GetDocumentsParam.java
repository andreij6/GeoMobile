package com.geospatialcorporation.android.geomobile.library.DI.Tasks.models;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;

import com.geospatialcorporation.android.geomobile.library.panelmanager.ISlidingPanelManager;
import com.geospatialcorporation.android.geomobile.models.Folders.Folder;
import com.geospatialcorporation.android.geomobile.ui.Interfaces.IPostExecuter;

public class GetDocumentsParam {
    private FragmentManager mFragmentManager;
    private Folder mCurrentFolder;
    private RecyclerView mRecycler;
    private Context mContext;
    private IPostExecuter mFragment;
    private ISlidingPanelManager mPanelManager;

    public GetDocumentsParam(FragmentManager fm, Folder currentFolder, Context context, IPostExecuter fragment){
        mFragmentManager = fm;
        mCurrentFolder = currentFolder;
        mContext = context;
        mFragment = fragment;
    }

    public FragmentManager getFragmentManager() {
        return mFragmentManager;
    }

    public void setFragmentManager(FragmentManager fragmentManager) {
        mFragmentManager = fragmentManager;
    }

    public Folder getCurrentFolder() {
        return mCurrentFolder;
    }

    public RecyclerView getRecycler() {
        return mRecycler;
    }

    public Context getContext() {
        return mContext;
    }

    public IPostExecuter getFragment() {
        return mFragment;
    }

    public ISlidingPanelManager getPanelManager(){ return mPanelManager;}
}
