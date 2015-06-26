package com.geospatialcorporation.android.geomobile.library.map.featureMappers;

import com.geospatialcorporation.android.geomobile.models.Layers.Layer;
import com.geospatialcorporation.android.geomobile.models.Query.map.response.Feature;
import com.geospatialcorporation.android.geomobile.models.Query.map.response.Geometry;
import com.geospatialcorporation.android.geomobile.models.Query.map.response.Style;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

/**
 * Created by andre on 6/25/2015.
 */
public class MultiPointFeatureMapper extends MultiFeatureMapperBase<MarkerOptions, PointFeatureMapperBase> {

    public MultiPointFeatureMapper() {
        super(new PointFeatureMapper());
    }

    @Override
    public IFeatureMapper draw(Feature feature) {

        drawFeature(feature.getGeometry().getPoints());

        return this;
    }

    @Override
    protected MarkerOptions newOptionType() {
        return new MarkerOptions();
    }
}
