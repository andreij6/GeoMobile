package com.geospatialcorporation.android.geomobile.library.DI.Tasks.models;

import com.geospatialcorporation.android.geomobile.ui.Interfaces.IFullExecuter;

public class UserLoginModel {

    private String mPassword;
    private String mEmail;
    private IFullExecuter mExecuter;

    public UserLoginModel(String email, String password, IFullExecuter loginActivity) {
        mEmail = email;
        mPassword = password;
        mExecuter = loginActivity;

    }

    public String getPassword() {
        return mPassword;
    }

    public String getEmail() {
        return mEmail;
    }

    public IFullExecuter getExecuter() {
        return mExecuter;
    }
}
