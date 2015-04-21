package com.geospatialcorporation.android.geomobile.library.rest;

import com.geospatialcorporation.android.geomobile.models.Library.Document;

import java.util.List;

import retrofit.http.GET;
import retrofit.http.Path;

/**
 * Created by andre on 4/21/2015.
 */
public interface FolderService {
    @GET("/API/Folders/{folderId}/Documents")
    List<Document> getFolderDocuments(@Path("id") int folderId);
}
