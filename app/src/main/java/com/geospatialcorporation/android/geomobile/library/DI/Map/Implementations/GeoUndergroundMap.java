package com.geospatialcorporation.android.geomobile.library.DI.Map.Implementations;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.graphics.Point;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.provider.Settings;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.geospatialcorporation.android.geomobile.R;
import com.geospatialcorporation.android.geomobile.application;
import com.geospatialcorporation.android.geomobile.library.DI.Analytics.Interfaces.IGeoAnalytics;
import com.geospatialcorporation.android.geomobile.library.DI.Analytics.Models.GoogleAnalyticEvent;
import com.geospatialcorporation.android.geomobile.library.DI.Map.Interfaces.IGeoUndergroundMap;
import com.geospatialcorporation.android.geomobile.library.DI.Map.Interfaces.ILayerManager;
import com.geospatialcorporation.android.geomobile.library.DI.Map.Interfaces.IMapStateService;
import com.geospatialcorporation.android.geomobile.library.DI.Map.Models.MapStateSaveRequest;
import com.geospatialcorporation.android.geomobile.library.DI.Tasks.Implementations.LayerStyleTask;
import com.geospatialcorporation.android.geomobile.library.DI.Tasks.Interfaces.IGetLayersTask;
import com.geospatialcorporation.android.geomobile.library.DI.Tasks.Interfaces.ILayerStyleTask;
import com.geospatialcorporation.android.geomobile.library.DI.Tasks.models.GetLayersTaskParams;
import com.geospatialcorporation.android.geomobile.library.DI.UIHelpers.Interfaces.IMapStatusBarManager;
import com.geospatialcorporation.android.geomobile.library.constants.GeometryTypeCodes;
import com.geospatialcorporation.android.geomobile.library.helpers.ProgressDialogHelper;
import com.geospatialcorporation.android.geomobile.library.services.QueryRestService;
import com.geospatialcorporation.android.geomobile.models.Folders.Folder;
import com.geospatialcorporation.android.geomobile.models.Layers.Layer;
import com.geospatialcorporation.android.geomobile.models.Layers.LegendLayer;
import com.geospatialcorporation.android.geomobile.models.Query.map.response.featurewindow.ParcelableFeatureQueryResponse;
import com.geospatialcorporation.android.geomobile.ui.Interfaces.IPostExecuter;
import com.geospatialcorporation.android.geomobile.ui.MainTabletActivity;
import com.geospatialcorporation.android.geomobile.ui.fragments.MapFragments.TabletMapFragment;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.PolyUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GeoUndergroundMap implements IGeoUndergroundMap, IPostExecuter<List<Folder>> {
    private static final String TAG = GeoUndergroundMap.class.getSimpleName();
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;

    GoogleMap mMap;
    MapView mMapView;
    UiSettings mUiSettings;
    ILayerManager mLayerManager;
    IMapStateService mMapStateService;
    IGetLayersTask mGetLayersTask;
    IMapStatusBarManager mStatusBarManager;
    Marker mCurrentLocationMaker;

    Context mContext;
    GoogleApiClient mLocationClient;
    Activity mActivity;
    ProgressDialogHelper mProgressDialogHelper;

    Polygon mHighlightedPolygon;
    Polyline mHighlightedPolyline;
    Marker mHighlightedMarker;

    IGeoAnalytics mAnalytics;
    QueryRestService mQueryService;

    String mSelectedFeatureId;
    int mSelectedLayerId;

    public GeoUndergroundMap(ILayerManager layerManager, IMapStateService mapStateService) {
        mLayerManager = layerManager;
        mMapStateService = mapStateService;
        mAnalytics = application.getAnalyticsComponent().provideGeoAnalytics();
        mQueryService = new QueryRestService();
        mGetLayersTask = application.getTasksComponent().provideLayersTask();
        mStatusBarManager = application.getStatusBarManager();
    }

    @Override
    public void initializeMap(Bundle savedInstanceState, MapView mapView, final TabletMapFragment mapFragment) {
        mMapView = mapView;

        mMapView.onCreate(savedInstanceState);

        if(application.getIsTablet()) {
            mProgressDialogHelper = new ProgressDialogHelper(mContext);

            mProgressDialogHelper.showProgressDialog();
        }

        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                mMap = googleMap;

                mapFragment.setMap(mMap);
                mMap.setMyLocationEnabled(true);

                mUiSettings = mMap.getUiSettings();
                mUiSettings.setMyLocationButtonEnabled(false);

                application.setGoogleMap(mMap);

                mLayerManager.setMap(mMap);

                mMap.setOnMapLoadedCallback(GeoUndergroundMap.this);

                mLocationClient = new GoogleApiClient.Builder(mContext)
                        .addApi(LocationServices.API)
                        .addConnectionCallbacks(GeoUndergroundMap.this)
                        .addOnConnectionFailedListener(GeoUndergroundMap.this)
                        .build();
                try {

                    MapsInitializer.initialize(mContext);

                    mLocationClient.connect();

                } catch (Exception e) {
                    e.printStackTrace();

                    mAnalytics.sendException(e);
                }

                setMapState();
            }
        });
    }

    @Override
    public void setup(Activity activity) {
        mContext = activity;
        mActivity = activity;
    }

    @Override
    public void getLocationBtn(ImageButton gpsbtn) {

        gpsbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Location currentLocation = LocationServices.FusedLocationApi.getLastLocation(mLocationClient);

                mAnalytics.trackClick(new GoogleAnalyticEvent().CurrentLocation());

                if (currentLocation == null) {
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity());
                    builder1.setMessage(mActivity.getString(R.string.location_not_found))
                            .setPositiveButton(mActivity.getString(R.string.enable_gps),
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            Intent gpsOptions = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);

                                            mActivity.startActivity(gpsOptions);
                                        }
                                    })
                            .setNegativeButton(mActivity.getString(R.string.cancel),
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.cancel();
                                        }
                                    });

                    AlertDialog alert11 = builder1.create();
                    alert11.show();
                } else {
                    handleNewLocation(currentLocation);
                }
            }
        });

    }

    @Override
    public void onMapLoaded() {
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                clearHighlights();

                try {
                    getFeatureWindow(marker.getId(), LayerManager.POINT);

                    highlight(marker);

                } catch (Exception e) {
                    mStatusBarManager.reset();

                    mAnalytics.sendException(e);
                }

                return true;

            }

        });

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {

                clearHighlights();

                Boolean highlightFound;

                highlightFound = highlightLine(latLng);

                if (!highlightFound) {
                    highlightPolygon(latLng);
                }
            }
        });

        //endregion

        mMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {

                mLayerManager.showLayers(mMap);
            }
        });

        if(application.getIsTablet()) {
            mProgressDialogHelper.showProgressDialog();
            preloadLayers();
        }

        mLayerManager.showLayers(mMap);

    }

    protected void preloadLayers() {
        mGetLayersTask.getAll(new GetLayersTaskParams(this));
    }

    @Override
    public void clearHighlights() {
        if(mHighlightedPolyline != null) {
            mHighlightedPolyline.remove();
            mHighlightedPolyline = null;
        }
        if(mHighlightedPolygon != null){
            mHighlightedPolygon.remove();
            mHighlightedPolygon = null;
        }
        if(mHighlightedMarker != null) {
            mHighlightedMarker.remove();
            mHighlightedMarker = null;
        }
    }

    @Override
    public boolean validate(ParcelableFeatureQueryResponse response) {
        boolean result = false;
        try {
            result = response.getFeatureQueryResponse().get(0).getFeatures().size() != 0;
        } catch (Exception exception){
            result = false;

            mAnalytics.sendException(exception);

        } finally {
            return result;
        }
    }

    @Override
    public void getNextFeature() {
        int typeCode = getTypeCode();

        LatLng center = getHighlightCenter();

        if(typeCode != 0){
            mLayerManager.getNextFeature(mSelectedFeatureId, mSelectedLayerId, typeCode, true, center);
        }
    }

    @Override
    public void getPreviousFeature() {
        int typeCode = getTypeCode();

        LatLng center = getHighlightCenter();

        if(typeCode != 0){
            mLayerManager.getNextFeature(mSelectedFeatureId, mSelectedLayerId, typeCode, false, center);
        }
    }

    @Override
    public int getSelectedLayerId() {
        return mSelectedLayerId;
    }


    @Override
    public void rezoomToHighlighted() {
        if(mHighlightedMarker != null){
            zoomToFeature(mHighlightedMarker);
        }

        if(mHighlightedPolygon != null){
            polyZoomToFeature(mHighlightedPolygon.getPoints());
        }

        if(mHighlightedPolyline != null){
            polyZoomToFeature(mHighlightedPolyline.getPoints());
        }
    }

    @Override
    public void simulateClick(final LatLng latLng) {
        if(latLng == null){
            Toast.makeText(mContext, "No Next Feature", Toast.LENGTH_SHORT).show();
            return;
        }

        float zoom = 15f;

        double lat = 0;

        if(latLng.latitude > 0){
            lat = latLng.latitude - .005;
        } else {
            lat = latLng.latitude + .005;
        }

        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(new LatLng(lat, latLng.longitude), zoom);

        mMap.animateCamera(update);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Point point = mMap.getProjection().toScreenLocation(latLng);

                long touchTime = SystemClock.uptimeMillis();

                MotionEvent simulationEvent = MotionEvent.obtain(touchTime, touchTime,
                        MotionEvent.ACTION_DOWN, point.x, point.y, 0);
                mMapView.dispatchTouchEvent(simulationEvent);

                simulationEvent = MotionEvent.obtain(touchTime, touchTime,
                        MotionEvent.ACTION_UP, point.x, point.y, 0);

                mMapView.dispatchTouchEvent(simulationEvent);
            }
        }, 1100);
    }

    //region Highlight Helpers
    protected void highlight(Marker feature){
        clearHighlights();

        MarkerOptions options = new MarkerOptions();
        options.anchor(0.5f, 0.5f);
        options.position(feature.getPosition());
        options.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_crosshairs_gps_white_24dp));

        mHighlightedMarker = mMap.addMarker(options);

        zoomToFeature(mHighlightedMarker);
    }

    public void highlight(Polygon feature){
        clearHighlights();

        PolygonOptions options = new PolygonOptions();
        options.strokeColor(invertColor(feature.getStrokeColor()));
        options.fillColor(invertColor(feature.getFillColor()));
        options.strokeWidth(feature.getStrokeWidth());

        LatLng[] points = new LatLng[feature.getPoints().size()];

        feature.getPoints().toArray(points);

        options.zIndex(1000);
        options.add(points);

        mHighlightedPolygon = mMap.addPolygon(options);

        polyZoomToFeature(mHighlightedPolygon.getPoints());
    }

    public void highlight(Polyline feature){
        clearHighlights();

        PolylineOptions options = new PolylineOptions();
        options.color(invertColor(feature.getColor()));

        LatLng[] points = new LatLng[feature.getPoints().size()];

        feature.getPoints().toArray(points);

        options.zIndex(1000);
        options.add(points);

        mHighlightedPolyline = mMap.addPolyline(options);

        polyZoomToFeature(mHighlightedPolyline.getPoints());
    }

    @Override
    public void centerMap(LatLng position) {
        CameraUpdate update = CameraUpdateFactory.newLatLng(position);

        mMap.animateCamera(update);
    }

    protected void zoomToFeature(Marker highlightedMarker){
        float zoom = mMap.getCameraPosition().zoom;

        CameraUpdate update = CameraUpdateFactory.newLatLng(highlightedMarker.getPosition());

        mMap.moveCamera(update);

        LatLngBounds bounds = mMap.getProjection().getVisibleRegion().latLngBounds;

        LatLngBounds dblBounds = createDoubleBounds(bounds);

        CameraUpdate update2 = CameraUpdateFactory.newLatLngBounds(dblBounds, 0);

        mMap.animateCamera(update2);

        double longDiff = (dblBounds.getCenter().longitude - highlightedMarker.getPosition().longitude) / 2;

        double lngBtwCenterMarker = highlightedMarker.getPosition().longitude + longDiff;

        CameraUpdate finalUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(dblBounds.getCenter().latitude, lngBtwCenterMarker), zoom);

        mMap.animateCamera(finalUpdate);
    }

    public void polyZoomToFeature(List<LatLng> points){
        final LatLngBounds bounds = getBounds(points);

        LatLngBounds horizontalDoubleBounds = createDoubleBounds(bounds);

        CameraUpdate update = CameraUpdateFactory.newLatLngBounds(horizontalDoubleBounds, 50);

        mMap.animateCamera(update);
    }

    private LatLngBounds createDoubleBounds(LatLngBounds bounds) {
        double diff = bounds.southwest.longitude - bounds.northeast.longitude;

        double longitude = bounds.northeast.longitude - diff;

        LatLng northeast = new LatLng(bounds.northeast.latitude, longitude);

        return new LatLngBounds(bounds.southwest , northeast);
    }



    protected int invertColor(int color){
        return (0xFFFFFF - color) | 0xFF000000;
    }

    public Boolean highlightLine(LatLng position){
        Boolean foundMatch = false;

        Iterable<Polyline> lines = mLayerManager.getVisiblePolylines();

        double tolerance = calculateTolerance(mMap.getCameraPosition().zoom);

        for (Polyline line : lines) {
            if (PolyUtil.isLocationOnPath(position, line.getPoints(), false, tolerance)) { //idea: reset tolerance by zoom level
                getFeatureWindow(line.getId(), LayerManager.LINE);

                highlight(line);
                foundMatch = true;
                break;
            }
        }

        return foundMatch;
    }

    private double calculateTolerance(float zoom) {
        //Log.d(TAG, "Current: " + zoom);
        //Log.d(TAG, "Max: " + mMap.getMaxZoomLevel());
        //Log.d(TAG, "Min: " + mMap.getMinZoomLevel());

        if(zoom >= 12){
            return 450.0;
        }

        if(zoom > 10 && zoom < 12){
            return 750.0;
        }

        if(zoom > 8 && zoom <= 10){
            return 1300.0;
        }

        if(zoom > 5 && zoom <= 8){
            return 1800.0;
        }

        return 3300.0;
    }

    protected void  highlightPolygon(LatLng position){
        Iterable<Polygon> polygons = mLayerManager.getVisiblePolygons();

        for (Polygon ss : polygons) {
            if (PolyUtil.containsLocation(position, ss.getPoints(), true)) {
                getFeatureWindow(ss.getId(), LayerManager.POLYGON);

                highlight(ss);
                break;
            }

        }
    }
    //endregion

    @Override
    public void onConnected(Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        if (connectionResult.hasResolution()) {
            try {
                // Start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult(mActivity, CONNECTION_FAILURE_RESOLUTION_REQUEST);
            } catch (IntentSender.SendIntentException e) {
                e.printStackTrace();

                mAnalytics.sendException(e);
            }
        } else {
            Log.i(TAG, "Location services connection failed with code " + connectionResult.getErrorCode());
        }
    }

    //region Configs / Getters & Setters
    @Override
    public GoogleMap getMap() {
        return mMap;
    }

    @Override
    public void setMap(GoogleMap map) {
        mMap = map;
    }

    @Override
    public UiSettings getUiSettings() {
        return mUiSettings;
    }

    @Override
    public void setUiSettings(UiSettings uiSettings) {
        mUiSettings = uiSettings;
    }

    @Override
    public ILayerManager getLayerManager() {
        return mLayerManager;
    }

    @Override
    public void setLayerManager(ILayerManager layerManager) {
        mLayerManager = layerManager;
    }

    @Override
    public Context getContext() {
        return mContext;
    }

    @Override
    public void setContext(Context context) {
        mContext = context;
    }

    @Override
    public GoogleApiClient getLocationClient() {
        return mLocationClient;
    }

    @Override
    public void setLocationClient(GoogleApiClient locationClient) {
        mLocationClient = locationClient;
    }

    @Override
    public Activity getActivity() {
        return mActivity;
    }

    @Override
    public void setActivity(Activity activity) {
        mActivity = activity;
    }
    //endregion

    //region GeoUndergroundMap.Configuration
    @Override
    public void onPause() {
        if (notNull(mMapView)) {
            mMapView.onPause();
        }

        if(notNull(mLocationClient) && mLocationClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mLocationClient, this);
            mLocationClient.disconnect();

        }
    }

    @Override
    public void onDestroy() {
        mLayerManager.clearVisibleLayers();
        if(notNull(mMapView)) {
            mMapView.onDestroy();
        }
    }

    private boolean notNull(Object obj) {
        return obj != null;
    }

    @Override
    public void onLowMemory() {
        if(notNull(mMapView)) {
            mMapView.onLowMemory();
        }
    }

    @Override
    public void onResume() {
        if(notNull(mMapView)) {
            mMapView.onResume();
        }
    }

    @Override
    public void onStop(){
        mMapStateService.saveMapState(new MapStateSaveRequest(mMap));

        if(mLocationClient != null) {
            mLocationClient =  null;
        }
    }
    //endregion

    //region Helpers
    protected void setMapState() {
        CameraPosition position = mMapStateService.getSavedCameraPosition();
        Integer mapType = mMapStateService.getSavedMapType();
        mMap.setMapType(mapType);
        if(position != null){
            CameraUpdate update = CameraUpdateFactory.newCameraPosition(position);
            mMap.moveCamera(update);
        }
    }

    protected void handleNewLocation(Location currentLocation) {

        if(mCurrentLocationMaker != null){
            mCurrentLocationMaker.remove();
        }

        LatLng ll = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());

        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(ll, 16);

        mMap.animateCamera(update);

    }

    public void getFeatureWindow(String id, int shapeCode){
        mStatusBarManager.setMessage(mActivity.getString(R.string.loading_feature_window));

        mSelectedFeatureId = mLayerManager.getFeatureId(id, shapeCode);
        mSelectedLayerId = mLayerManager.getLayerId(id, shapeCode);

        mQueryService.featureWindow(mSelectedFeatureId, mSelectedLayerId);
    }

    protected int getTypeCode() {
        if(mHighlightedMarker != null){
            return GeometryTypeCodes.Point;
        }

        if(mHighlightedPolyline != null){
            return GeometryTypeCodes.Line;
        }

        if(mHighlightedPolygon != null){
            return GeometryTypeCodes.Polygon;
        }
        return 0;
    }

    public LatLng getHighlightCenter() {
        if(mHighlightedMarker != null){
            return mHighlightedMarker.getPosition();
        }

        if(mHighlightedPolyline != null){
            LatLngBounds bounds = getBounds(mHighlightedPolyline.getPoints());

            return bounds.getCenter();
        }

        if(mHighlightedPolygon != null){
            LatLngBounds bounds = getBounds(mHighlightedPolygon.getPoints());

            return bounds.getCenter();

        }
        return null;
    }

    private LatLngBounds getBounds(List<LatLng> points) {
        LatLngBounds.Builder builder = new LatLngBounds.Builder();

        for (LatLng point : points) {
            builder.include(point);
        }

        return builder.build();
    }
    //endregion

    @Override
    public void onPostExecute(List<Folder> model) {
        application.setLayerFolders(model);

        List<LegendLayer> llayers = getLayersFromFolders(model);

        for (LegendLayer legendLayer : llayers){
            application.addLegendLayerToQueue(legendLayer);
        }

        for(LegendLayer llayer : llayers){
            ILayerStyleTask layerStyleTask = new LayerStyleTask();

            layerStyleTask.getActiveStyle(llayer);
        }

        if(application.getIsTablet()){
            application.setMapStateLoaded(true);
            mProgressDialogHelper.hideProgressDialog();
        }

        ((MainTabletActivity)application.getGeoMainActivity()).showLayers();
        ((MainTabletActivity)application.getGeoMainActivity()).closeInfoFragment();
    }

    protected List<LegendLayer> getLayersFromFolders(List<Folder> layerFolders) {
        List<LegendLayer> result = new ArrayList<>();
        HashMap<Integer, Layer> layerHashMap = new HashMap<>();


        for(Folder folder : layerFolders){

            result.add(new LegendLayer(folder));

            if(folder.getLayers() != null && !folder.getLayers().isEmpty()){
                List<Layer> layers = new ArrayList<>();

                layers.addAll(folder.getLayers());

                for(Layer layer : layers){
                    layerHashMap.put(layer.getId(), layer);
                    result.add(new LegendLayer(layer));
                }
            }
        }

        application.setLayerHashMap(layerHashMap);

        return result;
    }

    @Override
    public void onLocationChanged(Location location) {
        handleNewLocation(location);
    }


}
