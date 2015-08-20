package com.geospatialcorporation.android.geomobile.library.DI.FeatureWindow;

import com.geospatialcorporation.android.geomobile.library.DI.FeatureWindow.Implementations.FeatureWindowDataParser;
import com.geospatialcorporation.android.geomobile.library.helpers.converter.Implementations.DMSCoordinateConverter;

import dagger.Module;
import dagger.Provides;

@Module
public class FeatureWindowModule {
    @Provides
    IFeatureWindowDataParser provideDataParser(){ return new FeatureWindowDataParser(new DMSCoordinateConverter()); }
}
