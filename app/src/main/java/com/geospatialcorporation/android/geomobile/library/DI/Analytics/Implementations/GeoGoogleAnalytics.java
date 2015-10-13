package com.geospatialcorporation.android.geomobile.library.DI.Analytics.Implementations;

import android.app.Activity;
import android.content.Context;

import com.geospatialcorporation.android.geomobile.application;
import com.geospatialcorporation.android.geomobile.library.DI.Analytics.Interfaces.IGeoAnalytics;
import com.geospatialcorporation.android.geomobile.library.DI.Analytics.Models.GoogleAnalyticEvent;
import com.google.android.gms.analytics.ExceptionReporter;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

public class GeoGoogleAnalytics implements IGeoAnalytics<GoogleAnalyticEvent> {

    Tracker mTracker;
    Context mContext;
    GoogleAnalytics mGoogleAnalytics;

    public GeoGoogleAnalytics(){
        mTracker = application.tracker();
        mContext = application.getAppContext();
        mGoogleAnalytics = application.analytics();
    }

    @Override
    public void trackClick(GoogleAnalyticEvent event) {
        mTracker.send(new HitBuilders
                        .EventBuilder(event.getCategory(), event.getAction())
                        .setLabel(event.getLabel())
                        .build());
    }

    @Override
    public void trackScreen(GoogleAnalyticEvent event) {
        mTracker.setScreenName(event.getScreenName());
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
    }

    @Override
    public Thread.UncaughtExceptionHandler getExceptionReporter(Thread.UncaughtExceptionHandler defaultHandler) {
        return new ExceptionReporter(mTracker, defaultHandler, mContext);

    }

    @Override
    public void onStop(Activity activity) {
        mGoogleAnalytics.reportActivityStop(activity);
    }
}
