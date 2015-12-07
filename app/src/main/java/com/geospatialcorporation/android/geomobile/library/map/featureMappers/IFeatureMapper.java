package com.geospatialcorporation.android.geomobile.library.map.featureMappers;

import android.graphics.drawable.Drawable;

import com.geospatialcorporation.android.geomobile.models.Layers.LegendLayer;
import com.geospatialcorporation.android.geomobile.models.Query.map.response.mapquery.Feature;
import com.geospatialcorporation.android.geomobile.models.Query.map.response.mapquery.Style;

public interface IFeatureMapper {
    IFeatureMapper draw(Feature feature);

    IFeatureMapper addStyle(Style style);

    Drawable getActiveDrawable();

    void commit(LegendLayer llayer);

    void reset();

    void setLegendLayer(LegendLayer llayer);
}
