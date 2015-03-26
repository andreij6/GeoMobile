package com.geospatialcorporation.android.geomobile;

public enum EndPoints {
    API("API/"),
    Plugins("Plugins/");

    public final String value;

    private EndPoints(String value) {
        this.value = value;
    }
}
