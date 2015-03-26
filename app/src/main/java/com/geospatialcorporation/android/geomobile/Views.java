package com.geospatialcorporation.android.geomobile;

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
