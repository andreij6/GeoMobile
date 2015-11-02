package com.geospatialcorporation.android.geomobile.library.map;

import com.geospatialcorporation.android.geomobile.application;
import com.geospatialcorporation.android.geomobile.library.DI.Map.Interfaces.ILayerManager;
import com.geospatialcorporation.android.geomobile.library.services.QueryRestService;
import com.geospatialcorporation.android.geomobile.models.Layers.Layer;
import com.geospatialcorporation.android.geomobile.models.Layers.LegendLayer;
import com.geospatialcorporation.android.geomobile.models.Query.map.MapDefaultQueryRequest;

public class EditLayerMapQueryRequestCallback implements GeoCallback {
    private static final String TAG = EditLayerMapQueryRequestCallback.class.getSimpleName();

    LegendLayer mLegendLayer;
    MapDefaultQueryRequest mRequest;
    ILayerManager mLayerManager;
    QueryRestService mService;


    public EditLayerMapQueryRequestCallback(MapDefaultQueryRequest request, LegendLayer legendLayer) {
        mRequest = request;
        mLegendLayer = legendLayer;
        mService = new QueryRestService();
        mLayerManager = application.getLayerManager();
    }

    @Override
    public void invokeCallback() {
        Layer layer = mLegendLayer.getLayer();

        mLayerManager.addVisibleLayerExtent(layer.getId(), layer.getExtent());

        if(mLayerManager.isLayerCached(mLegendLayer.getLayer())){
            mLayerManager.editLayers(mLegendLayer);

        } else {
            mService.mapQuery(mRequest, mLegendLayer);
        }
    }
}
