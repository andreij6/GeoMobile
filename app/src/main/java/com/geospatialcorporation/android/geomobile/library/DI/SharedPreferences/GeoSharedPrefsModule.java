package com.geospatialcorporation.android.geomobile.library.DI.SharedPreferences;

import com.geospatialcorporation.android.geomobile.library.DI.SharedPreferences.Implementations.GeoSharedPrefs;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class GeoSharedPrefsModule {

    @Provides @Singleton
    IGeoSharedPrefs provideGeoSharedPrefs(){ return new GeoSharedPrefs(); }

}
