package com.geospatialcorporation.android.geomobile.library.validation.requests;

import com.geospatialcorporation.android.geomobile.application;

public class ValidateRequestBase {

    protected Boolean mIsValid;
    protected String mMessage;

    protected String getString(int id){
        return application.getAppContext().getResources().getString(id);
    }

}
