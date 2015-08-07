package com.geospatialcorporation.android.geomobile.models;

import com.geospatialcorporation.android.geomobile.R;
import com.geospatialcorporation.android.geomobile.application;

public abstract class AreYouSureRequest {

    protected String mMessage;

    public AreYouSureRequest(){
        mMessage = application.getAppContext().getString(R.string.are_you_sure_remove_map_feature_doc);
    }

    public String getMessage(){
        return mMessage;
    }
}
