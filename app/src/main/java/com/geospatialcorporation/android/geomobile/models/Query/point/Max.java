package com.geospatialcorporation.android.geomobile.models.Query.point;

import com.google.android.gms.maps.model.Marker;

public class Max extends Point{

    public Max(Marker marker){
        super(marker.getPosition().latitude, marker.getPosition().longitude);
    }

    @Override
    public String toString(){
        return "Latitude: " + X + " Longitude: " + Y;
    }
}
