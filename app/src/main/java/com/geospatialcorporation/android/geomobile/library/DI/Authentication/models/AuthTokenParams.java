package com.geospatialcorporation.android.geomobile.library.DI.Authentication.models;

import com.geospatialcorporation.android.geomobile.library.helpers.ProgressDialogHelper;
import com.geospatialcorporation.android.geomobile.ui.GoogleApiActivity;

public abstract class AuthTokenParams {
    public int activity_auth_request_code;
    public String account_name;
    public GoogleApiActivity google_context;
    public ProgressDialogHelper mProgess_helper;
}
