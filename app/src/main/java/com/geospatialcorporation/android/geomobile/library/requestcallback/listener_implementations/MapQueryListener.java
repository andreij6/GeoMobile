package com.geospatialcorporation.android.geomobile.library.requestcallback.listener_implementations;

import com.geospatialcorporation.android.geomobile.library.map.GeoMapper;
import com.geospatialcorporation.android.geomobile.library.requestcallback.RequestListener;
import com.geospatialcorporation.android.geomobile.models.Layers.LegendLayer;
import com.geospatialcorporation.android.geomobile.models.Query.map.response.mapquery.MapQueryResponse;

import java.util.List;

/**
 * Created by andre on 6/24/2015.
 */
public class MapQueryListener extends RequestListenerBase<List<MapQueryResponse>> implements RequestListener<List<MapQueryResponse>> {

    private static final String TAG = MapQueryListener.class.getSimpleName();

    GeoMapper mGeoMapper;
    LegendLayer mLLayer;

    public MapQueryListener(Boolean shouldRefresh) {
        super(shouldRefresh);
        mGeoMapper = new GeoMapper();
    }

    public MapQueryListener(LegendLayer llayer){
        super(false);
        mGeoMapper = new GeoMapper();
        mLLayer = llayer;
    }

    @Override
    public void onSuccess(List<MapQueryResponse> response) {
        super.onSuccess(response);

        mGeoMapper.map(response, mLLayer);
    }


}
