package com.geospatialcorporation.android.geomobile.library.rest;

import com.geospatialcorporation.android.geomobile.models.Layers.Layer;
import com.geospatialcorporation.android.geomobile.models.Layers.LayerCreateRequest;
import com.geospatialcorporation.android.geomobile.models.Layers.LayerCreateResponse;
import com.geospatialcorporation.android.geomobile.models.RenameRequest;

import java.util.List;

import retrofit.Callback;
import retrofit.client.Response;
import retrofit.http.Body;
import retrofit.http.DELETE;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.Path;

public interface LayerService {

    @POST("/API/Layers")
    void createLayer(@Body LayerCreateRequest layer, Callback<LayerCreateResponse> cb);

    @GET("/API/Layers")
    List<Layer> getLayers();

    @GET("/API/Layers/{id}")
    Layer getLayer(@Path("id") int id);

    @GET("/API/Layers/{id}/Sublayers")
    List<Layer> getSublayers(@Path("id") int id);

    @GET("/API/Layers/{id}/Details")
    List<Layer> getDetails(@Path("id") int id);

    @PUT("/API/Layers/{id}/Rename")
    void rename(@Path("id") int id, @Body RenameRequest rename, Callback<Response> cb);

    @DELETE("/API/Layers/{id}")
    void delete(@Path("id") int id, Callback<Layer> cb);

    @GET("/API/Layers/{id}/Style")
    Layer.StyleInfo getStyle(@Path("id") int id);
}
