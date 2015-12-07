package com.geospatialcorporation.android.geomobile.library.rest;

import com.geospatialcorporation.android.geomobile.models.Query.box.BoxQueryRequest;
import com.geospatialcorporation.android.geomobile.models.Query.map.MapDefaultQueryRequest;
import com.geospatialcorporation.android.geomobile.models.Query.map.response.featurewindow.FeatureQueryResponse;
import com.geospatialcorporation.android.geomobile.models.Query.map.response.mapquery.MapQueryResponse;
import com.geospatialcorporation.android.geomobile.models.Query.point.PointQueryRequest;
import com.geospatialcorporation.android.geomobile.models.Query.quickSearch.QuickSearchRequest;
import com.geospatialcorporation.android.geomobile.models.Query.quickSearch.QuickSearchResponse;

import java.util.List;

import retrofit.Callback;
import retrofit.client.Response;
import retrofit.http.Body;
import retrofit.http.POST;

public interface QueryService {

    @POST("/API/Query/Quick")
    void quickSearch(@Body QuickSearchRequest request, Callback<List<QuickSearchResponse>> cb);

    @POST("/API/Query/Map/Box")
    void boxQuery(@Body BoxQueryRequest request, Callback<Response> cb);

    @POST("/API/Query/Map/Point")
    void pointQuery(@Body PointQueryRequest request, Callback<Response> cb);

    @POST("/API/Query/Map")
    void map(@Body MapDefaultQueryRequest request, Callback<List<MapQueryResponse>> cb);

    @POST("/API/Query/Map")
    void featureWindow(@Body MapDefaultQueryRequest request, Callback<List<FeatureQueryResponse>> cb);
}