package com.geospatialcorporation.android.geomobile.library.map.featureMappers;

import com.geospatialcorporation.android.geomobile.models.Layers.Layer;
import com.geospatialcorporation.android.geomobile.models.Layers.Point;
import com.geospatialcorporation.android.geomobile.models.Query.map.response.Geometry;
import com.geospatialcorporation.android.geomobile.models.Query.map.response.Ring;
import com.geospatialcorporation.android.geomobile.models.Query.map.response.Style;
import com.google.android.gms.maps.model.PolygonOptions;

/**
 * Created by andre on 6/25/2015.
 */
public abstract class PolygonFeatureMapperBase extends FeatureMapperBase<PolygonOptions> {

    public void drawPolygon(Geometry geometry){
        int ringSize = geometry.getRings().size();

        Ring[] rings = new Ring[ringSize];
        geometry.getRings().toArray(rings);

        for(int i = 0; i < ringSize; i++){
            for(int x = 0; x < rings[i].getPoints().size(); x++){
                Point point = rings[i].getPoints().get(x);

                mMapFeature.add(point.getLatLng());
            }
        }
    }

    public void drawPolygon(Geometry geometry, PolygonOptions option){
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
        int stroke = mGeoColor.parseColor(style.getBorderColor());
        int fill = mGeoColor.parseColor(style.getFillColor());

        addStyles(mMapFeature, stroke, fill);

        return this;
    }

    protected void addStyles(PolygonOptions mapFeature, Integer stroke, Integer fill) {
        mapFeature.strokeColor(stroke);
        mapFeature.fillColor(fill);

        mapFeature.geodesic(true);
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
