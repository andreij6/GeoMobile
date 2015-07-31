package com.geospatialcorporation.android.geomobile;

import android.app.Application;

import com.geospatialcorporation.android.geomobile.library.DI.Analytics.AnalyticsComponent;
import com.geospatialcorporation.android.geomobile.library.DI.Analytics.DaggerAnalyticsComponent;
import com.geospatialcorporation.android.geomobile.library.DI.Authentication.AuthenticationComponent;
import com.geospatialcorporation.android.geomobile.library.DI.Authentication.DaggerAuthenticationComponent;
import com.geospatialcorporation.android.geomobile.library.DI.ErrorHandler.DaggerErrorsComponent;
import com.geospatialcorporation.android.geomobile.library.DI.ErrorHandler.ErrorsComponent;
import com.geospatialcorporation.android.geomobile.library.DI.SharedPreferences.DaggerGeoSharedPrefsComponent;
import com.geospatialcorporation.android.geomobile.library.DI.SharedPreferences.GeoSharedPrefsComponent;

public abstract class applicationDIBase extends Application {


    public static ErrorsComponent getErrorsComponent() {
        return DaggerErrorsComponent.builder().build();
    }


    public static AuthenticationComponent getGoogleAuthComponent() {
        return DaggerAuthenticationComponent.builder().build();
    }

    public static GeoSharedPrefsComponent getGeoSharedPrefsComponent() {
        return DaggerGeoSharedPrefsComponent.builder().build();
    }

    public static AnalyticsComponent getAnalyticsComponent() {
        return DaggerAnalyticsComponent.builder().build();
    }
}
