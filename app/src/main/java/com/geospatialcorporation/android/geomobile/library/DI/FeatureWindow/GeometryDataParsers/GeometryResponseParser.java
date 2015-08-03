package com.geospatialcorporation.android.geomobile.library.DI.FeatureWindow.GeometryDataParsers;

import com.geospatialcorporation.android.geomobile.library.DI.FeatureWindow.models.FeatureWindowData;
import com.geospatialcorporation.android.geomobile.library.helpers.converter.ICoordinateConverter;

public interface GeometryResponseParser {
    FeatureWindowData parse(ICoordinateConverter converter);
}
