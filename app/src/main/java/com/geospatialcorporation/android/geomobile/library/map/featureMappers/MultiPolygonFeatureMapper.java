package com.geospatialcorporation.android.geomobile.library.map.featureMappers;

import com.geospatialcorporation.android.geomobile.models.Layers.Layer;
import com.geospatialcorporation.android.geomobile.models.Query.map.response.Feature;
import com.geospatialcorporation.android.geomobile.models.Query.map.response.Geometry;
import com.geospatialcorporation.android.geomobile.models.Query.map.response.Style;
import com.google.android.gms.maps.model.PolygonOptions;

/**
 * Created by andre on 6/25/2015.
 */
public class MultiPolygonFeatureMapper extends PolygonFeatureMapperBase {

    @Override
    public IFeatureMapper draw(Feature feature) {
        int polygonCount = feature.getGeometry().getPolygons().size();

        Geometry[] polygons = new Geometry[polygonCount];
        feature.getGeometry().getPolygons().toArray(polygons);

        for(int i = 0; i < polygonCount; i++){
            drawPolygon(polygons[i]);
        }

        return this;
    }

    @Override
    public IFeatureMapper addStyle(Style style) {
        int stroke = mGeoColor.parseColor(style.getBorderColor());
        int fillColor = mGeoColor.parseColor(style.getFillColor());

        mMapFeature.strokeColor(stroke);
        mMapFeature.fillColor(fillColor);

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
