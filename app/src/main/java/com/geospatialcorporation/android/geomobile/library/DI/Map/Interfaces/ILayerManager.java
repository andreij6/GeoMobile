package com.geospatialcorporation.android.geomobile.library.DI.Map.Interfaces;

import com.geospatialcorporation.android.geomobile.models.Layers.Extent;
import com.geospatialcorporation.android.geomobile.models.Layers.FeatureInfo;
import com.geospatialcorporation.android.geomobile.models.Layers.LegendLayer;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.List;

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

    void showLayers();
    void clearVisibleLayers();
    List<Integer> getVisibleLayerIds();

    void removeExtent(int id);
    void addVisibleLayerExtent(int id, Extent extent);
    void addAllLayersExtent(int id, Extent extent);

    Extent getFullExtent();
    void zoomToExtent(Extent extent);

    void setMap(GoogleMap map);

    void showLayer(LegendLayer llayer);
}
