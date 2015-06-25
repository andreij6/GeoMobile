package com.geospatialcorporation.android.geomobile.library.map.featureMappers;

import com.geospatialcorporation.android.geomobile.models.Layers.Layer;
import com.geospatialcorporation.android.geomobile.models.Query.map.response.Feature;
import com.geospatialcorporation.android.geomobile.models.Query.map.response.Style;
import com.google.android.gms.maps.model.PolygonOptions;

/**
 * Created by andre on 6/24/2015.
 */
public class RasterFeatureMapper  extends FeatureMapperBase<PolygonOptions> { //mapfeature type will change

    @Override
    public IFeatureMapper draw(Feature feature) {
        return null;
    }

    @Override
    public IFeatureMapper addStyle(Style style) {
        return null;
    }

    @Override
    public void commit(Layer layer) {

    }

    @Override
    public void reset() {

    }
}
