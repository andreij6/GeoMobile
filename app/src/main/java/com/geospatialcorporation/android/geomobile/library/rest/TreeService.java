package com.geospatialcorporation.android.geomobile.library.rest;

import com.geospatialcorporation.android.geomobile.models.Folders.Folder;

import java.util.List;

import retrofit.http.GET;

public interface TreeService {
    @GET("/API/Tree/Layer")
    List<Folder> getLayers();

    @GET("/API/Tree/Document")
    List<Folder> getDocuments();
}
