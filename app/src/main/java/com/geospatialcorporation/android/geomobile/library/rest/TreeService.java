package com.geospatialcorporation.android.geomobile.library.rest;

import com.geospatialcorporation.android.geomobile.models.Folders.Folder;

import java.util.List;

import retrofit.http.GET;

/**
 * Created by Administrator on 4/15/2015.
 */
public interface TreeService {
    @GET("/API/Tree/Layer")
    List<Folder> getLayers();

    @GET("/API/Tree/Library")
    List<Folder> getLibrary();
}
