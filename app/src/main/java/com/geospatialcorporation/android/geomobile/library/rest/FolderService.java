package com.geospatialcorporation.android.geomobile.library.rest;

import com.geospatialcorporation.android.geomobile.models.Folders.Folder;
import com.geospatialcorporation.android.geomobile.models.Layers.Layer;
import com.geospatialcorporation.android.geomobile.models.Library.Document;

import java.util.List;

import retrofit.http.GET;
import retrofit.http.Path;

public interface FolderService {

    @GET("/API/Folders/{folderId}")
    Folder getFolderById(@Path("folderId") int folderId);

    @GET("/API/Folders/{folderId}/Folders")
    List<Folder> getFoldersByFolder(@Path("folderId") int folderId);

    @GET("/API/Folders/{folderId}/Layers")
    List<Layer> getLayersByFolder(@Path("folderId") int folderId);

    @GET("/API/Folders/{folderId}/Documents")
    List<Document> getDocumentsByFolder(@Path("folderId") int folderId);
}
