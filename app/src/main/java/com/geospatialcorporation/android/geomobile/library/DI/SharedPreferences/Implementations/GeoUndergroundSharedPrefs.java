package com.geospatialcorporation.android.geomobile.library.DI.SharedPreferences.Implementations;

import android.content.Context;

import com.geospatialcorporation.android.geomobile.application;
import com.geospatialcorporation.android.geomobile.library.DI.SharedPreferences.GeoSharedPrefsBase;


public class GeoUndergroundSharedPrefs extends GeoSharedPrefsBase {


    public GeoUndergroundSharedPrefs(){
        super(application.getAppContext().getSharedPreferences("GeoUndergroundPrefs", Context.MODE_PRIVATE));
    }
}
