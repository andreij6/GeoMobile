package com.geospatialcorporation.android.geomobile.library.DI.FeatureWindow.Implementations;

import com.geospatialcorporation.android.geomobile.library.DI.FeatureWindow.GeometryDataParsers.GeometryResponseParser;
import com.geospatialcorporation.android.geomobile.library.DI.FeatureWindow.GeometryDataParsers.Implementations.LineResponseParser;
import com.geospatialcorporation.android.geomobile.library.DI.FeatureWindow.GeometryDataParsers.Implementations.MultiLineResponseParser;
import com.geospatialcorporation.android.geomobile.library.DI.FeatureWindow.GeometryDataParsers.Implementations.MultiPointResponseParser;
import com.geospatialcorporation.android.geomobile.library.DI.FeatureWindow.GeometryDataParsers.Implementations.MultiPolygonResponseParser;
import com.geospatialcorporation.android.geomobile.library.DI.FeatureWindow.GeometryDataParsers.Implementations.PointResponseParser;
import com.geospatialcorporation.android.geomobile.library.DI.FeatureWindow.GeometryDataParsers.Implementations.PolygonResponseParser;
import com.geospatialcorporation.android.geomobile.library.DI.FeatureWindow.IFeatureWindowDataParser;
import com.geospatialcorporation.android.geomobile.library.DI.FeatureWindow.models.FeatureWindowData;
import com.geospatialcorporation.android.geomobile.library.helpers.converter.ICoordinateConverter;
import com.geospatialcorporation.android.geomobile.models.Query.map.response.featurewindow.FeatureQueryResponse;

public class FeatureWindowDataParser implements IFeatureWindowDataParser {

    public static final Integer MAPINFO = 1;
    private FeatureWindowData mFeatureWindowData;
    private ICoordinateConverter mConverter;

    public FeatureWindowDataParser(ICoordinateConverter converter){
        mConverter = converter;
    }

    @Override
    public FeatureWindowData parseResponse(FeatureQueryResponse response, Integer windowType) {

        String shape = response.getFeatures().get(0).getMapInfo().getGeometryType().toLowerCase();

        if(windowType.equals(MAPINFO)) {
            switch (shape) {
                case "point":
                    parse(new PointResponseParser(response));
                    break;
                case "line":
                    parse(new LineResponseParser(response));
                    break;
                case "polygon":
                    parse(new PolygonResponseParser(response));
                    break;
                case "multipolygon":
                    parse(new MultiPolygonResponseParser(response));
                    break;
                case "multiline":
                    parse(new MultiLineResponseParser(response));
                    break;
                case "multipoint":
                    parse(new MultiPointResponseParser(response));
                    break;
                default:
                    break;
            }
        }

        return mFeatureWindowData;
    }

    protected void parse(GeometryResponseParser parser){
        mFeatureWindowData = parser.parse(mConverter);
    }
}
