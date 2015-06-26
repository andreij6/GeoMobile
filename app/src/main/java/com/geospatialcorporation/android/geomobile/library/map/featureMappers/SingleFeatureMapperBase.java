package com.geospatialcorporation.android.geomobile.library.map.featureMappers;

import com.geospatialcorporation.android.geomobile.library.map.featureMappers.FeatureMapperBase;
import com.geospatialcorporation.android.geomobile.models.Layers.LegendLayer;
import com.geospatialcorporation.android.geomobile.models.Query.map.response.Geometry;
import com.geospatialcorporation.android.geomobile.models.Query.map.response.Style;

/**
 * Created by andre on 6/26/2015.
 */
public abstract class SingleFeatureMapperBase<T> extends FeatureMapperBase<T> {

    public abstract int addStyles(T option, Style style);

    public abstract void addMapObject(LegendLayer layer, T option);

    public abstract void drawFeature(Geometry feature, T option);

}
