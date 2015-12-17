package com.geospatialcorporation.android.geomobile.library.DI.Authentication.models;

import com.afollestad.materialdialogs.MaterialDialog;
import com.geospatialcorporation.android.geomobile.ui.activity.GoogleApiActivity;

public abstract class AuthTokenParams {
    public int activity_auth_request_code;
    public String account_name;
    public GoogleApiActivity google_context;
    public MaterialDialog mProgress_helper;
}
