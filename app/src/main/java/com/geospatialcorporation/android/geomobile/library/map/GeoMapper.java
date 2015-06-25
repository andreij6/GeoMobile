package com.geospatialcorporation.android.geomobile.library.map;

import com.geospatialcorporation.android.geomobile.application;
import com.geospatialcorporation.android.geomobile.models.Query.map.response.Feature;
import com.geospatialcorporation.android.geomobile.models.Query.map.response.MapQueryResponse;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

/**
 * Created by andre on 6/24/2015.
 */
public class GeoMapper implements IGeoMapper  {

    GoogleMap mMap;

    public GeoMapper(){
        mMap = application.getGoogleMap();
    }

    @Override
    public void map(List<MapQueryResponse> responses) {
        for(MapQueryResponse response : responses){
            for(Feature feature : response.getFeatures()){
                MarkerOptions point = new MarkerOptions().position(getLatLng(feature));

                point.flat(true);

                mMap.addMarker(point);
            }
        }
    }

    private LatLng getLatLng(Feature feature) {
        return new LatLng(feature.getGeometry().getY(), feature.getGeometry().getX());
    }
}
