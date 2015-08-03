package com.geospatialcorporation.android.geomobile;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;

import com.facebook.stetho.Stetho;
import com.facebook.stetho.okhttp.StethoInterceptor;
import com.geospatialcorporation.android.geomobile.library.DI.Analytics.Interfaces.IGeoAnalytics;
import com.geospatialcorporation.android.geomobile.library.DI.Analytics.Models.GoogleAnalyticEvent;
import com.geospatialcorporation.android.geomobile.library.DI.SharedPreferences.IGeoSharedPrefs;
import com.geospatialcorporation.android.geomobile.library.DI.SharedPreferences.Implementations.GeoSharedPrefs;
import com.geospatialcorporation.android.geomobile.library.constants.Domains;
import com.geospatialcorporation.android.geomobile.library.constants.GeoPanel;
import com.geospatialcorporation.android.geomobile.library.map.layerManager.ILayerManager;
import com.geospatialcorporation.android.geomobile.library.map.layerManager.LayerManager;
import com.geospatialcorporation.android.geomobile.models.Bookmarks.Bookmark;
import com.geospatialcorporation.android.geomobile.models.Client;
import com.geospatialcorporation.android.geomobile.models.Folders.Folder;
import com.geospatialcorporation.android.geomobile.models.Layers.Layer;
import com.geospatialcorporation.android.geomobile.models.Document.Document;
import com.geospatialcorporation.android.geomobile.models.MapLayerState;
import com.geospatialcorporation.android.geomobile.ui.MainActivity;
import com.geospatialcorporation.android.geomobile.ui.fragments.GoogleMapFragment;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.GoogleMap;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.HashMap;

import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.client.OkClient;

public class application extends applicationDIBase {
    //region Properties
    private final static String TAG = "application";
    private final static String prefsName = "AppState";
    private static final String geoAuthTokenName = "geoAuthToken";
    //private static GoogleMapFragment googleMap;
    private static SharedPreferences appState;
    private static String domain;
    private static Context context;
    private static GoogleApiClient googleClient;
    private static Client geoClient;
    private static String geoAuthToken;
    private static String googleAuthToken;
    private static OkHttpClient client;
    private static RestAdapter restAdapter;
    private static boolean isAdminUser;
    public static Uri mMediaUri;

    private static HashMap<Integer, Folder> folderHashMap;
    private static HashMap<Integer, Document> documentHashMap;
    private static HashMap<Integer, Layer> layerHashMap;
    private static HashMap<Integer, Bookmark> bookmarkHashMap;
    private static DrawerLayout layerDrawer;
    private static SlidingUpPanelLayout mapFragmentPanel;
    private static SlidingUpPanelLayout sublayerFragmentPanel;
    private static SlidingUpPanelLayout layerAttributePanel;
    private static MainActivity mainActivity;
    private static MapLayerState mapLayerState;

    private static GoogleAnalytics analytics;

    private static Tracker tracker;
    private static GoogleMap googleMap;

    private static LayerManager layerManager;
    //endregion

    //region stuff
    public static GoogleAnalytics analytics() {
        return analytics;
    }

    public static Tracker tracker() {
        return tracker;
    }

    //region Tree Entity Getters & Setters
    public static HashMap<Integer, Folder> getFolderHashMap() {
        return folderHashMap;
    }

    public static void setFolderHashMap(HashMap<Integer, Folder> folderHashMap) {
        application.folderHashMap = folderHashMap;
    }

    public static HashMap<Integer, Document> getDocumentHashMap() {
        return documentHashMap;
    }

    public static void setDocumentHashMap(HashMap<Integer, Document> documentHashMap) {
        application.documentHashMap = documentHashMap;
    }

    public static HashMap<Integer, Layer> getLayerHashMap() {
        return layerHashMap;
    }

    public static void setLayerHashMap(HashMap<Integer, Layer> layerHashMap) {
        application.layerHashMap = layerHashMap;
    }
    //endregion

    public static MapLayerState getMapLayerState() {
        return mapLayerState;
    }

    public static void setMapLayerState(MapLayerState mapLayerState) {
        application.mapLayerState = mapLayerState;
    }

    public static void setIsAdminUser(Boolean isAdminUser) {
        application.isAdminUser = isAdminUser;
    }

    public static Boolean getIsAdminUser(){
        return isAdminUser;
    }

    public static HashMap<Integer, Bookmark> getBookmarkHashMap() {
        if(bookmarkHashMap == null){
            bookmarkHashMap = new HashMap<>();
        }
        return bookmarkHashMap;
    }

    public static void setLayerDrawer(DrawerLayout layerDrawer) {
        application.layerDrawer = layerDrawer;
    }

    public static DrawerLayout getLayerDrawer() {
        return layerDrawer;
    }

    public static SlidingUpPanelLayout getMapFragmentPanel() {
        return mapFragmentPanel;
    }

    public static void setMapFragmentPanel(SlidingUpPanelLayout mapFragmentPanel) {
        application.mapFragmentPanel = mapFragmentPanel;
    }

    public static void setSublayerFragmentPanel(SlidingUpPanelLayout sublayerFragmentPanel){
        application.sublayerFragmentPanel = sublayerFragmentPanel;
    }

    public static void setLayerAttributePanel(SlidingUpPanelLayout layerAttributePanel) {
        application.layerAttributePanel = layerAttributePanel;
    }

    public static MainActivity getMainActivity() {
        return mainActivity;
    }

    public static void setMainActivity(MainActivity mainActivity) {
        application.mainActivity = mainActivity;
    }

    public static void setGoogleMap(GoogleMap googleMap) {
        application.googleMap = googleMap;
    }

    public static GoogleMap getGoogleMap() {
        return googleMap;
    }

    public static ILayerManager getLayerManager() {
        return layerManager;
    }

    public static void setGeoAuthToken(String geoAuthToken) {
        application.geoAuthToken = geoAuthToken;
    }
    //endregion

    public void onCreate() {
        super.onCreate();

        context = getApplicationContext();
        appState = getSharedPreferences(prefsName, 0);

        analytics = GoogleAnalytics.getInstance(this);
        analytics.setLocalDispatchPeriod(1800);

        tracker = analytics.newTracker("UA-49521639-6");
        tracker.enableExceptionReporting(true);
        tracker.enableAdvertisingIdCollection(true);
        tracker.enableAutoActivityTracking(true);

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

        mapLayerState = new MapLayerState();
        layerManager = new LayerManager();

        initializeApplication();
    }

    public static GoogleMapFragment getMapFragment() {
        return new GoogleMapFragment(); // returning the googlemap fragment from it init didnt work after the views were disposed
        //return googleMap;
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

    public static RestAdapter getRestAdapter() {
        return restAdapter;
    }

    public static SlidingUpPanelLayout getSlidingPanel(int panelType) {
        SlidingUpPanelLayout panel = null;

        switch (panelType){
            case GeoPanel.MAP:
                panel = mapFragmentPanel;
                break;
            case GeoPanel.SUBLAYER:
                panel = sublayerFragmentPanel;
                break;
            case GeoPanel.LAYER_ATTRIBUTE:
                panel = layerAttributePanel;
                break;
            default:
                break;
        }

        return panel;
    }

    public static void setMapFragment() {

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
                addTokenToSharedPreferences(newToken);
            }

            return response;
        }

        private void addTokenToSharedPreferences(String token) {
            Context context = application.this;
            SharedPreferences preferences = context.getSharedPreferences("geoAuthToken", Context.MODE_PRIVATE);

            SharedPreferences.Editor editor = preferences.edit();

            editor.putString("GeoUndergroundAuthToken", token);

            editor.apply();
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
        layerHashMap = null;
        documentHashMap = null;
        folderHashMap = null;

        IGeoSharedPrefs prefs = getGeoSharedPrefsComponent().provideGeoSharedPrefs();
        prefs.remove(prefs.getGoogleAccountName());

        IGeoAnalytics analytics = getAnalyticsComponent().provideGeoAnalytics();
        analytics.trackClick(new GoogleAnalyticEvent().Logout());

        appState.getStringSet(geoAuthTokenName, null);

        initializeApplication();
    }

    private static void initializeApplication() {
        folderHashMap = new HashMap<>();
        layerHashMap = new HashMap<>();
        documentHashMap = new HashMap<>();
        //googleMap = new GoogleMapFragment();
        geoAuthToken = appState.getString(geoAuthTokenName, null);
    }

}
