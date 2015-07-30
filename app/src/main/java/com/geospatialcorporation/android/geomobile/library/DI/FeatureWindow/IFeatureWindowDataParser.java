package com.geospatialcorporation.android.geomobile.library.DI.FeatureWindow;

import com.geospatialcorporation.android.geomobile.library.DI.FeatureWindow.models.FeatureWindowData;
import com.geospatialcorporation.android.geomobile.models.Query.map.response.featurewindow.FeatureQueryResponse;


public interface IFeatureWindowDataParser {

    FeatureWindowData parseResponse(FeatureQueryResponse response, Integer mapinfo);
}
