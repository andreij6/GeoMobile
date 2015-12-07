package com.geospatialcorporation.android.geomobile.library.DI.ErrorHandler;

import com.geospatialcorporation.android.geomobile.library.DI.ErrorHandler.Implementations.GeoErrorHandler;
import com.geospatialcorporation.android.geomobile.library.DI.ErrorHandler.Interfaces.IGeoErrorHandler;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class ErrorsModule {

    @Provides @Singleton
    IGeoErrorHandler provideErrorHandler(){ return new GeoErrorHandler(); }
}
