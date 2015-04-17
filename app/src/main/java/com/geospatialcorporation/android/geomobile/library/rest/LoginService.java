package com.geospatialcorporation.android.geomobile.library.rest;

import retrofit.client.Response;
import retrofit.http.Body;
import retrofit.http.POST;

public interface LoginService {
    @POST("/API/Auth/Mobile/Start")
    Response start(@Body String body);

    @POST("/API/Auth/Mobile/Login")
    Response login(@Body String content);

    @POST("/API/Auth/Google")
    Response google(@Body String authToken);
}
