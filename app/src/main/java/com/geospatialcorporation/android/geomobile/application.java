package com.geospatialcorporation.android.geomobile;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.StrictMode;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;

import com.facebook.stetho.Stetho;
import com.facebook.stetho.okhttp.StethoInterceptor;
import com.geospatialcorporation.android.geomobile.library.DI.Analytics.Interfaces.IGeoAnalytics;
import com.geospatialcorporation.android.geomobile.library.DI.Analytics.Models.GoogleAnalyticEvent;
import com.geospatialcorporation.android.geomobile.library.DI.Map.Implementations.LayerManager;
import com.geospatialcorporation.android.geomobile.library.DI.SharedPreferences.Interfaces.IGeoSharedPrefs;
import com.geospatialcorporation.android.geomobile.library.DI.UIHelpers.Interfaces.IMapStatusBarManager;
import com.geospatialcorporation.android.geomobile.library.constants.Domains;
import com.geospatialcorporation.android.geomobile.library.constants.GeoPanel;
import com.geospatialcorporation.android.geomobile.library.constants.GeoSharedPreferences;
import com.geospatialcorporation.android.geomobile.library.DI.Map.Interfaces.ILayerManager;
import com.geospatialcorporation.android.geomobile.models.Bookmarks.Bookmark;
import com.geospatialcorporation.android.geomobile.models.Layers.LegendLayer;
import com.geospatialcorporation.android.geomobile.models.Subscription;
import com.geospatialcorporation.android.geomobile.models.Document.Document;
import com.geospatialcorporation.android.geomobile.models.Folders.Folder;
import com.geospatialcorporation.android.geomobile.models.Layers.Layer;
import com.geospatialcorporation.android.geomobile.models.MapLayerState;
import com.geospatialcorporation.android.geomobile.models.UserAccount;
import com.geospatialcorporation.android.geomobile.ui.Interfaces.IFeatureWindowCtrl;
import com.geospatialcorporation.android.geomobile.ui.Interfaces.IGeoMainActivity;
import com.geospatialcorporation.android.geomobile.ui.MainActivity;
import com.geospatialcorporation.android.geomobile.library.map.Models.GeoClusterMarker;
import com.geospatialcorporation.android.geomobile.ui.MainTabletActivity;
import com.geospatialcorporation.android.geomobile.ui.fragments.GoogleMapFragment;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.GoogleMap;
import com.google.maps.android.clustering.ClusterManager;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.client.OkClient;

public class application extends applicationDIBase {
    //region Properties
    private final static String TAG = "application";
    private final static String prefsName = "AppState";
    private static final String geoAuthTokenName = "geoAuthToken";
    //private static GoogleMapFragment googleMap;
    private static Locale locale;
    private static SharedPreferences appState;
    private static String domain;
    private static Context context;
    private static GoogleApiClient googleClient;
    private static Subscription geoSubscription;
    private static String geoAuthToken;
    private static String googleAuthToken;
    private static OkClient serviceClient;
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

    private static GoogleMapFragment mGoogleMapFragment;
    private static ClusterManager<GeoClusterMarker> clusterManager;

    private static ILayerManager layerManager;
    private static SlidingUpPanelLayout libraryFragmentPanel;
    private static SlidingUpPanelLayout layerFragmentPanel;

    private static int FeatureWindowDocument_LayerId;
    private static String FeatureWindowDocument_FeatureId;
    private static SlidingUpPanelLayout layerDetailFragmentPanel;
    private static SlidingUpPanelLayout documentFolderFragmentPanel;
    private static SlidingUpPanelLayout documentDetailFragmentPanel;
    private static IMapStatusBarManager statusBarManager;
    private static String azureDomain;
    private static UserAccount userAccount;
    private static boolean isTablet;
    private static MainTabletActivity mainTabletActivity;
    private static IGeoMainActivity geoMainActivity;
    private static IFeatureWindowCtrl featureWindowCtrl;
    private static int currentFeatureWindowTab;
    private static List<Folder> layerFolders;
    private static List<LegendLayer> legendLayerQueue;
    private static boolean shouldSetAppState;
    private static boolean mapStateLoaded;

    private Thread.UncaughtExceptionHandler androidDefaultUEH;
    //endregion

    private Thread.UncaughtExceptionHandler handler = new Thread.UncaughtExceptionHandler() {
         public void uncaughtException(Thread thread, Throwable ex) {
                 Log.e("TestApplication", "Uncaught exception is: ", ex);
                 // log it & phone home.
                 androidDefaultUEH.uncaughtException(thread, ex);
             }
    };
    private static boolean editingLayerMode;
    private static Integer clientCode;
    private static Boolean isLandscape;


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
        if(application.layerHashMap == null || application.layerHashMap.isEmpty()) {
            application.layerHashMap = layerHashMap;
        } else {
            for(Integer key : layerHashMap.keySet()){
                if(application.layerHashMap.get(key) == null){
                    application.layerHashMap.put(key, layerHashMap.get(key));
                }
            }
        }
    }
    //endregion

    public static MapLayerState getMapLayerState() {
        return mapLayerState;
    }

    public static void setMapLayerState(MapLayerState mapLayerState) {
        application.mapLayerState = mapLayerState;
    }

    public static void setIsAdminUser(Boolean isAdminUser) {
        IGeoSharedPrefs geoSharedPrefs = getGeoSharedPrefsComponent().provideGeoSharedPrefs();

        geoSharedPrefs.add("IsAdminUser", isAdminUser);
        geoSharedPrefs.apply();
    }

    public static Boolean getIsAdminUser(){
        IGeoSharedPrefs geoSharedPrefs = getGeoSharedPrefsComponent().provideGeoSharedPrefs();

        return geoSharedPrefs.getBoolean("IsAdminUser", false);
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

    public static void setGeoAuthToken(String geoAuthToken) {
        application.geoAuthToken = geoAuthToken;
    }

    public static void setClusterManager(ClusterManager<GeoClusterMarker> clusterManager) {
        application.clusterManager = clusterManager;
    }

    public static ClusterManager<GeoClusterMarker> getClusterManager() {
        return clusterManager;
    }

    public static ILayerManager getLayerManager() {
        return layerManager;
    }

    public static void setLibraryFragmentPanel(SlidingUpPanelLayout libraryFragmentPanel) {
        application.libraryFragmentPanel = libraryFragmentPanel;
    }

    public static void setLayerFragmentPanel(SlidingUpPanelLayout layerFragmentPanel) {
        application.layerFragmentPanel = layerFragmentPanel;
    }

    public static void setLayerDetailFragmentPanel(SlidingUpPanelLayout layerDetailFragmentPanel) {
        application.layerDetailFragmentPanel = layerDetailFragmentPanel;
    }

    public static void setDocumentFolderFragmentPanel(SlidingUpPanelLayout documentFolderFragmentPanel) {
        application.documentFolderFragmentPanel = documentFolderFragmentPanel;
    }

    public static void setDocumentDetailFragmentPanel(SlidingUpPanelLayout documentDetailFragmentPanel) {
        application.documentDetailFragmentPanel = documentDetailFragmentPanel;
    }

    public static IMapStatusBarManager getStatusBarManager() {
        if(statusBarManager == null){
            statusBarManager = application.getUIHelperComponent().provideMapStatusBarManager();
        }

        return statusBarManager;
    }

    public static String getAzureDomain() {
        return azureDomain;
    }

    public static UserAccount getUserAccount() {
        return userAccount;
    }

    public static void setUserAccount(UserAccount userAccount) {
        application.userAccount = userAccount;
    }

    public static boolean getIsTablet() {
        return isTablet;
    }

    public static void setIsTablet(boolean isTablet) {
        application.isTablet = isTablet;
    }

    public static MainTabletActivity getMainTabletActivity() {
        return mainTabletActivity;
    }

    public static void setGeoMainActivity(IGeoMainActivity geoMainActivity) {
        application.geoMainActivity = geoMainActivity;
    }

    public static IGeoMainActivity getGeoMainActivity() {
        return geoMainActivity;
    }

    public static IFeatureWindowCtrl getFeatureWindowCtrl() {
        return featureWindowCtrl;
    }

    public static void setFeatureWindowCtrl(IFeatureWindowCtrl featureWindowCtrl) {
        application.featureWindowCtrl = featureWindowCtrl;
    }

    public static void setCurrentFeatureWindowTab(int currentFeatureWindowTab) {
        application.currentFeatureWindowTab = currentFeatureWindowTab;
    }

    public static int getCurrentFeatureWindowTab() {
        return currentFeatureWindowTab;
    }

    public static void setLayerFolders(List<Folder> layerFolders) {
        application.layerFolders = layerFolders;
    }

    public static List<Folder> getLayerFolders() {
        return layerFolders;
    }

    public static List<LegendLayer> getLegendLayerQueue() {
        return legendLayerQueue;
    }

    public static void setShouldSetAppState(boolean shouldSetAppState) {
        application.shouldSetAppState = shouldSetAppState;
    }

    public static boolean getShouldSetAppState() {
        return shouldSetAppState;
    }

    public static void setMapStateLoaded(boolean mapStateLoaded) {
        application.mapStateLoaded = mapStateLoaded;
    }

    public static boolean getMapStateLoaded() {
        return mapStateLoaded;
    }

    public static void setLegendLayerQueue(List<LegendLayer> legendLayerQueue) {
        application.legendLayerQueue = legendLayerQueue;
    }

    public static void setMainTabletActivity(MainTabletActivity mainTabletActivity) {
        application.mainTabletActivity = mainTabletActivity;
    }

    public static void setEditingLayerMode(boolean editingLayerMode) {
        application.editingLayerMode = editingLayerMode;
    }

    public static boolean getEditingLayerMode() {
        return editingLayerMode;
    }

    public static void setIsLandscape(Boolean isLandscape) {
        application.isLandscape = isLandscape;
    }

    public static Boolean getIsLandscape() {
        return isLandscape;
    }

    //endregion

    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onApplicationCreate");
        androidDefaultUEH = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(handler);

        context = getApplicationContext();
        appState = getSharedPreferences(prefsName, 0);
        locale = Locale.US;

        analytics = GoogleAnalytics.getInstance(this);
        analytics.setLocalDispatchPeriod(1800);

        tracker = analytics.newTracker("UA-49521639-6");
        tracker.enableExceptionReporting(true);
        tracker.enableAdvertisingIdCollection(true);
        tracker.enableAutoActivityTracking(true);

        if (BuildConfig.DEBUG) {
            domain = Domains.DEVELOPMENT;
            azureDomain = "https://geoeastusfilesdev01.blob.core.windows.net/icons/";

            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                    .detectDiskReads()
                    .detectDiskWrites()
                    .detectNetwork()   // or .detectAll() for all detectable problems
                    .penaltyLog()
                    .build());
            
            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                    .detectLeakedSqlLiteObjects()
                    .detectLeakedClosableObjects()
                    .penaltyLog()
                    .penaltyDeath()
                    .build());

        } else {
            // TODO: Change on release
            domain = Domains.QUALITY_ASSURANCE;
            azureDomain = "https://geoeastusfilesprod01.blob.core.windows.net/icons/";

        }

        Stetho.initialize(
                Stetho.newInitializerBuilder(this)
                        .enableDumpapp(
                                Stetho.defaultDumperPluginsProvider(this))
                        .enableWebKitInspector(
                                Stetho.defaultInspectorModulesProvider(this))
                        .build());

        RequestInterceptor requestInterceptor = setupInterceptor();

        OkHttpClient client = new OkHttpClient();
        client.networkInterceptors().add(new StethoInterceptor());
        client.interceptors().add(new TokenInterceptor());

        serviceClient = new OkClient(client);

        configRestAdapter(requestInterceptor);

        isAdminUser = false;
        editingLayerMode = false;

        mapLayerState = new MapLayerState();
        layerManager = new LayerManager();

        initializeApplication();
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

    public static String getDomain() { return domain; }

    protected static void configRestAdapter(RequestInterceptor requestInterceptor) {
        if (BuildConfig.DEBUG) {
            restAdapter = new RestAdapter.Builder()
                    .setLogLevel(RestAdapter.LogLevel.FULL)
                    .setClient(serviceClient)
                    .setEndpoint(domain)
                    .setRequestInterceptor(requestInterceptor)
                    .build();
        } else {
            restAdapter = new RestAdapter.Builder()
                    .setClient(serviceClient)
                    .setEndpoint(domain)
                    .setRequestInterceptor(requestInterceptor)
                    .build();
        }
    }

    protected static RequestInterceptor setupInterceptor() {
        return new RequestInterceptor() {
            @Override
            public void intercept(RequestInterceptor.RequestFacade request) {
                request.addHeader("User-Agent", "GeoUnderground-Mobile");

                if (geoAuthToken != null) {
                    request.addHeader("Authorization", "WebToken " + geoAuthToken);
                }
            }
        };
    }

    public static Locale getLocale() { return locale; }

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
            case GeoPanel.LAYER_FRAGMENT:
                panel = layerFragmentPanel;
                break;
            case GeoPanel.LIBRARY_FRAGMENT:
                panel = libraryFragmentPanel;
                break;
            case GeoPanel.LAYER_DETAIL:
                panel = layerDetailFragmentPanel;
                break;
            case GeoPanel.DOCUMENT_DETAIL:
                panel = documentDetailFragmentPanel;
                break;
            case GeoPanel.DOCUMENT_FOLDER_DETAIL:
                panel = documentFolderFragmentPanel;
                break;
            default:
                break;
        }

        return panel;
    }

    public static void setMapFragment(GoogleMapFragment googleMapFragment) {
        mGoogleMapFragment = googleMapFragment;
    }

    public static GoogleMapFragment getMapFragment() {
        if(mGoogleMapFragment == null){
            mGoogleMapFragment = new GoogleMapFragment();
        }

        return mGoogleMapFragment;
    }

    public static void setFeatureWindowDocumentIds(int layerId, String featureId) {
        FeatureWindowDocument_LayerId = layerId;
        FeatureWindowDocument_FeatureId = featureId;
    }

    public static int getFeatureWindowLayerId() {
        return FeatureWindowDocument_LayerId;
    }

    public static String getFeatureWindowFeatureId() {
        return FeatureWindowDocument_FeatureId;
    }

    public static boolean areLayerFoldersStored() {
        return layerFolders.size() > 0;
    }

    public static void addLegendLayerToQueue(LegendLayer legendLayer) {
        if(!containsLegendLayer(legendLayerQueue, legendLayer)) {
            legendLayerQueue.add(legendLayer);
        }
    }

    private static boolean containsLegendLayer(List<LegendLayer> legendLayerQueue, LegendLayer layerToCheck) {
        boolean result = false;

        for (LegendLayer layer : legendLayerQueue) {
            if(layer.getLayer() != null && layerToCheck.getLayer() != null){
                if(layer.getLayer().getId() == layerToCheck.getLayer().getId()){
                    result = true;
                    break;
                }
            }
        }

        return result;
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
            IGeoSharedPrefs prefs = getGeoSharedPrefsComponent().provideGeoSharedPrefs();
            prefs.add(geoAuthTokenName, token);
            prefs.apply();
        }
    }

    public static String getGoogleAuthToken() {
        return googleAuthToken;
    }

    public static void setGoogleAuthToken(String token) {
        googleAuthToken = token;
    }

    public static Subscription getGeoSubscription() {
        return geoSubscription;
    }

    public static void setGeoSubscription(Subscription subscription) {
        geoSubscription = subscription;
    }

    public static SharedPreferences getAppState() {
        return appState;
    }

    public static void Logout() {
        geoAuthToken = null;
        googleAuthToken = null;
        clearUserSpecificData();

        getStatusBarManager().reset();

        IGeoSharedPrefs prefs = getGeoSharedPrefsComponent().provideGeoSharedPrefs();
        prefs.remove(GeoSharedPreferences.GOOGLE_ACCOUNT);
        prefs.commit();

        IGeoAnalytics analytics = getAnalyticsComponent().provideGeoAnalytics();
        analytics.trackClick(new GoogleAnalyticEvent().Logout());

        //appState.getStringSet(geoAuthTokenName, null);
        prefs.remove(geoAuthTokenName);
        prefs.commit();

        initializeApplication();
    }

    public static void clearUserSpecificData(){
        folderHashMap = new HashMap<>();
        layerHashMap = new HashMap<>();
        documentHashMap = new HashMap<>();
        userAccount = null;
        layerFolders = new ArrayList<>();
        legendLayerQueue = new ArrayList<>();
    }

    private static void initializeApplication() {
        folderHashMap = new HashMap<>();
        layerHashMap = new HashMap<>();
        documentHashMap = new HashMap<>();
        mGoogleMapFragment = new GoogleMapFragment();
        //geoAuthToken = appState.getString(geoAuthTokenName, null);
        geoAuthToken = null;
        layerFolders = new ArrayList<>();
        legendLayerQueue = new ArrayList<>();

        IGeoSharedPrefs prefs = getGeoSharedPrefsComponent().provideGeoSharedPrefs();
        prefs.remove(geoAuthTokenName);
        prefs.commit();
    }

}
