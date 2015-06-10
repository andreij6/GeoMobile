package com.geospatialcorporation.android.geomobile.library.rest;

import com.geospatialcorporation.android.geomobile.models.Layers.Layer;

import java.util.List;

import retrofit.http.GET;
import retrofit.http.Path;

/**
 * Created by andre on 6/8/2015.
 */
public interface SublayerService {

    @GET("/API/Layers/{id}/Sublayers")
    List<Layer> getSublayers(@Path("id") int id);

}
