package com.geospatialcorporation.android.geomobile.library.rest;

import com.geospatialcorporation.android.geomobile.models.Layers.Layer;
import com.geospatialcorporation.android.geomobile.models.Layers.SublayerCreateRequest;
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

/**
 * Created by andre on 6/8/2015.
 */
public interface SublayerService {

    @GET("/API/Layers/{id}/Sublayers")
    List<Layer> getSublayers(@Path("id") int id);

    @PUT("/API/Sublayers/{sublayerId}/Rename")
    void rename(@Path("sublayerId") int sublayerId, @Body RenameRequest model, Callback<Response> cb);

    @POST("/API/Sublayers")
    void create(@Body SublayerCreateRequest model, Callback<Response> cb);

    @DELETE("/API/Sublayers/{sublayerId}")
    void delete(@Path("sublayerId") int sublayerId, Callback<Response> cb);
}
