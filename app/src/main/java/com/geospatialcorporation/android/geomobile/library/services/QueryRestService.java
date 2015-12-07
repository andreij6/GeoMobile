package com.geospatialcorporation.android.geomobile.library.services;

import com.geospatialcorporation.android.geomobile.application;
import com.geospatialcorporation.android.geomobile.library.requestcallback.RequestCallback;
import com.geospatialcorporation.android.geomobile.library.requestcallback.listener_implementations.FeatureWindowListener;
import com.geospatialcorporation.android.geomobile.library.requestcallback.listener_implementations.MapQueryListener;
import com.geospatialcorporation.android.geomobile.library.rest.QueryService;
import com.geospatialcorporation.android.geomobile.models.Layers.LegendLayer;
import com.geospatialcorporation.android.geomobile.models.Query.map.Layers;
import com.geospatialcorporation.android.geomobile.models.Query.map.MapDefaultQueryRequest;
import com.geospatialcorporation.android.geomobile.models.Query.map.Options;

import java.util.ArrayList;
import java.util.List;


public class QueryRestService  {

    QueryService mService;

    public QueryRestService(){
        mService = application.getRestAdapter().create(QueryService.class);
    }

    public void mapQuery(MapDefaultQueryRequest request, LegendLayer llayer){
        mService.map(request, new RequestCallback<>(new MapQueryListener(llayer)));
    }

    public void featureWindow(String featureId, int layerId) {
        List<String> featureIds = makeList(featureId);

        Layers layers = new Layers(featureIds, layerId);

        MapDefaultQueryRequest request = new MapDefaultQueryRequest(makeList(layers), Options.FEATURE_WINDOW);  //TODO add featureId and layerId to request

        mService.featureWindow(request, new RequestCallback<>(new FeatureWindowListener()));
    }

    protected <T> List<T> makeList(T item) {
        List<T> result = new ArrayList<>(1);
        result.add(item);
        return result;
    }
}
