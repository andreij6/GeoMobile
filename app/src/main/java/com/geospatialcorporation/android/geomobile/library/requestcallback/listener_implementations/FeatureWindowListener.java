package com.geospatialcorporation.android.geomobile.library.requestcallback.listener_implementations;

import android.util.Log;

import com.geospatialcorporation.android.geomobile.application;
import com.geospatialcorporation.android.geomobile.library.requestcallback.RequestListener;
import com.geospatialcorporation.android.geomobile.models.Query.map.response.featurewindow.FeatureQueryResponse;
import com.geospatialcorporation.android.geomobile.models.Query.map.response.mapquery.MapQueryResponse;

import java.util.List;

/**
 * Created by andre on 7/1/2015.
 */
public class FeatureWindowListener extends RequestListenerBase<List<FeatureQueryResponse>> implements RequestListener<List<FeatureQueryResponse>> {

    private static final String TAG = FeatureWindowListener.class.getSimpleName();

    //region Constructors
    public FeatureWindowListener(Boolean shouldRefresh) {
        super(shouldRefresh);
    }

    public FeatureWindowListener() {
        super(false);
    }
    //endregion

    @Override
    public void onSuccess(List<FeatureQueryResponse> response) {
        super.onSuccess(response);

        application.getMapFragment().showFeatureWindow(response);
    }
}
