package com.geospatialcorporation.android.geomobile.library.DI.Tasks.models;

public class UserLoginModel {

    private String mPassword;
    private String mEmail;

    public UserLoginModel(String email, String password) {
        mEmail = email;
        mPassword = password;
    }

    public String getPassword() {
        return mPassword;
    }

    public String getEmail() {
        return mEmail;
    }
}
