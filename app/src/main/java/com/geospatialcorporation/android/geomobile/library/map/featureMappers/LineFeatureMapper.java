package com.geospatialcorporation.android.geomobile.library.map.featureMappers;

import android.graphics.Color;

import com.geospatialcorporation.android.geomobile.library.helpers.GeoColor;
import com.geospatialcorporation.android.geomobile.models.Layers.Layer;
import com.geospatialcorporation.android.geomobile.models.Query.map.response.Feature;
import com.geospatialcorporation.android.geomobile.models.Query.map.response.Geometry;
import com.geospatialcorporation.android.geomobile.models.Query.map.response.Style;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by andre on 6/24/2015.
 */
public class LineFeatureMapper extends FeatureMapperBase<PolylineOptions> {

    @Override
    public IFeatureMapper draw(Feature feature) {
        int pointsCount = feature.getGeometry().getPoints().size();

        Geometry[] points = new Geometry[pointsCount];
        feature.getGeometry().getPoints().toArray(points);

        LatLng[] vertices = new LatLng[pointsCount];

        for (int i = 0; i < pointsCount; i++) {
            vertices[i] = points[i].getLatLng();
        }

        mMapFeature.add(vertices);

        return this;
    }

    @Override
    public IFeatureMapper addStyle(Style style) {
        String geoColor = style.getBorderColor();
        int borderWidth = style.getBorderWidth();

        int color = new GeoColor().parseColor(geoColor);

        mMapFeature.width(borderWidth); //use styles from Response
        mMapFeature.color(color);

        return this;
    }

    @Override
    public void commit(Layer layer) {
        layer.setMapObject(mMap.addPolyline(mMapFeature));
    }

    @Override
    public void reset() {
        mMapFeature = new PolylineOptions();
    }
}
