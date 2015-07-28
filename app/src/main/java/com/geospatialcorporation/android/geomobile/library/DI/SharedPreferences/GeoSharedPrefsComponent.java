package com.geospatialcorporation.android.geomobile.library.DI.SharedPreferences;


import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {GeoSharedPrefsModule.class})
public interface GeoSharedPrefsComponent {
    IGeoSharedPrefs provideGeoSharedPrefs();
}
