package com.geospatialcorporation.android.geomobile.library.map.featureMappers;

import com.geospatialcorporation.android.geomobile.models.Layers.Layer;
import com.geospatialcorporation.android.geomobile.models.Layers.Point;
import com.geospatialcorporation.android.geomobile.models.Query.map.response.Feature;
import com.geospatialcorporation.android.geomobile.models.Query.map.response.Geometry;
import com.geospatialcorporation.android.geomobile.models.Query.map.response.Ring;
import com.geospatialcorporation.android.geomobile.models.Query.map.response.Style;
import com.google.android.gms.maps.model.PolygonOptions;

/**
 * Created by andre on 6/24/2015.
 */
public class PolygonFeatureMapper extends PolygonFeatureMapperBase {

    @Override
    public IFeatureMapper draw(Feature feature) {
        drawFeature(feature.getGeometry(), mMapFeature);

        return this;
    }


}
