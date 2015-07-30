package com.geospatialcorporation.android.geomobile.library.DI.Analytics.Interfaces;

import com.geospatialcorporation.android.geomobile.library.DI.Analytics.Models.AnalyticEvent;

public interface IGeoAnalytics<T extends  AnalyticEvent> {

    void trackClick(T event);

    void trackScreen(T event);
}
