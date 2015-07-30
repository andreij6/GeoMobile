package com.geospatialcorporation.android.geomobile.library.DI.Analytics;

import com.geospatialcorporation.android.geomobile.library.DI.Analytics.Interfaces.IGeoAnalytics;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = AnalyticsModule.class)
public interface AnalyticsComponent {

    IGeoAnalytics provideGeoAnalytics();
}
