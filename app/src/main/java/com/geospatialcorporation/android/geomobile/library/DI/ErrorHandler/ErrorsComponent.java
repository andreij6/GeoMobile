package com.geospatialcorporation.android.geomobile.library.DI.ErrorHandler;

import com.geospatialcorporation.android.geomobile.library.DI.ErrorHandler.Interfaces.IGeoErrorHandler;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component( modules = ErrorsModule.class)
public interface ErrorsComponent {

    IGeoErrorHandler provideErrorHandler();
}
