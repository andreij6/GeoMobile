package com.geospatialcorporation.android.geomobile.library.rest;

import com.geospatialcorporation.android.geomobile.models.Layers.Layer;
import com.geospatialcorporation.android.geomobile.models.Library.Document;

import java.util.List;

import retrofit.http.GET;

/**
 * Created by Administrator on 4/15/2015.
 */
public interface TreeService {
    @GET("/API/Tree/Layers")
    List<Layer> getLayers();

    @GET("/API/Tree/Library")
    List<Document> getLibrary();
}
