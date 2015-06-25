package com.geospatialcorporation.android.geomobile.library.map.featureMappers;

import com.geospatialcorporation.android.geomobile.models.Query.map.response.Feature;
import com.geospatialcorporation.android.geomobile.models.Query.map.response.Style;

/**
 * Created by andre on 6/24/2015.
 */
public interface IFeatureMapper {
    IFeatureMapper draw(Feature feature);

    IFeatureMapper addStyle(Style style);

    void commit();

    void reset();
}
