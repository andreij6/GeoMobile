package com.geospatialcorporation.android.geomobile.library.DI.Map.Implementations;

import android.util.Log;

import com.geospatialcorporation.android.geomobile.application;
import com.geospatialcorporation.android.geomobile.library.DI.Map.Interfaces.ILayerManager;
import com.geospatialcorporation.android.geomobile.library.constants.GeometryTypeCodes;
import com.geospatialcorporation.android.geomobile.library.map.layerManager.IOptionsManager;
import com.geospatialcorporation.android.geomobile.library.map.layerManager.implementations.ClusterExtentMarkerOptionsManager;
import com.geospatialcorporation.android.geomobile.library.map.layerManager.implementations.PolygonOptionsManager;
import com.geospatialcorporation.android.geomobile.library.map.layerManager.implementations.PolylineOptionsManager;
import com.geospatialcorporation.android.geomobile.models.Layers.Extent;
import com.geospatialcorporation.android.geomobile.models.Layers.FeatureInfo;
import com.geospatialcorporation.android.geomobile.models.Layers.Layer;
import com.geospatialcorporation.android.geomobile.models.Layers.LegendLayer;
import com.geospatialcorporation.android.geomobile.models.Layers.Point;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
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

    private static final String TAG = LayerManager.class.getSimpleName();

    IOptionsManager mMarkerManager;
    IOptionsManager mPolygonOptionsManager;
    IOptionsManager mPolylineOptionsManager;

    GoogleMap mMap;

    HashMap<Integer, Extent> mVisibleLayersExtentMap;
    HashMap<Integer, Extent> mAllLayersExtentMap;

    public LayerManager(){
        mMarkerManager = getMarkerManger();
        mPolygonOptionsManager = new PolygonOptionsManager();
        mPolylineOptionsManager = new PolylineOptionsManager();

        mVisibleLayersExtentMap = new HashMap<>();
        mAllLayersExtentMap = new HashMap<>();
    }

    //region Interface
    @Override
    public void showLayers(GoogleMap map){
        mMap = map;

        mMarkerManager.showLayers(mMap);
        mPolygonOptionsManager.showLayers(mMap);
        mPolylineOptionsManager.showLayers(mMap);
    }

    @Override
    public void showLayer(LegendLayer llayer) {

        int code = llayer.getLayer().getGeometryTypeCodeId();

        switch (code){
            case GeometryTypeCodes.Point:
            case GeometryTypeCodes.MultiPoint:
                mMarkerManager.showLayers(mMap);
                break;
            case GeometryTypeCodes.Line:
            case GeometryTypeCodes.MultiLine:
                mPolylineOptionsManager.showLayers(mMap);
                break;
            case GeometryTypeCodes.Polygon:
            case GeometryTypeCodes.MultiPolygon:
                mPolygonOptionsManager.showLayers(mMap);
                break;
            default:
                break;
        }

    }

    @Override
    public boolean isLayerCached(Layer layer) {
        int code = layer.getGeometryTypeCodeId();

        switch (code){
            case GeometryTypeCodes.Point:
            case GeometryTypeCodes.MultiPoint:
                return mMarkerManager.isLayerCached(layer.getId());
            case GeometryTypeCodes.Line:
            case GeometryTypeCodes.MultiLine:
                return mPolylineOptionsManager.isLayerCached(layer.getId());
            case GeometryTypeCodes.Polygon:
            case GeometryTypeCodes.MultiPolygon:
                return mPolygonOptionsManager.isLayerCached(layer.getId());
            default:
                return false;
        }
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
        mVisibleLayersExtentMap.remove(id);
    }

    @Override
    public void addVisibleLayerExtent(int id, Extent extent) {
        addLayersExtent(mVisibleLayersExtentMap, id, extent);
    }

    @Override
    public void addAllLayersExtent(int id, Extent extent) {
        addLayersExtent(mAllLayersExtentMap, id, extent);
    }

    @Override
    public Extent getFullExtent(){
        if(mVisibleLayersExtentMap.isEmpty()){
            return setExtentFromMap(mAllLayersExtentMap);
        } else {
            return setExtentFromMap(mVisibleLayersExtentMap);
        }
    }

    @Override
    public void zoomToExtent(Extent extent){
        if(extent == null){
            return;
        }

        LatLng max = extent.getMaxLatLng();
        LatLng min = extent.getMinLatLng();

        LatLngBounds bounds = new LatLngBounds(min, max);

        int padding = 50;
        CameraUpdate u = CameraUpdateFactory.newLatLngBounds(bounds, padding);

        if(mMap == null){
            mMap = application.getGoogleMap();
        }

        mMap.animateCamera(u);
    }

    @Override
    public void setMap(GoogleMap map) {
        mMap = map;
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
        //mMarkerManager = new ClusterMarkerOptionsManager(application.getClusterManager());
        mMarkerManager = getMarkerManger();
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
    //endregion

    //region Helpers
    protected IOptionsManager getMarkerManger(){

        return new ClusterExtentMarkerOptionsManager(application.getClusterManager());
        //return new ClusterMakerOptionsManager(application.getClusterManager());
        //return new MarkerOptionsManager();
    }

    protected Extent setExtentFromMap(HashMap<Integer, Extent> extentMap){
        Extent result = null;

        for (Integer key : extentMap.keySet()) {
            Extent extent = extentMap.get(key);

            if (result == null) {
                result = new Extent(extent.getGeometryType(), extent.getMin(), extent.getMax());
            } else {
                result.setMax(compareMax(result.getMax(), extent.getMax()));
                result.setMin(compareMin(result.getMin(), extent.getMin()));
            }
        }

        return result;
    }

    protected void addLayersExtent(HashMap<Integer, Extent> map, int id, Extent extent){
        if(extent == null){
            return;
        }

        map.put(id, new Extent(8, extent.getMin(), extent.getMax()));
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
    //endregion

    public static final int POINT = 1;
    public static final int POLYGON = 2;
    public static final int LINE = 3;
}
