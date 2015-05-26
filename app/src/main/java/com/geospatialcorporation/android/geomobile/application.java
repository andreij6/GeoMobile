package com.geospatialcorporation.android.geomobile;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.facebook.stetho.Stetho;
import com.facebook.stetho.okhttp.StethoInterceptor;
import com.geospatialcorporation.android.geomobile.library.constants.Domains;
import com.geospatialcorporation.android.geomobile.models.Client;
import com.geospatialcorporation.android.geomobile.models.Folders.Folder;
import com.geospatialcorporation.android.geomobile.models.Layers.Layer;
import com.geospatialcorporation.android.geomobile.models.Library.Document;
import com.google.android.gms.common.api.GoogleApiClient;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.client.OkClient;

public class application extends Application {
    private final static String TAG = "application";
    private final static String prefsName = "AppState";
    private static SharedPreferences appState;
    private static String domain;
    private static Context context;
    private static GoogleApiClient googleClient;
    private static Client geoClient;
    private static String geoAuthToken;
    private static String googleAuthToken;
    private static OkHttpClient client;
    private static RestAdapter restAdapter;
    private static List<Folder> libraryFolders;
    private static HashMap<Integer, Folder> folderHashMap;
    private static List<Folder> layerFolders;
    private static List<Layer> Layers;
    private static HashMap<Integer, Layer> layerHashMap;
    private static HashMap<Integer, Document> documentsHashMap;
    private static List<Document> documents;
    private static List<Folder> documentFolders;

    public void onCreate() {
        super.onCreate();
        application.context = getApplicationContext();
        folderHashMap = new HashMap<>();
        layerHashMap = new HashMap<>();
        documentsHashMap = new HashMap<>();

        appState = getSharedPreferences(prefsName, 0);

        if (BuildConfig.DEBUG) {
            domain = Domains.DEVELOPMENT;
        } else {
            // TODO: Change on release
            domain = Domains.QUALITY_ASSURANCE;
        }

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

                if (geoAuthToken != null) {
                    request.addHeader("Authorization", "WebToken " + geoAuthToken);
                }
            }
        };

        client = new OkHttpClient();
        client.networkInterceptors().add(new StethoInterceptor());
        client.interceptors().add(new TokenInterceptor());

        if (BuildConfig.DEBUG) {
            restAdapter = new RestAdapter.Builder()
                    .setLogLevel(RestAdapter.LogLevel.FULL)
                    .setClient(new OkClient(client))
                    .setEndpoint(domain)
                    .setRequestInterceptor(requestInterceptor)
                    .build();
        } else {
            restAdapter = new RestAdapter.Builder()
                    .setClient(new OkClient(client))
                    .setEndpoint(domain)
                    .setRequestInterceptor(requestInterceptor)
                    .build();
        }


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

        if (BuildConfig.DEBUG) {
            restAdapter = new RestAdapter.Builder()
                    .setLogLevel(RestAdapter.LogLevel.FULL)
                    .setClient(new OkClient(client))
                    .setEndpoint(domain)
                    .setRequestInterceptor(requestInterceptor)
                    .build();
        } else {
            restAdapter = new RestAdapter.Builder()
                    .setClient(new OkClient(client))
                    .setEndpoint(domain)
                    .setRequestInterceptor(requestInterceptor)
                    .build();
        }

        Log.d(TAG, "Set GeoToken to: " + token);
    }

    public static String getAuthToken() {
        return geoAuthToken;
    }

    public static void setLibraryFolders(List<Folder> folders) {
        libraryFolders = folders;
    }

    public static List<Folder> getLibraryFolders() {
        return libraryFolders;
    }

    public static void setLayerFolders(List<Folder> folders) {
        layerFolders = folders;

        for (Folder folder : layerFolders) {
            folderHashMap.put(folder.getId(), folder);
        }
    }

    public static void setDocuments(List<Document> documents) {
        application.documents = documents;

        for (Document document : application.documents) {
            documentsHashMap.put(document.getId(), document);
        }
    }

    public static Document getDocumentById(int documentId) {
        return documentsHashMap.get(documentId);
    }

    public static List<Document> getDocuments() {
        return documents;
    }

    public static void setDocumentFolders(List<Folder> documentFolders) {
        application.documentFolders = documentFolders;

        for (Folder folder : documentFolders) {
            folderHashMap.put(folder.getId(), folder);
        }
    }

    public static List<Folder> getDocumentFolders() {
        return documentFolders;
    }

    public static List<Folder> getLayerFolders() {
        return layerFolders;
    }

    public static Folder getFolderById(int folderId) {
        return folderHashMap.get(folderId);
    }

    public static void setLayers(List<Layer> layers) {
        Layers = layers;
        for (Layer layer : layers) {
            layerHashMap.put(layer.getId(), layer);
        }
    }

    public static List<Layer> getLayers() {
        return Layers;
    }

    public static RestAdapter getRestAdapter() {
        return restAdapter;
    }

    public static Layer getLayer(int id) {
        return layerHashMap.get(id);
    }

    class TokenInterceptor implements Interceptor {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            Response response = chain.proceed(request);

            String newToken = response.header("X-WebToken");
            if (newToken != null) {
                geoAuthToken = newToken;
                Log.d(TAG, "Set GeoToken to: " + newToken);
            }

            return response;
        }
    }

    public static String getGoogleAuthToken() {
        return googleAuthToken;
    }

    public static void setGoogleAuthToken(String token) {
        googleAuthToken = token;
    }

    public static Client getGeoClient() {
        return geoClient;
    }

    public static void setGeoClient(Client client) {
        geoClient = client;
    }

    public static SharedPreferences getAppState() {
        return appState;
    }
}
