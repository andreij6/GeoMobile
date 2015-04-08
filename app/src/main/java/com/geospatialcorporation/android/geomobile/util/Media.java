package com.geospatialcorporation.android.geomobile.util;

import com.squareup.okhttp.MediaType;

public enum Media {
    MARKDOWN("text/x-markdown; charset=utf-8"),
    JSON("application/json; charset=utf-8"),
    QUALITY_ASSURANCE("multipart/form-data; charset=utf-8");

    public final MediaType value;

    private Media(String value) {
        this.value = MediaType.parse(value);
    }
}
