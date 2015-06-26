package com.geospatialcorporation.android.geomobile.library.map.featureMappers;

import com.geospatialcorporation.android.geomobile.application;
import com.geospatialcorporation.android.geomobile.models.Layers.LegendLayer;
import com.geospatialcorporation.android.geomobile.models.Layers.Point;
import com.geospatialcorporation.android.geomobile.models.Query.map.response.Geometry;
import com.geospatialcorporation.android.geomobile.models.Query.map.response.Ring;
import com.geospatialcorporation.android.geomobile.models.Query.map.response.Style;
import com.google.android.gms.maps.model.PolygonOptions;

/**
 * Created by andre on 6/25/2015.
 */
public abstract class PolygonFeatureMapperBase extends SingleFeatureMapperBase<PolygonOptions> {

    public void drawFeature(Geometry geometry, PolygonOptions option){
        int ringSize = geometry.getRings().size();

        Ring[] rings = new Ring[ringSize];
        geometry.getRings().toArray(rings);

        for(int i = 0; i < ringSize; i++){
            for(int x = 0; x < rings[i].getPoints().size(); x++){
                Point point = rings[i].getPoints().get(x);

                option.add(point.getLatLng());
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
        int stroke = mGeoColor.parseColor(style.getBorderColor());
        mColor = mGeoColor.parseColor(style.getFillColor());

        mapFeature.strokeColor(stroke);
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
    public void addMapObject(final LegendLayer layer, final PolygonOptions option){
        layer.setMapObject(mMap.addPolygon(option));
    }

    @Override
    public void reset() {
        mMapFeature = new PolygonOptions();
    }
}
