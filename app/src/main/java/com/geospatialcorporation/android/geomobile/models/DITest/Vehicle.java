package com.geospatialcorporation.android.geomobile.models.DITest;

import javax.inject.Inject;

/**
 * Created by andre on 7/22/2015.
 */
public class Vehicle {
    private Motor motor;

    @Inject
    public Vehicle(Motor motor){
        this.motor = motor;
    }

    public void increaseSpeed(int value){
        motor.accelerate(value);
    }

    public void stop(){
        motor.brake();
    }

    public int getSpeed(){
        return motor.getRpm();
    }
}