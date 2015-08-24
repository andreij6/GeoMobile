package com.geospatialcorporation.android.geomobile.library.map;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Queue;

public class MapQueue {

    GoogleMap mGoogleMap;
    Queue<MarkerOptions> markerQueue;

    public MapQueue(){

    }

    public void setGoogleMap(GoogleMap map){
        mGoogleMap = map;
    }

    public void addToQueue(MarkerOptions options){



    }

}
