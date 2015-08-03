package com.geospatialcorporation.android.geomobile.library.map.featureMappers;

import com.geospatialcorporation.android.geomobile.models.Query.map.response.mapquery.Feature;
import com.google.android.gms.maps.model.PolygonOptions;


/**
 * Created by andre on 6/25/2015.
 */
public class MultiPolygonFeatureMapper extends MultiFeatureMapperBase<PolygonOptions, PolygonFeatureMapper> {

    public MultiPolygonFeatureMapper() {
        super(new PolygonFeatureMapper());
    }

    @Override
    public IFeatureMapper draw(Feature feature) {
        drawFeature(feature.getGeometry().getPolygons(), feature.getId());

        return this;
    }

    @Override
    protected PolygonOptions newOptionType() {
        return new PolygonOptions();
    }
}
