package com.geospatialcorporation.android.geomobile.library.map.featureMappers;

import com.geospatialcorporation.android.geomobile.models.Layers.Layer;
import com.geospatialcorporation.android.geomobile.models.Query.map.response.Feature;
import com.geospatialcorporation.android.geomobile.models.Query.map.response.Geometry;
import com.geospatialcorporation.android.geomobile.models.Query.map.response.Style;
import com.google.android.gms.maps.model.PolygonOptions;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by andre on 6/25/2015.
 */
public class MultiPolygonFeatureMapper extends PolygonFeatureMapperBase {

    List<PolygonOptions> options;

    public MultiPolygonFeatureMapper() {
        options = new ArrayList<>();
    }

    @Override
    public IFeatureMapper draw(Feature feature) {
        int polygonCount = feature.getGeometry().getPolygons().size();

        Geometry[] polygons = new Geometry[polygonCount];
        feature.getGeometry().getPolygons().toArray(polygons);

        for (int i = 0; i < polygonCount; i++) {

            PolygonOptions option = new PolygonOptions();
            drawPolygon(polygons[i], option);
            options.add(option);

        }

        return this;
    }


    @Override
    public IFeatureMapper addStyle(Style style) {
        int stroke = mGeoColor.parseColor(style.getBorderColor());
        int fill = mGeoColor.parseColor(style.getFillColor());

        for (PolygonOptions option : options) {
            addStyles(option, stroke, fill);
        }

        return this;
    }

    @Override
    public void commit(Layer layer) {
        for (PolygonOptions option : options) {
            layer.setMapObject(mMap.addPolygon(option));
        }
    }

    @Override
    public void reset() {
        options = new ArrayList<>();
    }
}
