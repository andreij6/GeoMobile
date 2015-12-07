package com.geospatialcorporation.android.geomobile.library.rest;

import com.geospatialcorporation.android.geomobile.models.AddAttributeRequest;
import com.geospatialcorporation.android.geomobile.models.Layers.LayerAttributeColumn;

import java.util.List;

import retrofit.Callback;
import retrofit.client.Response;
import retrofit.http.Body;
import retrofit.http.DELETE;
import retrofit.http.GET;
import retrofit.http.PUT;
import retrofit.http.Path;

public interface AttributeService {

    @GET("/API/Layers/{layerId}/Attributes/Columns")
    List<LayerAttributeColumn> getLayerAttributeColumns(@Path("layerId") int layerId);

    @PUT("/API/Layers/{layerId}/Attributes/Columns")
    void addLayerAttributeColumn(@Path("layerId") int layerId, @Body AddAttributeRequest data, Callback<Response> cb);

    @DELETE("/API/Layers/{id}/Attributes/Columns/{columnId}")
    void deleteColumn(@Path("id") int layerId, @Path("columnId") int columnId);
}
