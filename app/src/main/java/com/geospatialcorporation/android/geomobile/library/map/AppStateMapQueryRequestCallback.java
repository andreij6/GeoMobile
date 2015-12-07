package com.geospatialcorporation.android.geomobile.library.map;


import com.geospatialcorporation.android.geomobile.application;
import com.geospatialcorporation.android.geomobile.library.DI.Map.Interfaces.ILayerManager;
import com.geospatialcorporation.android.geomobile.library.services.QueryRestService;
import com.geospatialcorporation.android.geomobile.models.Layers.LegendLayer;
import com.geospatialcorporation.android.geomobile.models.Query.map.MapDefaultQueryRequest;

public class AppStateMapQueryRequestCallback implements GeoCallback {
    private static final String TAG = AppStateMapQueryRequestCallback.class.getSimpleName();

    LegendLayer mLegendLayer;
    MapDefaultQueryRequest mRequest;
    QueryRestService mService;
    ILayerManager mLayerManager;

    public AppStateMapQueryRequestCallback(MapDefaultQueryRequest request, LegendLayer legendLayer) {
        mRequest = request;
        mLegendLayer = legendLayer;
        mService = new QueryRestService();
        mLayerManager = application.getLayerManager();
    }

    @Override
    public void invokeCallback() {
        mLegendLayer.getLayer().setIsShowing(true);

        mService.mapQuery(mRequest, mLegendLayer);
    }
}
