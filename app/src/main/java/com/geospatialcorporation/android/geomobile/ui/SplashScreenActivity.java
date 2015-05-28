package com.geospatialcorporation.android.geomobile.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.geospatialcorporation.android.geomobile.R;
import com.geospatialcorporation.android.geomobile.application;

public class SplashScreenActivity extends Activity {
    // Splash screen timer
    private static int SPLASH_TIME_OUT = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash_screen);

        final Intent login = new Intent(SplashScreenActivity.this, LoginActivity.class);
        final Intent main = new Intent(SplashScreenActivity.this, MainActivity.class);

        // TODO: Check for authentication

        Object postDelay = new Handler().postDelayed(new Runnable() {
 
            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity
                if (application.getAuthToken() == null) {
                    startActivity(login);
                } else {
                    startActivity(main);
                }

                // close this activity
                finish();
            }
        }, SPLASH_TIME_OUT);
    }
}