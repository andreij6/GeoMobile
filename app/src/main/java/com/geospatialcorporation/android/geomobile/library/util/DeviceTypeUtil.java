package com.geospatialcorporation.android.geomobile.library.util;

import android.content.res.Configuration;
import android.content.res.Resources;

public class DeviceTypeUtil {

    public static boolean isTablet(Resources resources) {
        return (resources.getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK)
                >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }
}
