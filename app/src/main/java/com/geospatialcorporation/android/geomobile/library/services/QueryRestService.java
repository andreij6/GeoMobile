package com.geospatialcorporation.android.geomobile.library.services;

import com.geospatialcorporation.android.geomobile.application;
import com.geospatialcorporation.android.geomobile.library.requestcallback.RequestCallback;
import com.geospatialcorporation.android.geomobile.library.requestcallback.listener_implementations.MapQueryListener;
import com.geospatialcorporation.android.geomobile.library.rest.QueryService;
import com.geospatialcorporation.android.geomobile.models.Layers.Layer;
import com.geospatialcorporation.android.geomobile.models.Query.map.MapDefaultQueryRequest;

import retrofit.client.Response;

/**
 * Created by andre on 6/24/2015.
 */
public class QueryRestService  {

    QueryService mService;

    public QueryRestService(){
        mService = application.getRestAdapter().create(QueryService.class);
    }

    public void mapQuery(MapDefaultQueryRequest request, Layer layer){
        mService.map(request, new RequestCallback<>(new MapQueryListener(layer)));
    }
}
