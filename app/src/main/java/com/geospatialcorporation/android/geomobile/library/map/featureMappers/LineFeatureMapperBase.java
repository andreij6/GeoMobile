package com.geospatialcorporation.android.geomobile.library.map.featureMappers;

import com.geospatialcorporation.android.geomobile.application;
import com.geospatialcorporation.android.geomobile.library.helpers.GeoColor;
import com.geospatialcorporation.android.geomobile.models.Layers.LegendLayer;
import com.geospatialcorporation.android.geomobile.models.Query.map.response.Geometry;
import com.geospatialcorporation.android.geomobile.models.Query.map.response.Style;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;

/**
 * Created by andre on 6/25/2015.
 */
public abstract class LineFeatureMapperBase extends SingleFeatureMapperBase<PolylineOptions> {

    @Override
    public void drawFeature(Geometry geometry, PolylineOptions options){
        int pointsCount = geometry.getPoints().size();

        Geometry[] points = new Geometry[pointsCount];
        geometry.getPoints().toArray(points);

        LatLng[] vertices = new LatLng[pointsCount];

        for (int i = 0; i < pointsCount; i++) {
            vertices[i] = points[i].getLatLng();
        }

        options.add(vertices);
    }

    @Override
    public IFeatureMapper addStyle(Style style) {
        addStyles(mMapFeature, style);

        return this;
    }

    @Override
    public int addStyles(PolylineOptions option, Style style) {
        String geoColor = style.getBorderColor();
        int borderWidth = style.getBorderWidth();

        mColor = new GeoColor().parseColor(geoColor);

        option.width(borderWidth);
        option.color(mColor);

        return mColor;
    }

    @Override
    public void commit(LegendLayer layer) {
        addMapObject(layer, mMapFeature);

        setLegendIcon(layer);
    }

    @Override
    public void addMapObject(final LegendLayer layer, final PolylineOptions option) {
        layer.setMapObject(mMap.addPolyline(option));
    }

    @Override
    public void reset() {
        mMapFeature = new PolylineOptions();
    }
}
