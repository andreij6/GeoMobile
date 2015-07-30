package com.geospatialcorporation.android.geomobile.library.DI.Analytics;

import com.geospatialcorporation.android.geomobile.library.DI.Analytics.Implementations.GeoGoogleAnalytics;
import com.geospatialcorporation.android.geomobile.library.DI.Analytics.Interfaces.IGeoAnalytics;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class AnalyticsModule {

    @Provides @Singleton
    IGeoAnalytics provideGeoAnalytics(){ return new GeoGoogleAnalytics(); }

}
