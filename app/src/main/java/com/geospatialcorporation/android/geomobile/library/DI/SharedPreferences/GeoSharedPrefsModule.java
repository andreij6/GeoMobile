package com.geospatialcorporation.android.geomobile.library.DI.SharedPreferences;

import com.geospatialcorporation.android.geomobile.library.DI.SharedPreferences.Implementations.AppStateSharedPrefs;
import com.geospatialcorporation.android.geomobile.library.DI.SharedPreferences.Implementations.GeoUndergroundSharedPrefs;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class GeoSharedPrefsModule {

    @Provides @Singleton
    GeoUndergroundSharedPrefs provideGeoSharedPrefs(){ return new GeoUndergroundSharedPrefs(); }

    @Provides @Singleton
    AppStateSharedPrefs provideAppStateSharedPrefs(){ return new AppStateSharedPrefs(); }

}
