package com.geospatialcorporation.android.geomobile;

public enum Domains {
    DEVELOPMENT("https://dev.geounderground.com/"),
    PRODUCTION("https://www.geounderground.com/"),
    QUALITY_ASSURANCE("https://qa.geounderground.com/");

    public final String value;

    private Domains(String value) {
        this.value = value;
    }
}
