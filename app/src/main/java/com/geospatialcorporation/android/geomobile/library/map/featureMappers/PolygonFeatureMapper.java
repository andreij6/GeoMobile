package com.geospatialcorporation.android.geomobile.library.map.featureMappers;

import com.geospatialcorporation.android.geomobile.models.Layers.Layer;
import com.geospatialcorporation.android.geomobile.models.Layers.Point;
import com.geospatialcorporation.android.geomobile.models.Query.map.response.Feature;
import com.geospatialcorporation.android.geomobile.models.Query.map.response.Ring;
import com.geospatialcorporation.android.geomobile.models.Query.map.response.Style;
import com.google.android.gms.maps.model.PolygonOptions;

/**
 * Created by andre on 6/24/2015.
 */
public class PolygonFeatureMapper extends PolygonFeatureMapperBase {

    @Override
    public IFeatureMapper draw(Feature feature) {
        drawPolygon(feature.getGeometry());

        return this;
    }

    @Override
    public IFeatureMapper addStyle(Style style) {
        int stroke = mGeoColor.parseColor(style.getBorderColor());
        int fill = mGeoColor.parseColor(style.getFillColor());

        mMapFeature.strokeColor(stroke);
        mMapFeature.fillColor(fill);

        mMapFeature.geodesic(true);

        return this;
    }

    @Override
    public void commit(Layer layer) {
        layer.setMapObject(mMap.addPolygon(mMapFeature));
    }

    @Override
    public void reset() {
        mMapFeature = new PolygonOptions();
    }
}
