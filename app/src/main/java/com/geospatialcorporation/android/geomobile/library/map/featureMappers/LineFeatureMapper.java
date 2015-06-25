package com.geospatialcorporation.android.geomobile.library.map.featureMappers;

import com.geospatialcorporation.android.geomobile.library.helpers.GeoColor;
import com.geospatialcorporation.android.geomobile.models.Layers.Layer;
import com.geospatialcorporation.android.geomobile.models.Query.map.response.Feature;
import com.geospatialcorporation.android.geomobile.models.Query.map.response.Style;
import com.google.android.gms.maps.model.PolylineOptions;



/**
 * Created by andre on 6/24/2015.
 */
public class LineFeatureMapper extends LineFeatureMapperBase {

    @Override
    public IFeatureMapper draw(Feature feature) {
        drawLines(feature.getGeometry());

        return this;
    }
}
