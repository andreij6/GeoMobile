package com.geospatialcorporation.android.geomobile;

import android.app.Application;
import android.content.Context;

import com.facebook.stetho.Stetho;
import com.google.android.gms.common.api.GoogleApiClient;

public class application extends Application {
    private static Context context;
    private static GoogleApiClient googleClient;
    private static String geoAuthToken;

    public void onCreate(){
        super.onCreate();

        Stetho.initialize(
                Stetho.newInitializerBuilder(this)
                        .enableDumpapp(
                                Stetho.defaultDumperPluginsProvider(this))
                        .enableWebKitInspector(
                                Stetho.defaultInspectorModulesProvider(this))
                        .build());

        application.context = getApplicationContext();
    }

    public static Context getAppContext() {
        return application.context;
    }

    public static void setGoogleClient(GoogleApiClient client) {
        googleClient = client;
    }

    public static GoogleApiClient getGoogleClient() {
        return googleClient;
    }

    public static void setAuthToken(String token) {
        geoAuthToken = token;
    }

    public static String getAuthToken() {
        return geoAuthToken;
    }
}
