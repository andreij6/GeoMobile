package com.geospatialcorporation.android.geomobile.library.rest;

import retrofit.client.Response;
import retrofit.http.GET;
import retrofit.http.Path;

public interface DocumentService {

    // Ex here: https://medium.com/@giuder91/retrofit-how-to-download-get-a-file-e83a9badcf6c
    @GET("/API/Documents/{id}")
    Response download(@Path("id") int id);
}
