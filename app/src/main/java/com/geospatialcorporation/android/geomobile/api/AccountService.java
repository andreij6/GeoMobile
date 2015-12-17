package com.geospatialcorporation.android.geomobile.api;

import com.geospatialcorporation.android.geomobile.models.UserAccount;

import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;

public interface AccountService {
    @GET("/API/Account/Profile")
    UserAccount getProfile();

    @POST("/API/Account/Profile")
    UserAccount postChanges(@Body UserAccount userAccount);
}
