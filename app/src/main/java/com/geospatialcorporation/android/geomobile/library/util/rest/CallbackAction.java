package com.geospatialcorporation.android.geomobile.library.util.rest;

import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

/**
 * Created by andre on 4/7/2015.
 */
public abstract class CallbackAction {
    public void run(){}

    public void run(Response response){}

    public void run(Request request, Exception e){};
}
