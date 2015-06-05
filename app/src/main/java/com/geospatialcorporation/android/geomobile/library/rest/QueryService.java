package com.geospatialcorporation.android.geomobile.library.rest;

import com.geospatialcorporation.android.geomobile.models.Query.quickSearch.QuickSearchRequest;
import com.geospatialcorporation.android.geomobile.models.Query.quickSearch.QuickSearchResponse;

import java.util.List;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.POST;

/**
 * Created by andre on 6/4/2015.
 */
public interface QueryService {

    @POST("/API/Query/Quick")
    void quickSearch(@Body QuickSearchRequest request, Callback<List<QuickSearchResponse>> cb);
}