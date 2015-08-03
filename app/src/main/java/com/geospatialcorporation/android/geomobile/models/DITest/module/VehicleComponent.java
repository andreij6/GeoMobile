package com.geospatialcorporation.android.geomobile.models.DITest.module;

import com.geospatialcorporation.android.geomobile.models.DITest.Vehicle;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by andre on 7/22/2015.
 */

@Singleton
@Component(modules = {VehicleModule.class})
public interface VehicleComponent {

    Vehicle provideVehicle();
}
