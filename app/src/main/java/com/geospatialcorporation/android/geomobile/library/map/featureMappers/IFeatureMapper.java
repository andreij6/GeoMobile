package com.geospatialcorporation.android.geomobile.library.map.featureMappers;

import android.graphics.drawable.Drawable;

import com.geospatialcorporation.android.geomobile.models.Layers.LegendLayer;
import com.geospatialcorporation.android.geomobile.models.Query.map.response.mapquery.Feature;
import com.geospatialcorporation.android.geomobile.models.Query.map.response.mapquery.Style;

/**
 * Created by andre on 6/24/2015.
 */
public interface IFeatureMapper {
    IFeatureMapper draw(Feature feature);

    IFeatureMapper addStyle(Style style);

    Drawable getActiveDrawable();

    void commit(LegendLayer llayer);

    void reset();
}
