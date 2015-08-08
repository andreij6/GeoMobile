package com.geospatialcorporation.android.geomobile.library.DI.FeatureWindow.GeometryDataParsers.Implementations;

import com.geospatialcorporation.android.geomobile.library.DI.FeatureWindow.GeometryDataParsers.GeometryResponseParser;
import com.geospatialcorporation.android.geomobile.library.DI.FeatureWindow.models.FeatureWindowData;
import com.geospatialcorporation.android.geomobile.library.constants.MapInfoOrder;
import com.geospatialcorporation.android.geomobile.library.helpers.converter.ICoordinateConverter;
import com.geospatialcorporation.android.geomobile.models.Query.map.response.featurewindow.FeatureQueryResponse;
import com.geospatialcorporation.android.geomobile.models.Query.map.response.featurewindow.MapInfo;
import com.geospatialcorporation.android.geomobile.models.Query.map.response.featurewindow.WindowFeatures;

public class MultiPolygonResponseParser extends ResponseParserBase implements GeometryResponseParser {

    public MultiPolygonResponseParser(FeatureQueryResponse response) {
        super(response);
    }

    @Override
    public FeatureWindowData parse(ICoordinateConverter converter) {
        FeatureWindowData result = new FeatureWindowData();
        WindowFeatures features = mResponse.getFeatures().get(0);
        MapInfo mapInfo = features.getMapInfo();

        AddBasicAttributes(result, features, "MultiPolygon");

        result.addEntry("PointCount", mapInfo.getPointCount() + "", MapInfoOrder.POINTS);
        result.addEntry("Polygons", mapInfo.getPolygonCount() + "", MapInfoOrder.POLYGONS);

        AddCenterMinMax(result, mapInfo, converter);

        result.addEntry("Total Area", meterToFeet(mapInfo.getArea()), MapInfoOrder.TOTAL_AREA);
        result.addEntry("Total Diameter", meterToFeet(mapInfo.getLength()), MapInfoOrder.TOTAL_DIAMETER);

        return result;
    }




}
