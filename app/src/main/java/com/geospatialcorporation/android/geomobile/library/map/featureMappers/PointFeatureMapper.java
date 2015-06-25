package com.geospatialcorporation.android.geomobile.library.map.featureMappers;

import com.geospatialcorporation.android.geomobile.application;
import com.geospatialcorporation.android.geomobile.models.Query.map.response.Feature;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by andre on 6/24/2015.
 */
public class PointFeatureMapper extends FeatureMaperBase {

    public PointFeatureMapper(){
    }

    @Override
    public void draw(Feature feature) {
        MarkerOptions point = new MarkerOptions().position(getLatLng(feature));

        point.flat(true);

        mMap.addMarker(point);
    }

    private LatLng getLatLng(Feature feature) {
        return new LatLng(feature.getGeometry().getY(), feature.getGeometry().getX());
    }


}
