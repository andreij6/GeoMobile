package com.geospatialcorporation.android.geomobile.library.rest;

import com.geospatialcorporation.android.geomobile.models.UserAccount;

import retrofit.http.GET;

/**
 * Created by andre on 4/21/2015.
 */
public interface AccountService {
    @GET("/API/Account/Profile")
    UserAccount getProfile();
}
