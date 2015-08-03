package com.geospatialcorporation.android.geomobile.library.validation.requests;

import com.geospatialcorporation.android.geomobile.application;

/**
 * Created by andre on 6/19/2015.
 */
public class ValidateRequestBase {

    protected Boolean mIsValid;
    protected String mMessage;

    protected String getString(int id){
        return application.getAppContext().getResources().getString(id);
    }

}
