package com.geospatialcorporation.android.geomobile.library.map.layerManager;

import android.widget.Toast;

import com.geospatialcorporation.android.geomobile.application;
import com.geospatialcorporation.android.geomobile.library.constants.GeometryTypeCodes;
import com.geospatialcorporation.android.geomobile.library.map.layerManager.implementations.MarkerOptionsManager;
import com.geospatialcorporation.android.geomobile.library.map.layerManager.implementations.PolygonOptionsManager;
import com.geospatialcorporation.android.geomobile.library.map.layerManager.implementations.PolylineOptionsManager;
import com.geospatialcorporation.android.geomobile.models.Layers.Extent;
import com.geospatialcorporation.android.geomobile.models.Layers.FeatureInfo;
import com.geospatialcorporation.android.geomobile.models.Layers.Layer;
import com.geospatialcorporation.android.geomobile.models.Layers.Point;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by andre on 6/29/2015.
 */
public class LayerManager implements ILayerManager {

    IOptionsManager<MarkerOptions, Marker> mMarkerManager;
    IOptionsManager<PolygonOptions, Polygon> mPolygonOptionsManager;
    IOptionsManager<PolylineOptions, Polyline> mPolylineOptionsManager;

    GoogleMap mMap;

    HashMap<Integer, Extent> mExtentMap;

    public LayerManager(){
        mMarkerManager = new MarkerOptionsManager();
        mPolygonOptionsManager = new PolygonOptionsManager();
        mPolylineOptionsManager = new PolylineOptionsManager();
        mExtentMap = new HashMap<Integer, Extent>();
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
    public List<Integer> getVisibleLayerIds() {
        List<Integer> result = new ArrayList<>();

        Iterable<Integer> markerIds = mMarkerManager.getShowingLayerIds();
        Iterable<Integer> lineIds = mPolylineOptionsManager.getShowingLayerIds();
        Iterable<Integer> polygonIds = mPolygonOptionsManager.getShowingLayerIds();

        for(Integer marker : markerIds){
            result.add(marker);
        }

        for(Integer line : lineIds){
            result.add(line);
        }

        for(Integer id : polygonIds){
            result.add(id);
        }

        return result;
    }

    @Override
    public void removeExtent(int id) {
        mExtentMap.remove(id);
    }

    @Override
    public void addExtent(int id, Extent extent) {
        if(extent == null){
            return;
        }

        mExtentMap.put(id, new Extent(8, extent.getMin(), extent.getMax()));
    }

    @Override
    public Extent getFullExtent(){
        Extent result = null;

        for(Integer key : mExtentMap.keySet()){
            Extent extent = mExtentMap.get(key);

            if(result == null){
                result = new Extent(extent.getGeometryType(), extent.getMin(), extent.getMax());
            } else {
                result.setMax(compareMax(result.getMax(), extent.getMax()));
                result.setMin(compareMin(result.getMin(), extent.getMin()));
            }
        }

        return result;
    }

    public void zoomToExtent(Extent extent){
        if(extent == null){
            return;
        }

        LatLng max = extent.getMaxLatLng();
        LatLng min = extent.getMinLatLng();

        LatLngBounds bounds = new LatLngBounds(min, max);

        int padding = 50;
        CameraUpdate u = CameraUpdateFactory.newLatLngBounds(bounds, padding);
        mMap.animateCamera(u);
    }

    protected Point compareMin(Point point1, Point point2) {
        double minX = Math.min(point1.getX(), point2.getX());
        double minY = Math.min(point1.getY(), point2.getY());

        return new Point(1, minX, minY);

    }

    protected Point compareMax(Point point1, Point point2) {
        double maxX = Math.max(point1.getX(), point2.getX());
        double maxY = Math.max(point1.getY(), point2.getY());

        return new Point(1, maxX, maxY);
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

    @Override
    public Iterable<Polyline> getVisiblePolylines() {
        return mPolylineOptionsManager.getShowingLayers();
    }

    public static final int POINT = 1;
    public static final int POLYGON = 2;
    public static final int LINE = 3;
}
