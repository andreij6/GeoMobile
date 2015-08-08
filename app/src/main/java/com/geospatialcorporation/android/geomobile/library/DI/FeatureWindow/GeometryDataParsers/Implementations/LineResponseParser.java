package com.geospatialcorporation.android.geomobile.library.DI.FeatureWindow.GeometryDataParsers.Implementations;

import com.geospatialcorporation.android.geomobile.library.DI.FeatureWindow.GeometryDataParsers.GeometryResponseParser;
import com.geospatialcorporation.android.geomobile.library.DI.FeatureWindow.models.FeatureWindowData;
import com.geospatialcorporation.android.geomobile.library.constants.MapInfoOrder;
import com.geospatialcorporation.android.geomobile.library.helpers.converter.ICoordinateConverter;
import com.geospatialcorporation.android.geomobile.models.Query.map.response.featurewindow.FeatureQueryResponse;
import com.geospatialcorporation.android.geomobile.models.Query.map.response.featurewindow.MapInfo;
import com.geospatialcorporation.android.geomobile.models.Query.map.response.featurewindow.WindowFeatures;

public class LineResponseParser extends ResponseParserBase implements GeometryResponseParser {

    public LineResponseParser(FeatureQueryResponse response) {
        super(response);
    }

    @Override
    public FeatureWindowData parse(ICoordinateConverter converter) {
        FeatureWindowData result = new FeatureWindowData();
        WindowFeatures features = mResponse.getFeatures().get(0);
        MapInfo mapInfo = features.getMapInfo();

        AddBasicAttributes(result, features, "Line");

        result.addEntry("Points", mapInfo.getPointCount() + "", MapInfoOrder.POINTS);

        //TODO: Add Z-Index to Start & EndPoint
        result.addEntry("End Point", getLatLngtString(mapInfo.getEndPoint(), converter), MapInfoOrder.END_POINT);
        result.addEntry("Start Point", getLatLngtString(mapInfo.getStartPoint(), converter), MapInfoOrder.START_POINT);

        AddMinMax(result, mapInfo, converter);

        result.addEntry("Length", meterToFeet(mapInfo.getLength()), MapInfoOrder.LENGTH);

        return result;
    }
}
