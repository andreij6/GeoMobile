package com.geospatialcorporation.android.geomobile.library.DI.Tasks.models;

import android.content.Context;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.RecyclerView;

import com.geospatialcorporation.android.geomobile.ui.Interfaces.IPostExecuter;

public class GetLayersTaskParams {
    private DrawerLayout mDrawer;
    private IPostExecuter mExecuter;

    public GetLayersTaskParams(DrawerLayout drawer, IPostExecuter executer){
        mDrawer = drawer;
        mExecuter = executer;
    }

    public DrawerLayout getDrawer() {
        return mDrawer;
    }

    public IPostExecuter getExecuter() {
        return mExecuter;
    }
}
