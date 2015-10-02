package com.geospatialcorporation.android.geomobile;

import android.app.Application;
import android.support.multidex.MultiDexApplication;

import com.geospatialcorporation.android.geomobile.library.DI.Analytics.AnalyticsComponent;
import com.geospatialcorporation.android.geomobile.library.DI.Analytics.DaggerAnalyticsComponent;
import com.geospatialcorporation.android.geomobile.library.DI.Authentication.AuthenticationComponent;
import com.geospatialcorporation.android.geomobile.library.DI.Authentication.DaggerAuthenticationComponent;
import com.geospatialcorporation.android.geomobile.library.DI.ErrorHandler.DaggerErrorsComponent;
import com.geospatialcorporation.android.geomobile.library.DI.ErrorHandler.ErrorsComponent;
import com.geospatialcorporation.android.geomobile.library.DI.Map.DaggerMapComponent;
import com.geospatialcorporation.android.geomobile.library.DI.Map.MapComponent;
import com.geospatialcorporation.android.geomobile.library.DI.SharedPreferences.DaggerGeoSharedPrefsComponent;
import com.geospatialcorporation.android.geomobile.library.DI.SharedPreferences.GeoSharedPrefsComponent;
import com.geospatialcorporation.android.geomobile.library.DI.Tasks.DaggerTasksComponent;
import com.geospatialcorporation.android.geomobile.library.DI.Tasks.TasksComponent;
import com.geospatialcorporation.android.geomobile.library.DI.TreeServices.DaggerTreeServiceComponent;
import com.geospatialcorporation.android.geomobile.library.DI.TreeServices.TreeServiceComponent;
import com.geospatialcorporation.android.geomobile.library.DI.UIHelpers.DaggerUIHelperComponent;
import com.geospatialcorporation.android.geomobile.library.DI.UIHelpers.UIHelperComponent;

public abstract class applicationDIBase extends MultiDexApplication {


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

    public static TasksComponent getTasksComponent(){
        return DaggerTasksComponent.builder().build();
    }

    public static UIHelperComponent getUIHelperComponent(){
        return DaggerUIHelperComponent.builder().build();
    }

    public static TreeServiceComponent getTreeServiceComponent(){
        return DaggerTreeServiceComponent.builder().build();
    }

    public static MapComponent getMapComponent(){
        return DaggerMapComponent.builder().build();
    }
}
