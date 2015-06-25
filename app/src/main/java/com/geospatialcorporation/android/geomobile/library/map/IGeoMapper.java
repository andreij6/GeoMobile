package com.geospatialcorporation.android.geomobile.library.map;

import com.geospatialcorporation.android.geomobile.models.Query.map.response.MapQueryResponse;

import java.util.List;

/**
 * Created by andre on 6/24/2015.
 */
public interface IGeoMapper {
    void map(List<MapQueryResponse> response);
}
