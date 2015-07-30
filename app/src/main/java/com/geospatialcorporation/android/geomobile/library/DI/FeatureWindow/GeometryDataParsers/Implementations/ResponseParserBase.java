package com.geospatialcorporation.android.geomobile.library.DI.FeatureWindow.GeometryDataParsers.Implementations;

import com.geospatialcorporation.android.geomobile.application;
import com.geospatialcorporation.android.geomobile.library.DI.FeatureWindow.models.FeatureWindowData;
import com.geospatialcorporation.android.geomobile.library.helpers.converter.DMSCoordinateConverter;
import com.geospatialcorporation.android.geomobile.library.helpers.converter.ICoordinateConverter;
import com.geospatialcorporation.android.geomobile.models.Layers.Layer;
import com.geospatialcorporation.android.geomobile.models.Layers.Point;
import com.geospatialcorporation.android.geomobile.models.Query.map.response.featurewindow.FeatureQueryResponse;
import com.geospatialcorporation.android.geomobile.models.Query.map.response.featurewindow.MapInfo;
import com.geospatialcorporation.android.geomobile.models.Query.map.response.featurewindow.WindowFeatures;


public abstract class ResponseParserBase {

    protected FeatureQueryResponse mResponse;
    private static final double METER_TO_FEET = 0.3048;

    public ResponseParserBase(FeatureQueryResponse response){
        mResponse = response;
    }

    protected String getLayerName(){
        Layer layer = application.getLayerHashMap().get(mResponse.getId());

        if(layer != null){
            return layer.getName();
        }

        return "Layer Name Not Found";
    }

    protected void AddBasicAttributes(FeatureWindowData result, WindowFeatures features, String shape) {
        result.addEntry("Feature Id", features.getId() + "");
        result.addEntry("Shape", shape);
        result.addEntry("Layer", getLayerName());
    }

    protected String getLatLngtString(Point point, ICoordinateConverter converter){
        String point_Lat = converter.convert(point.getY(), DMSCoordinateConverter.LAT);
        String point_Lng = converter.convert(point.getX(), DMSCoordinateConverter.LONG);
        return point_Lat + " \n " + point_Lng;
    }

    protected String meterToFeet(double measurement) {
        int measure_nodecimal = (int)measurement;

        return String.format("%.2f", (measure_nodecimal / METER_TO_FEET)) + " ft";
    }

    protected void AddCenterMinMax(FeatureWindowData result, MapInfo mapInfo, ICoordinateConverter converter) {
        result.addEntry("Center Point", getLatLngtString(mapInfo.getCenterPoint(), converter));
        
        AddMinMax(result, mapInfo, converter);
    }

    protected void AddMinMax(FeatureWindowData result, MapInfo mapInfo, ICoordinateConverter converter){
        result.addEntry("Min", getLatLngtString(mapInfo.getExtent().getMin(), converter));
        result.addEntry("Max", getLatLngtString(mapInfo.getExtent().getMax(), converter));
    }
}
