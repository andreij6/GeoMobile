package com.geospatialcorporation.android.geomobile.library.DI.UIHelpers.Implementations;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.geospatialcorporation.android.geomobile.application;
import com.geospatialcorporation.android.geomobile.library.DI.UIHelpers.Interfaces.IMapStatusBarManager;
import com.geospatialcorporation.android.geomobile.library.constants.GeometryTypeCodes;
import com.geospatialcorporation.android.geomobile.library.helpers.ProgressDialogHelper;
import com.geospatialcorporation.android.geomobile.ui.fragments.GoogleMapFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MapStatusBarManager implements IMapStatusBarManager {


    public static final Integer MARKERS = 1;
    public static final Integer LINES = 2;
    public static final Integer POLYGONS = 3;

    public MapStatusBarManager(){
        layers = new ArrayList<>();
        LoadedMap = new HashMap<>();
    }

    LinearLayout mLoadingBar;
    TextView mMessage;
    GoogleMapFragment mMapFragment;
    List<String> layers;
    HashMap<Integer, List<String>> LoadedMap;
    ProgressDialogHelper mProgressDialogHelper;

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
    public void StartLoading(Integer loadCode){

        switch (loadCode){
            case GeometryTypeCodes.Point:
            case GeometryTypeCodes.MultiPoint:
                loadCode = MapStatusBarManager.MARKERS;
                break;
            case GeometryTypeCodes.Line:
            case GeometryTypeCodes.MultiLine:
                loadCode = MapStatusBarManager.LINES;
                break;
            case GeometryTypeCodes.Polygon:
            case GeometryTypeCodes.MultiPolygon:
                loadCode = MapStatusBarManager.POLYGONS;
                break;
            default:
                break;
        }


        if(LoadedMap.get(loadCode) != null){
            LoadedMap.get(loadCode).add(loadCode + "");
        } else {
            List<String> entry = new ArrayList<>();
            entry.add(loadCode + "");
            LoadedMap.put(loadCode, entry);
        }

        ToggleLoadingLayers();
    }

    @Override
    public void FinishLoading(Integer loadCode) {

        if(LoadedMap.get(loadCode) != null && !LoadedMap.get(loadCode).isEmpty()){
            LoadedMap.get(loadCode).remove(0);
        }

        ToggleLoadingLayers();
    }

    protected void getProgressDialogHelper(){
        if(mProgressDialogHelper == null) {
            if(application.getIsTablet()){
                mProgressDialogHelper = new ProgressDialogHelper(application.getMainTabletActivity());
            } else {
                mProgressDialogHelper = new ProgressDialogHelper(application.getMainActivity());
            }
        }
    }

    protected void ToggleLoadingLayers() {
        if(!application.getIsTablet()) {
            getProgressDialogHelper();

            Boolean isLoading = false;

            for (Integer code : LoadedMap.keySet()) {
                if (!LoadedMap.get(code).isEmpty()) {
                    mProgressDialogHelper.showProgressDialog();
                    isLoading = true;
                    break;
                }
            }

            if (!isLoading) {
                mProgressDialogHelper.hideProgressDialog();
            }
        }

    }

    @Override
    public void reset() {
        try {
            LoadedMap.clear();
            mProgressDialogHelper = null;
            setProperties();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    protected void setProperties() throws Exception{
        mMapFragment = (GoogleMapFragment)application.getMainActivity().getContentFragment();
        if(mMapFragment != null) {
            mLoadingBar = mMapFragment.getLoadingBar();
            mMessage = mMapFragment.getStatusBarMessage();
            mLoadingBar.setVisibility(View.GONE);
            mMessage.setText("");
        }
    }
}
