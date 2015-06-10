package com.geospatialcorporation.android.geomobile.models.Query.point;

import com.google.android.gms.maps.model.Marker;

/**
 * Created by andre on 6/4/2015.
 */
public class Min extends Point{

    public Min(Marker marker){
        super(marker.getPosition().latitude, marker.getPosition().longitude);
    }

}
