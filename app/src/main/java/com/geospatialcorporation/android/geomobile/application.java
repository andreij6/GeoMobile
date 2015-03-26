package com.geospatialcorporation.android.geomobile;

import android.app.Application;
import android.content.Context;

/**
 * Created by Administrator on 3/17/2015.
 */
public class application extends Application {
    private static Context context;

    public void onCreate(){
        super.onCreate();
        application.context = getApplicationContext();
    }

    public static Context getAppContext() {
        return application.context;
    }
}
