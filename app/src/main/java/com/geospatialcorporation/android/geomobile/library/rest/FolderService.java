package com.geospatialcorporation.android.geomobile.library.rest;

import com.geospatialcorporation.android.geomobile.models.Library.Document;

import java.util.List;

import retrofit.http.GET;
import retrofit.http.Path;

public interface FolderService {

    @GET("/API/Folders/{folderId}/Files")
    List<Document> getFolderDocuments(@Path("folderId") int folderId);
}
