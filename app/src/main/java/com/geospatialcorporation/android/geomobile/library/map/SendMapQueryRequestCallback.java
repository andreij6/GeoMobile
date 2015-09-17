package com.geospatialcorporation.android.geomobile.library.map;

import android.util.Log;

import com.geospatialcorporation.android.geomobile.application;
import com.geospatialcorporation.android.geomobile.library.DI.Map.Interfaces.ILayerManager;
import com.geospatialcorporation.android.geomobile.library.DI.UIHelpers.Interfaces.IMapStatusBarManager;
import com.geospatialcorporation.android.geomobile.library.services.QueryRestService;
import com.geospatialcorporation.android.geomobile.models.Layers.LegendLayer;
import com.geospatialcorporation.android.geomobile.models.Query.map.MapDefaultQueryRequest;

public class SendMapQueryRequestCallback implements GeoCallback {

    private static final String TAG = SendMapQueryRequestCallback.class.getSimpleName();

    LegendLayer mLegendLayer;
    MapDefaultQueryRequest mRequest;
    QueryRestService mService;
    ILayerManager mLayerManager;
    IMapStatusBarManager MapStatusBarManager;

    public SendMapQueryRequestCallback(MapDefaultQueryRequest request, LegendLayer legendLayer) {
        mRequest = request;
        mLegendLayer = legendLayer;
        mService = new QueryRestService();
        mLayerManager = application.getLayerManager();
        MapStatusBarManager = application.getStatusBarManager();
    }

    @Override
    public void invokeCallback() {
        mLegendLayer.getLayer().setIsShowing(true);

        MapStatusBarManager.StartLoading(mLegendLayer.getLayer().getGeometryTypeCodeId());

        if(mLayerManager.isLayerCached(mLegendLayer.getLayer())){
            mLegendLayer.getCheckBox().setEnabled(true);
            mLayerManager.showLayer(mLegendLayer);
        } else {
            mService.mapQuery(mRequest, mLegendLayer);
        }
    }
}
