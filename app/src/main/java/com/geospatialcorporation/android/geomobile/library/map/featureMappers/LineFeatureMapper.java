package com.geospatialcorporation.android.geomobile.library.map.featureMappers;

import android.graphics.Color;

import com.geospatialcorporation.android.geomobile.models.Layers.Layer;
import com.geospatialcorporation.android.geomobile.models.Query.map.response.Feature;
import com.geospatialcorporation.android.geomobile.models.Query.map.response.Geometry;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by andre on 6/24/2015.
 */
public class LineFeatureMapper extends FeatureMaperBase {

    @Override
    public void draw(Feature feature) {
        PolylineOptions polyline = new PolylineOptions();
        int pointsCount = feature.getGeometry().getPoints().size();

        Geometry[] points = new Geometry[pointsCount];
        feature.getGeometry().getPoints().toArray(points);

        LatLng[] vertices = new LatLng[pointsCount];

        for (int i = 0; i < pointsCount; i++) {
            vertices[i] = points[i].getLatLng();
        }

        polyline.add(vertices);

        polyline.width(2f); //use styles from Response

        polyline.color(Color.BLACK);

        mMap.addPolyline(polyline);

    }
}
