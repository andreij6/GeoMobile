package com.geospatialcorporation.android.geomobile.library.DI.UIHelpers.Implementations.StatusBarManager;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.geospatialcorporation.android.geomobile.application;
import com.geospatialcorporation.android.geomobile.library.DI.UIHelpers.Interfaces.IMapStatusBarManager;
import com.geospatialcorporation.android.geomobile.library.constants.GeometryTypeCodes;
import com.geospatialcorporation.android.geomobile.library.helpers.ProgressDialogHelper;
import com.geospatialcorporation.android.geomobile.ui.Interfaces.IMapStatusCtrl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public abstract class StatusBarManagerBase implements IMapStatusBarManager{

    public static final Integer MARKERS = 1;
    public static final Integer LINES = 2;
    public static final Integer POLYGONS = 3;

    HashMap<Integer, List<String>> LoadedMap;
    LinearLayout mLoadingBar;
    TextView mMessage;
    ProgressDialogHelper mProgressDialogHelper;
    HashMap<UUID, HashMap<Integer, Boolean>> showLayersMap;

    public StatusBarManagerBase(){
        LoadedMap = new HashMap<>();
        showLayersMap = new HashMap<>();
    }

    @Override
    public void showLayersMessage(String message, UUID uniqueId){
        try {
            setProperties();
            mMessage.setText(message);
        } catch (Exception e) {
            e.printStackTrace();
        }

        HashMap<Integer, Boolean> innerHash = new HashMap<>();

        innerHash.put(LINES, true);
        innerHash.put(MARKERS, true);
        innerHash.put(POLYGONS, true);

        showLayersMap.put(uniqueId, innerHash);
    }

    @Override
    public void ensureStatusBarVisible(){
        mLoadingBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void finished(int geometryCode, UUID uniqueId){
        HashMap<Integer, Boolean> innerHash = showLayersMap.get(uniqueId);

        innerHash.put(geometryCode, false);

        if(!innerHash.values().contains(true)){
            reset();
        }

    }

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

    protected abstract IMapStatusCtrl getMapStatusCtrl();

    protected void setProperties() throws Exception {
        //mMapFragment = (GoogleMapFragment)application.getMainActivity().getContentFragment();
        IMapStatusCtrl mapFragment = getMapStatusCtrl();
        if(mapFragment != null) {
            mLoadingBar = (LinearLayout)mapFragment.getLoadingBar();
            mMessage = mapFragment.getStatusBarMessage();
            mLoadingBar.setVisibility(View.GONE);
            mMessage.setText("");
        }
    };

}
