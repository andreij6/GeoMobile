package com.geospatialcorporation.android.geomobile;

import android.app.Application;
import android.content.Context;

import com.facebook.stetho.Stetho;
import com.facebook.stetho.okhttp.StethoInterceptor;
import com.google.android.gms.common.api.GoogleApiClient;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.client.OkClient;

public class application extends Application {
    private static Context context;
    private static GoogleApiClient googleClient;
    private static String geoAuthToken;
    private static OkHttpClient client;
    private static RestAdapter restAdapter;

    public void onCreate(){
        super.onCreate();

        Stetho.initialize(
                Stetho.newInitializerBuilder(this)
                        .enableDumpapp(
                                Stetho.defaultDumperPluginsProvider(this))
                        .enableWebKitInspector(
                                Stetho.defaultInspectorModulesProvider(this))
                        .build());

        RequestInterceptor requestInterceptor = new RequestInterceptor() {
            @Override
            public void intercept(RequestInterceptor.RequestFacade request) {
                request.addHeader("User-Agent", "Retrofit-Sample-App");
            }
        };

        client = new OkHttpClient();
        client.networkInterceptors().add(new StethoInterceptor());
        client.interceptors().add(new TokenInterceptor());

        restAdapter = new RestAdapter.Builder()
                .setClient(new OkClient(client))
                .setEndpoint("https://dev.geounderground.com")
                .setRequestInterceptor(requestInterceptor)
                .build();

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

        RequestInterceptor requestInterceptor = new RequestInterceptor() {
            @Override
            public void intercept(RequestInterceptor.RequestFacade request) {
                request.addHeader("Authorization", "WebToken " + geoAuthToken);
            }
        };

        restAdapter = new RestAdapter.Builder()
                .setClient(new OkClient(client))
                .setEndpoint("https://dev.geounderground.com")
                .setRequestInterceptor(requestInterceptor)
                .build();
    }

    public static String getAuthToken() {
        return geoAuthToken;
    }

    public static RestAdapter getRestAdapter() { return restAdapter; }

    class TokenInterceptor implements Interceptor {
        @Override public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            Response response = chain.proceed(request);

            String newToken = response.header("X-WebToken");
            if (newToken != null) {
                geoAuthToken = newToken;
            }

            return response;
        }
    }
}
