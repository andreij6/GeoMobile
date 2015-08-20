package com.geospatialcorporation.android.geomobile.library.DI.Map;

import com.geospatialcorporation.android.geomobile.application;
import com.geospatialcorporation.android.geomobile.library.DI.Map.Implementations.MapStateService;
import com.geospatialcorporation.android.geomobile.library.DI.Map.Interfaces.ILayerManager;
import com.geospatialcorporation.android.geomobile.library.DI.Map.Interfaces.IMapStateService;
import com.geospatialcorporation.android.geomobile.library.DI.SharedPreferences.Implementations.GeoUndergroundSharedPrefs;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class MapModule {
    @Provides
    IMapStateService provideMapStateService(){ return new MapStateService(new GeoUndergroundSharedPrefs()); }

    @Provides @Singleton
    ILayerManager provideLayerManager(){ return application.getLayerManager(); }
}
