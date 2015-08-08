package com.geospatialcorporation.android.geomobile.library.DI.FeatureWindow.GeometryDataParsers.Implementations;

import com.geospatialcorporation.android.geomobile.library.DI.FeatureWindow.GeometryDataParsers.GeometryResponseParser;
import com.geospatialcorporation.android.geomobile.library.DI.FeatureWindow.models.FeatureWindowData;
import com.geospatialcorporation.android.geomobile.library.constants.MapInfoOrder;
import com.geospatialcorporation.android.geomobile.library.helpers.converter.DMSCoordinateConverter;
import com.geospatialcorporation.android.geomobile.library.helpers.converter.ICoordinateConverter;
import com.geospatialcorporation.android.geomobile.models.Query.map.response.featurewindow.FeatureQueryResponse;
import com.geospatialcorporation.android.geomobile.models.Query.map.response.featurewindow.WindowFeatures;

public class PointResponseParser extends ResponseParserBase implements GeometryResponseParser {

    public PointResponseParser(FeatureQueryResponse response) {
        super(response);
    }

    @Override
    public FeatureWindowData parse(ICoordinateConverter converter) {
        FeatureWindowData result = new FeatureWindowData();
        WindowFeatures features = mResponse.getFeatures().get(0);

        AddBasicAttributes(result, features, "Point");

        result.addEntry("Lat", converter.convert(features.getMapInfo().getPoint().getY(), converter.getLatitiude_PosNeg()), MapInfoOrder.LAT);
        result.addEntry("Long", converter.convert(features.getMapInfo().getPoint().getX(), converter.getLongitude_PosNeg()), MapInfoOrder.LONG);

        return result;
    }
}
