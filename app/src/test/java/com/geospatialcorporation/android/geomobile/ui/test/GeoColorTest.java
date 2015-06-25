package com.geospatialcorporation.android.geomobile.ui.test;

import android.graphics.Color;

import com.geospatialcorporation.android.geomobile.library.helpers.GeoColor;
import static org.junit.Assert.*;

import org.junit.Test;

/**
 * Created by andre on 6/25/2015.
 */
public class GeoColorTest {

    @Test
    public void parseColorTest_TypicalGeoUndergroundColorFormat(){
        String geoFormat = "0F279EFF";

        int actual = new GeoColor().parseColor(geoFormat);

        int expected = Color.parseColor("FF0F279E");

        assertTrue("GeoColor did now generate the same color Id.", actual == expected);
    }
}
