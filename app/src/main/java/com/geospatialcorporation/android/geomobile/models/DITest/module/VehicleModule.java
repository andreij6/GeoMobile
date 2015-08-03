package com.geospatialcorporation.android.geomobile.models.DITest.module;

import com.geospatialcorporation.android.geomobile.models.DITest.Motor;
import com.geospatialcorporation.android.geomobile.models.DITest.Vehicle;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by andre on 7/22/2015.
 */
@Module
public class VehicleModule {
    @Provides @Singleton
    Motor provideMotor(){
        return new Motor();
    }

    @Provides @Singleton
    Vehicle provideVehicle(){
        return new Vehicle(new Motor());
    }
}
