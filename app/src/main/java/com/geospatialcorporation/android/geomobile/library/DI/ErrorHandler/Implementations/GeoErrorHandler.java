package com.geospatialcorporation.android.geomobile.library.DI.ErrorHandler.Implementations;

import android.app.Activity;
import android.content.Intent;

import com.geospatialcorporation.android.geomobile.application;
import com.geospatialcorporation.android.geomobile.library.DI.Analytics.Interfaces.IGeoAnalytics;
import com.geospatialcorporation.android.geomobile.library.DI.ErrorHandler.Interfaces.IGeoErrorHandler;
import com.geospatialcorporation.android.geomobile.ui.activity.SplashScreenActivity;

import java.io.PrintWriter;
import java.io.StringWriter;

public class GeoErrorHandler implements IGeoErrorHandler {

    IGeoAnalytics mAnalytics;

    public GeoErrorHandler(){
        mAnalytics = application.getAnalyticsComponent().provideGeoAnalytics();
    }

    @Override
    public Thread.UncaughtExceptionHandler UncaughtExceptionHandler(final Activity currentActivity) {
        //Option: Custom Handler Or Default
        //Thread.UncaughtExceptionHandler defaultHandler = Thread.getDefaultUncaughtExceptionHandler();

        Thread.UncaughtExceptionHandler handler = new Thread.UncaughtExceptionHandler(){

            @Override
            public void uncaughtException(Thread thread, Throwable ex) {
                StringWriter _stackTrace = new StringWriter();
                ex.printStackTrace(new PrintWriter(_stackTrace));

                mAnalytics.sendException(new Exception(ex));

                Intent _intent = new Intent(currentActivity, SplashScreenActivity.class);

                _intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Here is the solution

                _intent.putExtra("error", _stackTrace.toString());
                currentActivity.startActivity(_intent);

                System.exit(0);
            }
        };


        return handler;
    }
}
