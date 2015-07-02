package com.geospatialcorporation.android.geomobile.library.map.layerManager;

import com.geospatialcorporation.android.geomobile.models.Layers.LegendLayer;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.PolylineOptions;

/**
 * Created by andre on 6/29/2015.
 */
public interface ILayerManager {

    void addLine(int id, PolylineOptions option);
    void addPoint(int id, MarkerOptions option);
    void addPolygon(int id, PolygonOptions option);
    void removeLayer(int layerId, int geometryTypeCode);
    int getLayerId(String markerID);
}
