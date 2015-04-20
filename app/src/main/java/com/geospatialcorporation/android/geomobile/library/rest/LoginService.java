package com.geospatialcorporation.android.geomobile.library.rest;

import com.geospatialcorporation.android.geomobile.models.Client;

import java.util.List;

import retrofit.client.Response;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;

public interface LoginService {
    @POST("/API/Auth/Mobile/Start")
    Response start(@Body String body);

    @POST("/API/Auth/Mobile/Login")
    Response login(@Body String content);

    @POST("/API/Auth/Google")
    Response google(@Body String authToken);

    @GET("/API/Clients")
    List<Client> getClients();

    @GET("/API/Clients/Current")
    Client getCurrentClient();

    @POST("/API/Clients/Switch")
    Response setClient(@Body int clientId);
}
