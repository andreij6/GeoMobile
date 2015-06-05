package com.geospatialcorporation.android.geomobile.library.helpers;

import com.google.android.gms.maps.model.Marker;

import java.util.Comparator;

/**
 * Created by andre on 6/4/2015.
 */
public class MarkerComparer implements Comparator<Marker> {
    @Override
    public int compare(Marker o, Marker t) {
        Boolean longitude = o.getPosition().longitude < t.getPosition().longitude;
        Boolean latitude = o.getPosition().latitude > t.getPosition().latitude;

        if(longitude){
            return 0;
        }

        return 1;
    }
}
