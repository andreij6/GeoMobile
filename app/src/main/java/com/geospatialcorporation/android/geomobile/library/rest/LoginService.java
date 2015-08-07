package com.geospatialcorporation.android.geomobile.library.rest;

import com.geospatialcorporation.android.geomobile.models.Client;
import com.geospatialcorporation.android.geomobile.ui.LoginActivity;

import java.util.List;

import retrofit.Callback;
import retrofit.client.Response;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.Headers;
import retrofit.http.POST;

public interface LoginService {
    @Headers({
        "Content-Type: application/json",
        "X-GeoUnderground: Version " + LoginActivity.version + ';' + LoginActivity.versionId + ';' + LoginActivity.deviceId
    })
    @POST("/API/Auth/Mobile/Start")
    void start(@Body String body, Callback<String> callback);

    @Headers("X-GeoUnderground: Version " + LoginActivity.version + ';' + LoginActivity.versionId + ';' + LoginActivity.deviceId)
    @POST("/API/Auth/Mobile/Login")
    void login(@Header("X-Signature") String signature, @Body String content, Callback callback);

    @POST("/API/Auth/Google")
    Response google(@Body String authToken);

    @GET("/API/Clients")
    List<Client> getClients();

    @GET("/API/Clients/Current")
    Client getCurrentClient();

    @POST("/API/Clients/Switch")
    Response setClient(@Body int clientId);
}
