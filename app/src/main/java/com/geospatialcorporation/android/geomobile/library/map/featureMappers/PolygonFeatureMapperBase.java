package com.geospatialcorporation.android.geomobile.library.map.featureMappers;

import com.geospatialcorporation.android.geomobile.models.Layers.Point;
import com.geospatialcorporation.android.geomobile.models.Query.map.response.Geometry;
import com.geospatialcorporation.android.geomobile.models.Query.map.response.Ring;
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
}
