package com.geospatialcorporation.android.geomobile.library.helpers;

import android.content.Context;

import com.geospatialcorporation.android.geomobile.R;
import com.geospatialcorporation.android.geomobile.application;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

/**
 * Created by andre on 6/23/2015.
 */
public class AnalyticsHelper {

    Tracker mTracker;
    Context mContext;

    public AnalyticsHelper(){
        mTracker = application.tracker();
        mContext = application.getAppContext();
    }

    public void sendEvent(int category, int action, int label){

        mTracker.send(new HitBuilders
                .EventBuilder(mContext.getString(category), mContext.getString(action))
                .setLabel(mContext.getString(label))
                .build());

    }

    public void sendClickEvent(int label) {
        mTracker.send(new HitBuilders
                .EventBuilder(mContext.getString(R.string.ui), mContext.getString(R.string.clicked))
                .setLabel(mContext.getString(label))
                .build());
    }

    public void sendScreenName(int ScreenName){
        mTracker.setScreenName(mContext.getString(ScreenName));
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
    }
}
