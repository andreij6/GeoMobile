package com.geospatialcorporation.android.geomobile.library.DI.SharedPreferences;


import com.geospatialcorporation.android.geomobile.library.DI.SharedPreferences.Implementations.GeoUndergroundSharedPrefs;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {GeoSharedPrefsModule.class})
public interface GeoSharedPrefsComponent {
    GeoUndergroundSharedPrefs provideGeoSharedPrefs();
}
