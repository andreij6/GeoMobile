package com.geospatialcorporation.android.geomobile.library.map.featureMappers;

import com.geospatialcorporation.android.geomobile.models.Query.map.response.mapquery.Feature;

public class PointFeatureMapper extends PointFeatureMapperBase {

    @Override
    public IFeatureMapper draw(Feature feature) {
        setFeatureId(feature.getId());
        drawFeature(feature.getGeometry(), mMapFeature);

        return this;
    }



}
