package com.geospatialcorporation.android.geomobile;

import android.app.Application;
import android.app.DownloadManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.util.Log;

import com.facebook.stetho.Stetho;
import com.facebook.stetho.okhttp.StethoInterceptor;
import com.geospatialcorporation.android.geomobile.library.constants.Domains;
import com.geospatialcorporation.android.geomobile.models.Client;
import com.geospatialcorporation.android.geomobile.models.Folders.Folder;
import com.geospatialcorporation.android.geomobile.models.Layers.Layer;
import com.geospatialcorporation.android.geomobile.models.Library.Document;
import com.geospatialcorporation.android.geomobile.ui.fragments.GoogleMapFragment;
import com.google.android.gms.common.api.GoogleApiClient;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.client.OkClient;

public class application extends Application {
    private final static String TAG = "application";
    private final static String prefsName = "AppState";
    private static final String geoAuthTokenName = "geoAuthToken";
    private static GoogleMapFragment googleMap;
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
    private static List<Layer> layers;
    private static HashMap<Integer, Layer> layerHashMap;
    private static HashMap<Integer, Document> documentHashMap;
    private static List<Document> documents;
    private static List<Folder> documentFolders;
    private static boolean isAdminUser;
    public static Uri mMediaUri;

    public static void setIsAdminUser(boolean isAdminUser) {
        application.isAdminUser = isAdminUser;
    }

    public static Boolean getIsAdminUser(){
        return isAdminUser;
    }

    public static void setFolders(List<Folder> folders) {
        for(Folder f : folders){
            if(!folderHashMap.containsKey(f.getId())) {
                folderHashMap.put(f.getId(), f);
            }
        }
    }

    public void onCreate() {
        super.onCreate();

        context = getApplicationContext();
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
                request.addHeader("User-Agent", "GeoUnderground-Mobile");

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

        isAdminUser = false;

        initializeApplication();
    }

    public static GoogleMapFragment getMapFragment() {
        return googleMap;
    }

    public static Context getAppContext() {
        return context;
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
        appState.getString(geoAuthTokenName, token);
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
        Log.d(TAG, "setLayerFolders total: " + folders.size());
        layerFolders = folders;

        for (Folder folder : layerFolders) {
            folderHashMap.put(folder.getId(), folder);
        }
    }

    public static void setDocuments(List<Document> newDocuments) {
        documents = newDocuments;

        for (Document document : documents) {
            documentHashMap.put(document.getId(), document);
        }
    }

    public static Document getDocumentById(int documentId) {
        return documentHashMap.get(documentId);
    }

    public static List<Document> getDocuments() {
        return documents;
    }

    public static void setDocumentFolders(List<Folder> newDocumentFolders) {
        documentFolders = newDocumentFolders;

        for (Folder folder : newDocumentFolders) {
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

    public static void setLayers(List<Layer> newLayers) {

        for (Layer layer : newLayers) {
            if(!layerHashMap.containsKey(layer.getId())) {
                layerHashMap.put(layer.getId(), layer);
            }
        }
    }


    public static RestAdapter getRestAdapter() {
        return restAdapter;
    }

    public static Layer getLayer(int id) {
        return layerHashMap.get(id);
    }

    public static void addFolder(Folder folder) {
        folderHashMap.put(folder.getId(), folder);
    }

    public static void removeFolder(Integer id) {
        folderHashMap.remove(id); //TODO: May need to remove subfolders
    }

    public static void removeLayer(Integer id) {
        layerHashMap.remove(id);
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

    public static void Logout() {
        geoAuthToken = null;
        googleAuthToken = null;
        libraryFolders = null;
        layerFolders = null;
        layers = null;
        layerHashMap = null;
        documents = null;
        documentHashMap = null;
        documentFolders = null;

        appState.getStringSet(geoAuthTokenName, null);

        initializeApplication();
    }

    private static void initializeApplication() {
        folderHashMap = new HashMap<>();
        layerHashMap = new HashMap<>();
        documentHashMap = new HashMap<>();
        googleMap = new GoogleMapFragment();

        geoAuthToken = appState.getString(geoAuthTokenName, null);
    }
}
