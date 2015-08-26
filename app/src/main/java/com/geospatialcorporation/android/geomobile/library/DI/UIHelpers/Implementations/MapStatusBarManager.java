package com.geospatialcorporation.android.geomobile.library.DI.UIHelpers.Implementations;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.geospatialcorporation.android.geomobile.application;
import com.geospatialcorporation.android.geomobile.library.DI.UIHelpers.Interfaces.IMapStatusBarManager;
import com.geospatialcorporation.android.geomobile.ui.fragments.GoogleMapFragment;

public class MapStatusBarManager implements IMapStatusBarManager {


    LinearLayout mLoadingBar;
    TextView mMessage;
    //int mShowingCount;
    //int mExtentTotal;
    GoogleMapFragment mMapFragment;

    public MapStatusBarManager(){
        mMapFragment = (GoogleMapFragment)application.getMainActivity().getContentFragment();
    }

    @Override
    public void setMessage(String message) {
        setProperties();

        mLoadingBar.setVisibility(View.VISIBLE);
        mMessage.setText(message);
    }

    @Override
    public void reset() {
        setProperties();

        mLoadingBar.setVisibility(View.GONE);
        mMessage.setText("");

        //if(mExtentTotal == 0){
        //    mLoadingBar.setVisibility(View.GONE);
        //    mMessage.setText("");
        //} else {
        //    mLoadingBar.setVisibility(View.VISIBLE);
        //    mMessage.setText("Showing" + mShowingCount + " of " + mExtentTotal + " features in Extent");
        //}
    }

    protected void setProperties(){
        mLoadingBar = mMapFragment.getLoadingBar();
        mMessage = mMapFragment.getStatusBarMessage();
    }
}
