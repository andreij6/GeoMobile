package com.geospatialcorporation.android.geomobile.library.map.Models;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.ClusterItem;

import java.util.UUID;

public class GeoClusterMarker implements ClusterItem {

    private MarkerOptions mMarker;
    private String mId;

    public GeoClusterMarker(MarkerOptions options) {
        mMarker = options;
        mId = UUID.randomUUID().toString();
    }

    @Override
    public LatLng getPosition() {
        return mMarker.getPosition();
    }

    public MarkerOptions getMarker() {
        return mMarker;
    }

    public String getId() {
        return mId;
    }
}
