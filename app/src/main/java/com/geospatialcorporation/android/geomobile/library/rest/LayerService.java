package com.geospatialcorporation.android.geomobile.library.rest;

import com.geospatialcorporation.android.geomobile.models.Layers.Layer;

import java.util.List;

import retrofit.http.Body;
import retrofit.http.DELETE;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.Path;

public interface LayerService {
    @POST("/API/Layers")
    Layer createLayer(@Body Layer layer);

    @GET("/API/Layers/{id}/Sublayers")
    List<Layer> getSublayers(@Path("id") int id);

    @GET("/API/Layers/{id}/Details")
    List<Layer> getDetails(@Path("id") int id);

    @PUT("/API/Layers/{id}/Rename")
    void rename(@Path("id") int id, @Body Layer.Rename rename);

    @DELETE("/API/Layers/{id}")
    void delete(@Path("id") int id);

    @GET("/API/Layers/{id}/Style")
    Layer.StyleInfo getStyle(@Path("id") int id);
}
