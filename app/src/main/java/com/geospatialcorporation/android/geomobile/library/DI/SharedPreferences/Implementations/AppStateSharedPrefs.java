package com.geospatialcorporation.android.geomobile.library.DI.SharedPreferences.Implementations;

import android.content.Context;

import com.geospatialcorporation.android.geomobile.application;
import com.geospatialcorporation.android.geomobile.library.DI.SharedPreferences.GeoSharedPrefsBase;

public class AppStateSharedPrefs extends GeoSharedPrefsBase {

    public AppStateSharedPrefs(){
        super(application.getAppContext().getSharedPreferences("AppStatePrefs", Context.MODE_PRIVATE));
    }
}
