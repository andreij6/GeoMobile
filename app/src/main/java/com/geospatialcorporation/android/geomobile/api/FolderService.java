package com.geospatialcorporation.android.geomobile.api;

import com.geospatialcorporation.android.geomobile.models.Document.Document;
import com.geospatialcorporation.android.geomobile.models.Folders.Folder;
import com.geospatialcorporation.android.geomobile.models.Folders.FolderCreateRequest;
import com.geospatialcorporation.android.geomobile.models.Folders.FolderDetailsResponse;
import com.geospatialcorporation.android.geomobile.models.Folders.FolderPermissionsResponse;
import com.geospatialcorporation.android.geomobile.models.Layers.Layer;
import com.geospatialcorporation.android.geomobile.models.PermissionsSaveRequest;
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
    void createFolder(@Body FolderCreateRequest createRequest, Callback<Response> cb);

    @DELETE("/API/Folders/{folderId}")
    void remove(@Path("folderId") int folderId, Callback<Response> cb);

    @PUT("/API/Folders/{folderId}/Rename")
    void rename(@Path("folderId") int folderId, @Body RenameRequest name, Callback<Response> cb);

    @GET("/API/Folders/{folderId}/Details")
    FolderDetailsResponse getFolderDetail(@Path("folderId") int folderId);

    @GET("/API/Folders/{folderId}/Permissions")
    List<FolderPermissionsResponse> getFolderPermission(@Path("folderId") int folderId);

    @PUT("/API/Folders/{folderId}/Permissions")
    void saveFolderPermissions(int folderId, @Body PermissionsSaveRequest permissions);

}
