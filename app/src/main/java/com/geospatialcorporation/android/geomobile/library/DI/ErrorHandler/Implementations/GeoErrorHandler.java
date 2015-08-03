package com.geospatialcorporation.android.geomobile.library.DI.ErrorHandler.Implementations;

import com.geospatialcorporation.android.geomobile.library.DI.Analytics.Interfaces.IGeoAnalytics;
import com.geospatialcorporation.android.geomobile.library.DI.ErrorHandler.Interfaces.IGeoErrorHandler;

public class GeoErrorHandler implements IGeoErrorHandler {

    IGeoAnalytics mAnalytics;

    public GeoErrorHandler(IGeoAnalytics analytics){
        mAnalytics = analytics;
    }

    @Override
    public Thread.UncaughtExceptionHandler UncaughtExceptionHandler() {
        //Option: Custom Handler Or Default
        Thread.UncaughtExceptionHandler defaultHandler = Thread.getDefaultUncaughtExceptionHandler();

        Thread.UncaughtExceptionHandler handler = mAnalytics.getExceptionReporter(defaultHandler);

        return handler;
    }
}
