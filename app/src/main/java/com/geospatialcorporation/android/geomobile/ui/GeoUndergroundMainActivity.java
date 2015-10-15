package com.geospatialcorporation.android.geomobile.ui;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.FrameLayout;

import com.geospatialcorporation.android.geomobile.R;
import com.geospatialcorporation.android.geomobile.application;
import com.geospatialcorporation.android.geomobile.library.DI.Analytics.Interfaces.IGeoAnalytics;
import com.geospatialcorporation.android.geomobile.library.DI.ErrorHandler.Interfaces.IGeoErrorHandler;

import butterknife.Bind;
import butterknife.ButterKnife;

public abstract class GeoUndergroundMainActivity extends ActionBarActivity {

    protected IGeoAnalytics mAnalytics;
    protected Boolean mIsAdmin;
    protected IGeoErrorHandler mErrorHandler;
    @Bind(R.id.info_frame) FrameLayout mInfoFrame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResource());

        ButterKnife.bind(this);

        //region Property Sets
        mAnalytics = application.getAnalyticsComponent().provideGeoAnalytics();
        mIsAdmin = application.getIsAdminUser();
        mErrorHandler = application.getErrorsComponent().provideErrorHandler();
        Thread.setDefaultUncaughtExceptionHandler(mErrorHandler.UncaughtExceptionHandler(this));
        //endregion

    }

    /** Provides the XML Layout Resource Id of the Activity */
    public abstract int getLayoutResource();
}
