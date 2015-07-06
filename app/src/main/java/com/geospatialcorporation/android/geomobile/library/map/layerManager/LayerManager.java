package com.geospatialcorporation.android.geomobile.library.map.layerManager;

import com.geospatialcorporation.android.geomobile.application;
import com.geospatialcorporation.android.geomobile.library.constants.GeometryTypeCodes;
import com.geospatialcorporation.android.geomobile.library.map.layerManager.implementations.MarkerOptionsManager;
import com.geospatialcorporation.android.geomobile.library.map.layerManager.implementations.PolygonOptionsManager;
import com.geospatialcorporation.android.geomobile.library.map.layerManager.implementations.PolylineOptionsManager;
import com.geospatialcorporation.android.geomobile.models.Layers.LegendLayer;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.PolylineOptions;

/**
 * Created by andre on 6/29/2015.
 */
public class LayerManager implements ILayerManager {

    IOptionsManager<MarkerOptions> mMarkerManager;
    IOptionsManager<PolygonOptions> mPolygonOptionsManager;
    IOptionsManager<PolylineOptions> mPolylineOptionsManager;

    GoogleMap mMap;

    public LayerManager(){
        mMarkerManager = new MarkerOptionsManager();
        mPolygonOptionsManager = new PolygonOptionsManager();
        mPolylineOptionsManager = new PolylineOptionsManager();
    }

    public void showLayers(){
        mMap = application.getGoogleMap();

        mMarkerManager.showLayers(mMap);
        mPolygonOptionsManager.showLayers(mMap);
        mPolylineOptionsManager.showLayers(mMap);
    }

    @Override
    public void addLine(int id, PolylineOptions option) {
        mPolylineOptionsManager.addOption(id, option);
    }

    public void addPoint(int layerId, MarkerOptions options){
        mMarkerManager.addOption(layerId, options);
    }

    public void addPolygon(int layerId, PolygonOptions options){
        mPolygonOptionsManager.addOption(layerId, options);
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
    public int getLayerId(String id) {
        return Integer.parseInt(mMarkerManager.getFeatureId(id)[1]);
    }

    @Override
    public void reset() {
        mMarkerManager = new MarkerOptionsManager();
        mPolygonOptionsManager = new PolygonOptionsManager();
        mPolylineOptionsManager = new PolylineOptionsManager();
    }

    public String getFeatureId(String id) {
        return mMarkerManager.getFeatureId(id)[0];
    }


}
