package com.geospatialcorporation.android.geomobile.library.rest;

import com.geospatialcorporation.android.geomobile.models.Library.DocumentCreateResponse;

import retrofit.Callback;
import retrofit.client.Response;
import retrofit.http.GET;
import retrofit.http.Multipart;
import retrofit.http.POST;
import retrofit.http.Part;
import retrofit.http.Path;
import retrofit.mime.TypedFile;

public interface DocumentService {

    // Ex here: https://medium.com/@giuder91/retrofit-how-to-download-get-a-file-e83a9badcf6c
    @GET("/API/Documents/{id}")
    Response details(@Path("id") int id);

    @Multipart
    @POST("/API/Folders/{folderId}/Documents")
    void create(@Path("folderId") int folderId, @Part("file") TypedFile file, Callback<DocumentCreateResponse> cb);

}
