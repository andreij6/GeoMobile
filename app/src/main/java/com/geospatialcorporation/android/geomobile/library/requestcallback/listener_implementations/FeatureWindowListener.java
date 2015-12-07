package com.geospatialcorporation.android.geomobile.library.requestcallback.listener_implementations;


import com.geospatialcorporation.android.geomobile.application;
import com.geospatialcorporation.android.geomobile.library.requestcallback.RequestListener;
import com.geospatialcorporation.android.geomobile.models.Query.map.response.featurewindow.FeatureQueryResponse;
import com.geospatialcorporation.android.geomobile.models.Query.map.response.featurewindow.ParcelableFeatureQueryResponse;

import java.util.List;

public class FeatureWindowListener extends RequestListenerBase<List<FeatureQueryResponse>> implements RequestListener<List<FeatureQueryResponse>> {

    private static final String TAG = FeatureWindowListener.class.getSimpleName();

    //region Constructors
    public FeatureWindowListener() {
        super(true);
    }

    public FeatureWindowListener(Boolean shouldRefresh) {
        super(shouldRefresh);
    }
    //endregion

    @Override
    public void onSuccess(List<FeatureQueryResponse> response) {
        super.onSuccess(response);

        application.getFeatureWindowCtrl().showFeatureWindow(new ParcelableFeatureQueryResponse(response));

    }
}
