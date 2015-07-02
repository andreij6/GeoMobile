package com.geospatialcorporation.android.geomobile.library.map.featureMappers;

import com.geospatialcorporation.android.geomobile.models.Query.map.response.mapquery.Feature;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by andre on 6/25/2015.
 */
public class MultiPointFeatureMapper extends MultiFeatureMapperBase<MarkerOptions, PointFeatureMapperBase> {

    public MultiPointFeatureMapper() {
        super(new PointFeatureMapper());
    }

    @Override
    public IFeatureMapper draw(Feature feature) {

        drawFeature(feature.getGeometry().getPoints(), feature.getId());

        return this;
    }

    @Override
    protected MarkerOptions newOptionType() {
        return new MarkerOptions();
    }
}
