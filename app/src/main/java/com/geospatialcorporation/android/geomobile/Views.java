package com.geospatialcorporation.android.geomobile;

/**
 * Created by Jon Shaffer on 2/24/2015.
 */
public enum Views {
    MAP(0),
    LAYERS(1),
    LIBRARY(2),
    ACCOUNT(3),
    LOGOUT(4);

    public final int value;

    private Views(int value) {
        this.value = value;
    }
}
