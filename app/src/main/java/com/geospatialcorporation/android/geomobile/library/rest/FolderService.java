package com.geospatialcorporation.android.geomobile.library.rest;

import com.geospatialcorporation.android.geomobile.models.Folders.Folder;
import com.geospatialcorporation.android.geomobile.models.Folders.FolderCreateRequest;
import com.geospatialcorporation.android.geomobile.models.Folders.FolderCreateResponse;
import com.geospatialcorporation.android.geomobile.models.Layers.Layer;
import com.geospatialcorporation.android.geomobile.models.Library.Document;
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

public interface FolderService {
    @GET("/API/Folders?folderType={type}")
    List<Folder> getFolders(@Path("type") String type);

    @GET("/API/Folders/{folderId}")
    Folder getFolderById(@Path("folderId") int folderId);

    @GET("/API/Folders/{folderId}/Folders")
    List<Folder> getFoldersByFolder(@Path("folderId") int folderId);

    @GET("/API/Folders/{folderId}/Layers")
    List<Layer> getLayersByFolder(@Path("folderId") int folderId);

    @GET("/API/Folders/{folderId}/Documents")
    List<Document> getDocumentsByFolder(@Path("folderId") int folderId);

    @POST("/API/Folders")
    void createFolder(@Body FolderCreateRequest createRequest, Callback<FolderCreateResponse> cb);

    @DELETE("/API/Folders/{folderId}")
    void delete(@Path("folderId") int folderId, Callback<Folder> cb);

    @PUT("/API/Folders/{folderId}/Rename")
    void rename(@Path("folderId") int folderId, @Body RenameRequest name, Callback<Response> cb);
}
