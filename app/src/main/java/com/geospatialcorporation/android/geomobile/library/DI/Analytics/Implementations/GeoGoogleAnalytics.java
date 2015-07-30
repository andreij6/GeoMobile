package com.geospatialcorporation.android.geomobile.library.DI.Analytics.Implementations;

import android.content.Context;

import com.geospatialcorporation.android.geomobile.application;
import com.geospatialcorporation.android.geomobile.library.DI.Analytics.Interfaces.IGeoAnalytics;
import com.geospatialcorporation.android.geomobile.library.DI.Analytics.Models.GoogleAnalyticEvent;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

public class GeoGoogleAnalytics implements IGeoAnalytics<GoogleAnalyticEvent> {

    Tracker mTracker;
    Context mContext;

    public GeoGoogleAnalytics(){
        mTracker = application.tracker();
        mContext = application.getAppContext();
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
}
