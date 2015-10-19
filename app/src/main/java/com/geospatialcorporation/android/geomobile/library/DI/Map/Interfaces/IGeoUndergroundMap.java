package com.geospatialcorporation.android.geomobile.library.DI.Map.Interfaces;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageButton;

import com.geospatialcorporation.android.geomobile.library.DI.Map.Implementations.GeoUndergroundMap;
import com.geospatialcorporation.android.geomobile.library.DI.Map.Implementations.IGeoUMap;
import com.geospatialcorporation.android.geomobile.models.Query.map.response.featurewindow.ParcelableFeatureQueryResponse;
import com.geospatialcorporation.android.geomobile.ui.fragments.MapFragments.TabletMapFragment;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;

public interface IGeoUndergroundMap
        extends
        GoogleMap.OnMapLoadedCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        IGeoUMap.Configuration,
        IGeoUMap.LifeCycle, LocationListener

{
    void initializeMap(Bundle savedInstanceState, MapView mapView, TabletMapFragment map);

    void setup(Activity activity);

    void getLocationBtn(ImageButton gpsbtn);

    void clearHighlights();

    boolean validate(ParcelableFeatureQueryResponse response);
}
