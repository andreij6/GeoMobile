package com.geospatialcorporation.android.geomobile.library.map;

import com.geospatialcorporation.android.geomobile.models.Layers.LegendLayer;
import com.geospatialcorporation.android.geomobile.models.Query.map.response.mapquery.MapQueryResponse;

import java.util.List;

/**
 * Created by andre on 6/24/2015.
 */
public interface IGeoMapper {
    void map(List<MapQueryResponse> response, LegendLayer llayer);
}
