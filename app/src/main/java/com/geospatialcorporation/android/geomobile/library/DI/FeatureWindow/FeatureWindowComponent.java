package com.geospatialcorporation.android.geomobile.library.DI.FeatureWindow;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component( modules = {FeatureWindowModule.class})
public interface FeatureWindowComponent {

    IFeatureWindowDataParser provideDataParser();
}
