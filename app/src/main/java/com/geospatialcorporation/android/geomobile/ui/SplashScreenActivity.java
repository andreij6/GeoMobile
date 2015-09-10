package com.geospatialcorporation.android.geomobile.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.geospatialcorporation.android.geomobile.R;
import com.geospatialcorporation.android.geomobile.application;
import com.geospatialcorporation.android.geomobile.library.DI.ErrorHandler.Interfaces.IGeoErrorHandler;
import com.geospatialcorporation.android.geomobile.library.DI.Map.Interfaces.ILayerManager;

public class SplashScreenActivity extends Activity {
    // Splash screen timer
    private static int SPLASH_TIME_OUT = 3000;
    IGeoErrorHandler mErrorHandler;
    ILayerManager mLayerManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        mErrorHandler = application.getErrorsComponent().provideErrorHandler();
        mLayerManager = application.getLayerManager();

        Thread.setDefaultUncaughtExceptionHandler(mErrorHandler.UncaughtExceptionHandler());

        new Handler().postDelayed(new Runnable() {
 
            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

            @Override
            public void run() {
                startActivity(new Intent(SplashScreenActivity.this, LoginActivity.class));
            }
        }, SPLASH_TIME_OUT);

        mLayerManager.reset();
    }



}