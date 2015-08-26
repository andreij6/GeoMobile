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

    @Override
    public void setMessage(String message) {
        try {
            setProperties();
            mLoadingBar.setVisibility(View.VISIBLE);
            mMessage.setText(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void reset() {
        try {
            setProperties();

            mLoadingBar.setVisibility(View.GONE);
            mMessage.setText("");
        } catch (Exception e) {
            e.printStackTrace();
        }



        //if(mExtentTotal == 0){
        //    mLoadingBar.setVisibility(View.GONE);
        //    mMessage.setText("");
        //} else {
        //    mLoadingBar.setVisibility(View.VISIBLE);
        //    mMessage.setText("Showing" + mShowingCount + " of " + mExtentTotal + " features in Extent");
        //}
    }

    protected void setProperties() throws Exception{
        mMapFragment = (GoogleMapFragment)application.getMainActivity().getContentFragment();
        mLoadingBar = mMapFragment.getLoadingBar();
        mMessage = mMapFragment.getStatusBarMessage();
    }
}
