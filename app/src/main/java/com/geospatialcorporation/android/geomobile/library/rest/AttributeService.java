package com.geospatialcorporation.android.geomobile.library.rest;

import com.geospatialcorporation.android.geomobile.models.Layers.LayerAttributeColumns;

import java.util.List;

import retrofit.http.Body;
import retrofit.http.DELETE;
import retrofit.http.GET;
import retrofit.http.PUT;
import retrofit.http.Path;

/**
 * Created by andre on 6/8/2015.
 */
public interface AttributeService {

    @GET("/API/Layers/{layerId}/Attributes/Columns")
    List<LayerAttributeColumns> getLayerAttributeColumns(@Path("layerId") int layerId);

    @PUT("/API/Layers/{layerId}/Attributes/Columns")
    List<LayerAttributeColumns> addLayerAttributeColumn(@Path("layerId") int layerId, @Body List<LayerAttributeColumns> data);

    @DELETE("/API/Layers/{id}/Attributes/Columns/{columnId}")
    void deleteColumn(@Path("id") int layerId, @Path("columnId") int columnId);
}
