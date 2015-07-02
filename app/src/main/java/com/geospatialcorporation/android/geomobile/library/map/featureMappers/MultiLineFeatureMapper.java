package com.geospatialcorporation.android.geomobile.library.map.featureMappers;

import com.geospatialcorporation.android.geomobile.models.Query.map.response.mapquery.Feature;
import com.google.android.gms.maps.model.PolylineOptions;


/**
 * Created by andre on 6/25/2015.
 */
public class MultiLineFeatureMapper extends MultiFeatureMapperBase<PolylineOptions, LineFeatureMapperBase> {

    public MultiLineFeatureMapper() {
        super(new LineFeatureMapper());
    }

    @Override
    public IFeatureMapper draw(Feature feature) {

        drawFeature(feature.getGeometry().getLines(), feature.getId());

        return this;
    }

    @Override
    protected PolylineOptions newOptionType() {
        return new PolylineOptions();
    }
}
