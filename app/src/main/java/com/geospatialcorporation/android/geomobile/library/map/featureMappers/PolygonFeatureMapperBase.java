package com.geospatialcorporation.android.geomobile.library.map.featureMappers;

import com.geospatialcorporation.android.geomobile.models.Layers.FeatureInfo;
import com.geospatialcorporation.android.geomobile.models.Layers.LegendLayer;
import com.geospatialcorporation.android.geomobile.models.Layers.Point;
import com.geospatialcorporation.android.geomobile.models.Query.map.response.mapquery.Geometry;
import com.geospatialcorporation.android.geomobile.models.Query.map.response.mapquery.Ring;
import com.geospatialcorporation.android.geomobile.models.Query.map.response.mapquery.Style;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolygonOptions;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by andre on 6/25/2015.
 */
public abstract class PolygonFeatureMapperBase extends SingleFeatureMapperBase<PolygonOptions> {

    int mStroke;

    public PolygonFeatureMapperBase(){
        mStroke = 0;
    }

    public void drawFeature(Geometry geometry, PolygonOptions option) {
        List<Ring> ringList = geometry.getRings();
        int ringSize = ringList.size();

        Ring[] rings = new Ring[ringSize];
        ringList.toArray(rings);

        for (int i = 0; i < ringSize; i++) {
            List<LatLng> points = new ArrayList<>(ringSize);
            List<Point> ringPoints = rings[i].getPoints();

            for (int x = 0; x < ringPoints.size(); x++) {
                Point point = ringPoints.get(x);

                points.add(point.getLatLng());
            }

            if(i == 0) {
                option.addAll(points);
            } else {
                option.addHole(points);
            }
        }


    }

    @Override
    public IFeatureMapper addStyle(Style style) {
        addStyles(mMapFeature, style);

        return this;
    }

    @Override
    public int addStyles(PolygonOptions mapFeature, Style style) {
        if(mStroke == 0 || mColor == 0) {
            mStroke = mGeoColor.parseColor(style.getBorderColor());
            mColor = mGeoColor.parseColor(style.getFillColor());
        }

        mapFeature.strokeColor(mStroke);
        mapFeature.fillColor(mColor);
        mapFeature.geodesic(true);

        return mColor;
    }

    @Override
    public void commit(LegendLayer layer) {

        addMapObject(layer, mMapFeature);

        setLegendIcon(layer);
    }

    @Override
    public void addMapObject(LegendLayer layer, PolygonOptions option) {
        mLayerManager.addPolygon(layer.getLayer().getId(), option, new FeatureInfo(layer.getLayer().getId(), mFeatureId));
    }

    @Override
    public void reset() {
        mMapFeature = new PolygonOptions();
    }

}
