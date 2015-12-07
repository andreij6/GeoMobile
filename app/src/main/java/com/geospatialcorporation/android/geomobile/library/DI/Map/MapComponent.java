package com.geospatialcorporation.android.geomobile.library.DI.Map;

import com.geospatialcorporation.android.geomobile.library.DI.Map.Interfaces.ILayerManager;
import com.geospatialcorporation.android.geomobile.library.DI.Map.Interfaces.IMapStateService;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = MapModule.class)
public interface MapComponent {
    IMapStateService provideMapStateService();

    ILayerManager provideLayerManager();
}
