package com.geospatialcorporation.android.geomobile.library.map;

import com.geospatialcorporation.android.geomobile.library.services.QueryRestService;
import com.geospatialcorporation.android.geomobile.models.Layers.LegendLayer;
import com.geospatialcorporation.android.geomobile.models.Query.map.MapDefaultQueryRequest;

public class SendMapQueryRequestCallback implements GeoCallback {

    LegendLayer mLegendLayer;
    MapDefaultQueryRequest mRequest;
    QueryRestService mService;

    public SendMapQueryRequestCallback(MapDefaultQueryRequest request, LegendLayer legendLayer) {
        mRequest = request;
        mLegendLayer = legendLayer;
        mService = new QueryRestService();
    }

    @Override
    public void invokeCallback() {
        mLegendLayer.getLayer().setIsShowing(true);
        mService.mapQuery(mRequest, mLegendLayer);
    }
}
