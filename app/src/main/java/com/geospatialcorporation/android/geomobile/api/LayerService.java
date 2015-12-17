package com.geospatialcorporation.android.geomobile.api;

import com.geospatialcorporation.android.geomobile.library.services.LayerEditor.EditChangeRequest;
import com.geospatialcorporation.android.geomobile.library.services.LayerEditor.LayerFeatureChangeResponse;
import com.geospatialcorporation.android.geomobile.models.Layers.EditLayerAttributesRequest;
import com.geospatialcorporation.android.geomobile.models.Layers.Layer;
import com.geospatialcorporation.android.geomobile.models.Layers.LayerCreateRequest;
import com.geospatialcorporation.android.geomobile.models.Layers.LayerDetailsVm;
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
    void createLayer(@Body LayerCreateRequest layer, Callback<Response> cb);

    @GET("/API/Layers")
    List<Layer> getLayers();

    @GET("/API/Layers/{id}")
    Layer getLayer(@Path("id") int id);

    @GET("/API/Layers/{id}/Details")
    LayerDetailsVm getDetails(@Path("id") int id);

    @PUT("/API/Layers/{id}/Rename")
    void rename(@Path("id") int id, @Body RenameRequest rename, Callback<Response> cb);

    @DELETE("/API/Layers/{id}")
    void delete(@Path("id") int id, Callback<Response> cb);

    @GET("/API/Layers/{id}/Style")
    Layer.StyleInfo getStyle(@Path("id") int id);

    @POST("/API/Layers/{id}/Attributes/Values")
    void editAttributes(@Path("id") int id, @Body EditLayerAttributesRequest request, Callback<Response> cb);

    @PUT("/API/Layers/{id}/Features/{featureId}/Documents/{documentId}")
    void addMapFeatureDocument(@Path("id") int id, @Path("featureId") String featureId, @Path("documentId") int documentId, Callback<Response> cb);

    @DELETE("/API/Layers/{layerId}/Features/{featureId}/Documents/{documentId}")
    void removeMapFeatureDocument(@Path("layerId") int layerId, @Path("featureId") String featureId, @Path("documentId") int documentId, Callback<Response> cb);

    @POST("/API/Layers/{layerId}/ChangeSet")
    void editLayer(@Path("layerId") int layerId, @Body EditChangeRequest changeSet, Callback<LayerFeatureChangeResponse> cb);
}
