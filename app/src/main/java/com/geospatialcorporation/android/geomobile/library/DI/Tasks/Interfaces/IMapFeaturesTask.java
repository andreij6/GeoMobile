package com.geospatialcorporation.android.geomobile.library.DI.Tasks.Interfaces;

import com.geospatialcorporation.android.geomobile.models.Layers.LegendLayer;
import com.geospatialcorporation.android.geomobile.models.Query.map.response.mapquery.MapQueryResponse;

import java.util.List;

public interface IMapFeaturesTask {

    void mapFeatures(List<MapQueryResponse> responses, LegendLayer legendLayer);

}
