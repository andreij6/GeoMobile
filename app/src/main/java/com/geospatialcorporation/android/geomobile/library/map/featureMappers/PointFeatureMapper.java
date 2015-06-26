package com.geospatialcorporation.android.geomobile.library.map.featureMappers;

import com.geospatialcorporation.android.geomobile.models.Query.map.response.Feature;


/**
 * Created by andre on 6/24/2015.
 */
public class PointFeatureMapper extends PointFeatureMapperBase {

    @Override
    public IFeatureMapper draw(Feature feature) {
        drawFeature(feature.getGeometry(), mMapFeature);

        return this;
    }



}
