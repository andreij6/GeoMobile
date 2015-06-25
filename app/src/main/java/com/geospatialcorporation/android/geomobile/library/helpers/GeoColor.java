package com.geospatialcorporation.android.geomobile.library.helpers;

import android.graphics.Color;

/**
 * Created by andre on 6/25/2015.
 */
public class GeoColor {

    public int parseColor(String geocolor){
        String color = geocolor.substring(0, 6);
        String alpha = geocolor.substring(6);

        String finalColor = String.format("#%s%s", alpha, color);

        return Color.parseColor(finalColor);
    }
}
