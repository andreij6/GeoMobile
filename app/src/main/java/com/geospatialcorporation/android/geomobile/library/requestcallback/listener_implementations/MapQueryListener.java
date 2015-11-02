package com.geospatialcorporation.android.geomobile.library.requestcallback.listener_implementations;

import android.support.v4.app.Fragment;

import com.geospatialcorporation.android.geomobile.application;
import com.geospatialcorporation.android.geomobile.library.DI.Tasks.Interfaces.IMapFeaturesTask;
import com.geospatialcorporation.android.geomobile.library.requestcallback.RequestListener;
import com.geospatialcorporation.android.geomobile.models.Layers.LegendLayer;
import com.geospatialcorporation.android.geomobile.models.Query.map.response.mapquery.MapQueryResponse;
import com.geospatialcorporation.android.geomobile.ui.fragments.GoogleMapFragment;

import java.util.List;

import retrofit.RetrofitError;

/**
 * Created by andre on 6/24/2015.
 */
public class MapQueryListener extends RequestListenerBase<List<MapQueryResponse>> implements RequestListener<List<MapQueryResponse>> {

    private static final String TAG = MapQueryListener.class.getSimpleName();

    IMapFeaturesTask mGeoMapper;
    LegendLayer mLLayer;

    public MapQueryListener(Boolean shouldRefresh) {
        super(shouldRefresh);
        mGeoMapper = application.getTasksComponent().provideMapFeaturesTask();
    }

    public MapQueryListener(LegendLayer llayer){
        super(false);
        mGeoMapper = application.getTasksComponent().provideMapFeaturesTask();
        mLLayer = llayer;
    }

    @Override
    public void onSuccess(List<MapQueryResponse> response) {
        super.onSuccess(response);

        mGeoMapper.mapFeatures(response, mLLayer);

        //set Layer Loading
        if(!application.getIsTablet()) {
            Fragment contentFrag = application.getMainActivity().getContentFragment();

            if (contentFrag instanceof GoogleMapFragment) {
                ((GoogleMapFragment) contentFrag).hideLoadingBar();
            }
        }
    }

    @Override
    public void onFailure(RetrofitError error) {
        super.onFailure(error);
        try {
            mLLayer.getCheckBox().setEnabled(true);
        } catch (Exception e){
            
        }
    }
}
