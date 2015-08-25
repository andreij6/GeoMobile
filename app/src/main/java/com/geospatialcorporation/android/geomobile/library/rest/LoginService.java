package com.geospatialcorporation.android.geomobile.library.rest;

import com.geospatialcorporation.android.geomobile.library.util.Authentication;
import com.geospatialcorporation.android.geomobile.models.ClientSearchFilter;
import com.geospatialcorporation.android.geomobile.models.ClientSearchResponse;
import com.geospatialcorporation.android.geomobile.models.Subscription;
import com.geospatialcorporation.android.geomobile.ui.LoginActivity;

import java.util.List;

import retrofit.Callback;
import retrofit.client.Response;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.Headers;
import retrofit.http.POST;
import retrofit.mime.TypedString;

public interface LoginService {
    @Headers("X-GeoUnderground: Version " + Authentication.version + ';' + Authentication.versionId + ';' + Authentication.deviceId)
    @POST("/API/Auth/Mobile/Start")
    void start(@Body TypedString body, Callback<Response> callback);

    @Headers("X-GeoUnderground: Version " + Authentication.version + ';' + Authentication.versionId + ';' + Authentication.deviceId)
    @POST("/API/Auth/Mobile/Login")
    void login(@Header("X-Signature") String signature, @Body TypedString content, Callback<Response> callback);

    @POST("/API/Auth/Google")
    Response google(@Body String authToken);

    @GET("/API/Clients")
    List<Subscription> getClients();

    @POST("/Admin/Clients/Search")
    ClientSearchResponse searchClients(@Body ClientSearchFilter filter);

    @GET("/API/Clients/Current")
    Subscription getCurrentClient();

    @POST("/API/Clients/Switch")
    Response setClient(@Body int clientId);
}
