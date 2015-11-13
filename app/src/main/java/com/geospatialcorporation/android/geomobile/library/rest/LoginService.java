package com.geospatialcorporation.android.geomobile.library.rest;

import com.geospatialcorporation.android.geomobile.library.util.Authentication;
import com.geospatialcorporation.android.geomobile.models.ClientSearchFilter;
import com.geospatialcorporation.android.geomobile.models.ClientSearchResponse;
import com.geospatialcorporation.android.geomobile.models.PluginSubscriptionResponse;
import com.geospatialcorporation.android.geomobile.models.Subscription;

import java.util.List;

import retrofit.Callback;
import retrofit.client.Response;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.Headers;
import retrofit.http.POST;
import retrofit.http.Path;
import retrofit.http.Query;
import retrofit.mime.TypedString;

public interface LoginService {
    //@Headers("X-GeoUnderground: Version " + Authentication.version + ';' + Authentication.versionId + ';' + Authentication.deviceId)
    @POST("/API/Auth/Mobile/Start")
    void start(@Header("X-GeoUnderground") String version, @Body TypedString body, Callback<Response> callback);

    //@Headers("X-GeoUnderground: Version " + Authentication.version + ';' + Authentication.versionId + ';' + Authentication.deviceId)
    @POST("/API/Auth/Mobile/Login")
    void login(@Header("X-GeoUnderground") String version, @Header("X-Signature") String signature, @Body TypedString content, Callback<Response> callback);

    @POST("/API/Auth/Google")
    Response google(@Body String authToken);

    @GET("/API/Clients")
    List<Subscription> getClients();

    @POST("/API/Clients/Search")
    List<Subscription> searchClients(@Query("type") int type, @Query("pagesize") int pageSize);

    @POST("/Plugins/Map/SSP/Clients/Search")
    PluginSubscriptionResponse searchPluginClients(@Query("type") int type);

    @GET("/API/Clients/Current")
    Subscription getCurrentClient();

    @POST("/API/Clients/Switch")
    Response setClient(@Body int clientId);


}
