package com.geospatialcorporation.android.geomobile.ui;

import android.app.Activity;

import com.geospatialcorporation.android.geomobile.library.DI.Authentication.AuthenticationComponent;
import com.geospatialcorporation.android.geomobile.library.DI.Authentication.DaggerAuthenticationComponent;
import com.geospatialcorporation.android.geomobile.library.DI.SharedPreferences.DaggerGeoSharedPrefsComponent;
import com.geospatialcorporation.android.geomobile.library.DI.SharedPreferences.GeoSharedPrefsComponent;

public abstract class GeoActivityBase extends Activity {

    protected AuthenticationComponent mAuthComponent;
    protected GeoSharedPrefsComponent mSharedPrefsComponent;

    protected void setComponents(){
        mAuthComponent = DaggerAuthenticationComponent.builder().build();
        mSharedPrefsComponent = DaggerGeoSharedPrefsComponent.builder().build();

    }
}
