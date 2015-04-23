package com.geospatialcorporation.android.geomobile.library.rest;

import com.geospatialcorporation.android.geomobile.models.UserAccount;

import retrofit.http.GET;

public interface AccountService {
    @GET("/API/Account/Profile")
    UserAccount getProfile();
}
