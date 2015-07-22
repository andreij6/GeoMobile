package com.geospatialcorporation.android.geomobile.library.map.layerManager;

import com.geospatialcorporation.android.geomobile.application;
import com.geospatialcorporation.android.geomobile.library.constants.GeometryTypeCodes;
import com.geospatialcorporation.android.geomobile.library.map.layerManager.implementations.MarkerOptionsManager;
import com.geospatialcorporation.android.geomobile.library.map.layerManager.implementations.PolygonOptionsManager;
import com.geospatialcorporation.android.geomobile.library.map.layerManager.implementations.PolylineOptionsManager;
import com.geospatialcorporation.android.geomobile.models.Layers.FeatureInfo;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

/**
 * Created by andre on 6/29/2015.
 */
public class LayerManager implements ILayerManager {

    IOptionsManager<MarkerOptions, Marker> mMarkerManager;
    IOptionsManager<PolygonOptions, Polygon> mPolygonOptionsManager;
    IOptionsManager<PolylineOptions, Polyline> mPolylineOptionsManager;

    GoogleMap mMap;

    public LayerManager(){
        mMarkerManager = new MarkerOptionsManager();
        mPolygonOptionsManager = new PolygonOptionsManager();
        mPolylineOptionsManager = new PolylineOptionsManager();
    }

    @Override
    public void showLayers(){
        mMap = application.getGoogleMap();

        mMarkerManager.showLayers(mMap);
        mPolygonOptionsManager.showLayers(mMap);
        mPolylineOptionsManager.showLayers(mMap);
    }

    @Override
    public void clearVisibleLayers() {

        mMarkerManager.clearVisibleLayers();
        mPolygonOptionsManager.clearVisibleLayers();
        mPolylineOptionsManager.clearVisibleLayers();
    }

    @Override
    public void addLine(int id, PolylineOptions option, FeatureInfo featureInfo) {
        mPolylineOptionsManager.addOption(id, option, featureInfo);
    }

    public void addPoint(int layerId, MarkerOptions options, FeatureInfo featureInfo){
        mMarkerManager.addOption(layerId, options, featureInfo);
    }

    public void addPolygon(int layerId, PolygonOptions options, FeatureInfo featureInfo){
        mPolygonOptionsManager.addOption(layerId, options, featureInfo);
    }

    @Override
    public void removeLayer(int layerId, int code) {
        switch (code){
            case GeometryTypeCodes.Line:
            case GeometryTypeCodes.MultiLine:
                mPolylineOptionsManager.remove(layerId);
                break;
            case GeometryTypeCodes.Point:
            case GeometryTypeCodes.MultiPoint:
                mMarkerManager.remove(layerId);
                break;
            case GeometryTypeCodes.Polygon:
            case GeometryTypeCodes.MultiPolygon:
                mPolygonOptionsManager.remove(layerId);
                break;

        }
    }

    @Override
    public int getLayerId(String id, int shapeCode) {
        int result = 0;

        switch(shapeCode) {
            case POINT:
                result = mMarkerManager.getFeatureIdLayerId(id).getLayerId();
                break;
            case POLYGON:
                result = mPolygonOptionsManager.getFeatureIdLayerId(id).getLayerId();
                break;
            case LINE:
                result = mPolylineOptionsManager.getFeatureIdLayerId(id).getLayerId();
                break;
            default:
                break;
        }

        return result;
    }

    @Override
    public String getFeatureId(String id, int shapeCode) {
        String result = "";

        switch(shapeCode) {
            case POINT:
                result = mMarkerManager.getFeatureIdLayerId(id).getFeatureId();
                break;
            case POLYGON:
                result = mPolygonOptionsManager.getFeatureIdLayerId(id).getFeatureId();
                break;
            case LINE:
                result = mPolylineOptionsManager.getFeatureIdLayerId(id).getFeatureId();
                break;
            default:
                break;
        }

        return result;
    }

    @Override
    public void reset() {
        mMarkerManager = new MarkerOptionsManager();
        mPolygonOptionsManager = new PolygonOptionsManager();
        mPolylineOptionsManager = new PolylineOptionsManager();
    }

    @Override
    public Iterable<Polygon> getVisiblePolygons() {
        return mPolygonOptionsManager.getShowingLayers();
    }




    public Iterable<Polyline> getVisiblePolylines() {
        return mPolylineOptionsManager.getShowingLayers();
    }

    public static final int POINT = 1;
    public static final int POLYGON = 2;
    public static final int LINE = 3;
}
