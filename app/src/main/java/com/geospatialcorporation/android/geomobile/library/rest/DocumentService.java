package com.geospatialcorporation.android.geomobile.library.rest;

import com.geospatialcorporation.android.geomobile.models.Document.Document;
import com.geospatialcorporation.android.geomobile.models.Document.DocumentCreateResponse;
import com.geospatialcorporation.android.geomobile.models.Document.MoveRequest;
import com.geospatialcorporation.android.geomobile.models.RenameRequest;

import retrofit.Callback;
import retrofit.client.Response;
import retrofit.http.Body;
import retrofit.http.DELETE;
import retrofit.http.GET;
import retrofit.http.Multipart;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.Part;
import retrofit.http.Path;
import retrofit.mime.TypedFile;

public interface DocumentService {

    // Ex here: https://medium.com/@giuder91/retrofit-how-to-download-get-a-file-e83a9badcf6c
    @GET("/API/Documents/{id}")
    Response details(@Path("id") int id);

    @Multipart
    @POST("/API/Folders/{folderId}/Documents")
    void create(@Path("folderId") int folderId, @Part("file") TypedFile file, Callback<Response> cb);  //was DocumentCreateResponse

    @DELETE("/API/Documents/{documentId}")
    void delete(@Path("documentId") int documentId, Callback<Document> cb);

    @GET("/API/Documents/{documentId}/File")
    void download(@Path("documentId") int documentId, Callback<Response> cb);

    @PUT("/API/Documents/{documentId}/Rename")
    void rename(@Path("documentId") int documentId, @Body RenameRequest rename, Callback<Response> cb);

    @POST("/API/Documents/{id}/Move")
    void moveDocument(@Path("id") int documentId, @Body MoveRequest request, Callback<Response> cb);
}
