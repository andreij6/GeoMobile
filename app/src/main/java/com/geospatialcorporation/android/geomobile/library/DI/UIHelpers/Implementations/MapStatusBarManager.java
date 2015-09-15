package com.geospatialcorporation.android.geomobile.library.DI.UIHelpers.Implementations;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.geospatialcorporation.android.geomobile.application;
import com.geospatialcorporation.android.geomobile.library.DI.UIHelpers.Interfaces.IMapStatusBarManager;
import com.geospatialcorporation.android.geomobile.ui.fragments.GoogleMapFragment;

import java.util.ArrayList;
import java.util.List;

public class MapStatusBarManager implements IMapStatusBarManager {


    public MapStatusBarManager(){
        layers = new ArrayList<>();
    }

    LinearLayout mLoadingBar;
    TextView mMessage;
    //int mShowingCount;
    //int mExtentTotal;
    GoogleMapFragment mMapFragment;
    List<String> layers;

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
    public void setLayerMessage(String layer){

        if(layer != null) {
            layers.add(layer);
        }

        try {
            setProperties();
            mLoadingBar.setVisibility(View.VISIBLE);

            String layerMessage = getLayerMessage();

            mMessage.setText(layerMessage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void removeLayer(String name) {
        int index = layers.indexOf(name);

        layers.remove(index);

        if(layers.size() > 0){
            setLayerMessage(null);
        } else {
            reset();
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

    }



    protected void setProperties() throws Exception{
        mMapFragment = (GoogleMapFragment)application.getMainActivity().getContentFragment();
        mLoadingBar = mMapFragment.getLoadingBar();
        mMessage = mMapFragment.getStatusBarMessage();
    }

    public String getLayerMessage() {
        StringBuilder result = new StringBuilder();
        result.append("Loading ");

        for(String layerName : layers){
            result.append(layerName + ", ");
        }

        return result.toString();
    }
}
