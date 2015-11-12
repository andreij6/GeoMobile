package com.geospatialcorporation.android.geomobile.library.services.LayerEditor;

import android.content.Context;
import android.util.Log;

import com.geospatialcorporation.android.geomobile.application;
import com.geospatialcorporation.android.geomobile.library.DI.Map.Interfaces.ILayerManager;
import com.geospatialcorporation.android.geomobile.library.helpers.GeoColor;
import com.geospatialcorporation.android.geomobile.library.rest.LayerService;
import com.geospatialcorporation.android.geomobile.models.Layers.Layer;
import com.geospatialcorporation.android.geomobile.models.Layers.LegendLayer;
import com.geospatialcorporation.android.geomobile.ui.fragments.GoogleMapFragment;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.melnykov.fab.FloatingActionButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public abstract class LayerEditorBase<T> implements ILayerEditor {
    private static final String TAG = LayerEditorBase.class.getSimpleName();

    protected LegendLayer mLegendLayer;
    protected GoogleMap mMap;
    protected ILayerManager mLayerManager;
    protected GoogleMapFragment mMapFragment;
    protected Context mContext;
    Layer.StyleInfo mStyleInfo;
    protected HashMap<String, Change> mChangeMap;
    protected EditChangeRequest mChangeRequest;

    List<HashMap<Integer, T>> mEdits;
    List<HashMap<Integer, T>> mUndos;
    Iterable<T> mExistingFeatures;
    Layer mLayer;
    LayerService mLayerService;


    public LayerEditorBase(LegendLayer legendLayer, GoogleMap map, Context context) {
        mLegendLayer = legendLayer;
        mLayer = legendLayer.getLayer();
        mMap = map;
        mLayerManager = application.getLayerManager();
        mMapFragment  = application.getMapFragment();
        mContext = context;
        mStyleInfo = legendLayer.getLayer().getStyleInfo();

        mEdits = new ArrayList<>();
        mUndos = new ArrayList<>();
        mExistingFeatures = new ArrayList<>();

        mChangeMap = new HashMap<>();
        mChangeRequest = new EditChangeRequest();

        mLayerService = application.getRestAdapter().create(LayerService.class);
    }

    protected Iterable<LatLng> getLinePositions(List<Marker> lineMarkers) {
        List<LatLng> positions = new ArrayList<>();
        for(Marker marker : lineMarkers){
            positions.add(marker.getPosition());
        }
        return positions;
    }

    protected void clearMapListeners(){
        mMap.setOnMarkerClickListener(null);
        mMap.setOnMapClickListener(null);
        mMap.setOnCameraChangeListener(null);
        mMap.setOnMapLongClickListener(null);
    }

    @Override
    public void setUndoRedoListeners(FloatingActionButton undoBtn, FloatingActionButton redoBtn) {
        clearMapListeners();
    }

    @Override
    public void moveClickListener() {
        clearMapListeners();

    }

    public void removeClickListener() {
        clearMapListeners();
    }

    @Override
    public void addClickListener() {
        clearMapListeners();

    }

    @Override
    public void resetMapFragment() {

    }

    protected abstract boolean isExisting(T feature);

    protected HashMap<Integer,T> makeActionMap(T geometry, int action){
        HashMap<Integer, T> actionMap = new HashMap<>();

        actionMap.put(action, geometry);

        return actionMap;
    }

    protected void removeMarkers(Iterable<Marker> markers){
        for(Marker marker : markers){
            marker.remove();
        }
    }

    protected int getFillColor() {
        return new GeoColor().parseColor(mStyleInfo.FillColor);
    }

    protected int getStrokeColor() {
        return new GeoColor().parseColor(mStyleInfo.BorderColor);
    }

    protected float getWidth() {
        return mStyleInfo.BorderWidth;
    }

    protected boolean isAction(HashMap<Integer, T> map, int action) {
        return map.containsKey(action);
    }

    protected List<T> getCreatedFeatures(List<HashMap<Integer, T>> list) {
        List<T> result = new ArrayList<>();

        for (HashMap<Integer, T> map : list) {
            extractFeatures(result, map);
        }

        return  result;

    }

    private void extractFeatures(List<T> created, HashMap<Integer, T> map) {
        for (Integer key : map.keySet()) {
            created.add(map.get(key));
        }
    }

    @Override
    public void saveEdits() {
        mChangeRequest.setChanges(mChangeMap.values());

        Log.d(TAG, mChangeRequest.getChanges().size() + "nUMBER OF CHANGES ");

        mLayerService.editLayer(mLayer.getId(), mChangeRequest, new Callback<LayerFeatureChangeResponse>() {
            @Override
            public void success(LayerFeatureChangeResponse response, Response response2) {

                Log.d(TAG, "rESON");
            }

            @Override
            public void failure(RetrofitError error) {
                Log.d(TAG, "Failed");

            }
        });
        int count = 9;

        int som = count + 1;
    }
}
