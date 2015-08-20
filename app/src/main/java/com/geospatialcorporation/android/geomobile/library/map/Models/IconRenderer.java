package com.geospatialcorporation.android.geomobile.library.map.Models;

import android.content.Context;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;

public class IconRenderer extends DefaultClusterRenderer<GeoClusterMarker> {

    public IconRenderer(Context context, GoogleMap map, ClusterManager<GeoClusterMarker> clusterManager) {
        super(context, map, clusterManager);
    }

    @Override
    protected void onBeforeClusterItemRendered(GeoClusterMarker item, MarkerOptions markerOptions) {
        markerOptions.icon(item.getMarker().getIcon());
        markerOptions.title(item.getId());
        markerOptions.anchor(0.5f, 0.5f);
    }

}
