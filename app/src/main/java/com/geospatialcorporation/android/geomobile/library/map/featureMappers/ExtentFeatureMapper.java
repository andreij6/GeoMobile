package com.geospatialcorporation.android.geomobile.library.map.featureMappers;

import com.geospatialcorporation.android.geomobile.models.Layers.LegendLayer;
import com.geospatialcorporation.android.geomobile.models.Query.map.response.mapquery.Feature;
import com.geospatialcorporation.android.geomobile.models.Query.map.response.mapquery.Style;
import com.google.android.gms.maps.model.PolygonOptions;

/**
 * Created by andre on 6/24/2015.
 */
public class ExtentFeatureMapper  extends FeatureMapperBase<PolygonOptions> {
    //check map feature type

    @Override
    public IFeatureMapper draw(Feature feature) {
        return null;
    }

    @Override
    public IFeatureMapper addStyle(Style style) {
        return null;
    }

    @Override
    public void commit(LegendLayer layer) {

    }

    @Override
    public void reset() {

    }
}
