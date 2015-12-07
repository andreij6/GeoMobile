package com.geospatialcorporation.android.geomobile.library.DI.Analytics.Interfaces;

import android.app.Activity;

import com.geospatialcorporation.android.geomobile.library.DI.Analytics.Models.AnalyticEvent;

public interface IGeoAnalytics<T extends  AnalyticEvent> {

    void trackClick(T event);

    void trackScreen(T event);

    Thread.UncaughtExceptionHandler getExceptionReporter(Thread.UncaughtExceptionHandler defaultHandler);

    void onStop(Activity activity);

    void sendException(Exception e);
}
