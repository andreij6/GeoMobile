package com.geospatialcorporation.android.geomobile.library.util;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import java.util.List;

public class GeoPolyUtil {

    public static boolean BoundsIntersect(List<LatLng> points, LatLngBounds bounds){
        LatLngBounds.Builder boundsBuilder = new LatLngBounds.Builder();

        for(LatLng point : points) {
            boundsBuilder.include(point);
        }

        LatLngBounds polyBounds = boundsBuilder.build();

        //check to see if any of the edges are contained in either bounds
        if(BoundsContainsBounds(bounds, polyBounds) || BoundsContainsBounds(polyBounds, bounds)){
            return true;
        } else {
            return false;
        }
    }

    private static boolean BoundsContainsBounds(LatLngBounds container, LatLngBounds within) {
        if(container.contains(within.getCenter())
                || container.contains(within.southwest)
                || container.contains(within.northeast)
                || container.contains(getNorthWest(within))
                || container.contains(getSouthEast(within))
                )
        {
            return true;
        }

        return false;
    }

    private static LatLng getSouthEast(LatLngBounds within) {
        return new LatLng(within.southwest.latitude, within.northeast.longitude);
    }

    private static LatLng getNorthWest(LatLngBounds within) {
        return new LatLng(within.northeast.latitude, within.southwest.longitude);
    }
}
