package com.geospatialcorporation.android.geomobile.library.DI.Map.Models;

import com.google.android.gms.maps.GoogleMap;

public class MapStateSaveRequest {

    //region G's & S's
    public GoogleMap getMap() {
        return mMap;
    }

    public void setMap(GoogleMap map) {
        mMap = map;
    }
    //endregion

    private GoogleMap mMap;

    public MapStateSaveRequest(GoogleMap map){
        mMap = map;
    }
}
