package com.geospatialcorporation.android.geomobile.library.map.layerManager;

import com.geospatialcorporation.android.geomobile.models.Layers.FeatureInfo;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;


/**
 * Created by andre on 6/29/2015.
 */
public interface ILayerManager {

    void addLine(int id, PolylineOptions option, FeatureInfo featureInfo);
    void addPoint(int id, MarkerOptions option, FeatureInfo featureInfo);
    void addPolygon(int id, PolygonOptions option, FeatureInfo featureInfo);
    void removeLayer(int layerId, int geometryTypeCode);
    int getLayerId(String markerID, int shapeCode);
    void reset();
    Iterable<Polygon> getVisiblePolygons();
    Iterable<Polyline> getVisiblePolylines();
    String getFeatureId(String id, int shapeCode);
}
