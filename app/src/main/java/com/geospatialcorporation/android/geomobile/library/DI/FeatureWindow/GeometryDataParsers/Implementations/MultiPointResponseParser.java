package com.geospatialcorporation.android.geomobile.library.DI.FeatureWindow.GeometryDataParsers.Implementations;

import com.geospatialcorporation.android.geomobile.library.DI.FeatureWindow.GeometryDataParsers.GeometryResponseParser;
import com.geospatialcorporation.android.geomobile.library.DI.FeatureWindow.models.FeatureWindowData;
import com.geospatialcorporation.android.geomobile.library.constants.MapInfoOrder;
import com.geospatialcorporation.android.geomobile.library.helpers.converter.ICoordinateConverter;
import com.geospatialcorporation.android.geomobile.models.Query.map.response.featurewindow.FeatureQueryResponse;
import com.geospatialcorporation.android.geomobile.models.Query.map.response.featurewindow.MapInfo;
import com.geospatialcorporation.android.geomobile.models.Query.map.response.featurewindow.WindowFeatures;

public class MultiPointResponseParser extends ResponseParserBase implements GeometryResponseParser {

    public MultiPointResponseParser(FeatureQueryResponse response) {
        super(response);
    }

    @Override
    public FeatureWindowData parse(ICoordinateConverter converter) {
        FeatureWindowData result = new FeatureWindowData();
        WindowFeatures features = mResponse.getFeatures().get(0);
        MapInfo mapInfo = features.getMapInfo();

        AddBasicAttributes(result, features, "MultiPoint");

        result.addEntry("Points", mapInfo.getPointCount() + "", MapInfoOrder.POINTS);
        AddCenterMinMax(result, mapInfo, converter);

        return result;
    }
}
