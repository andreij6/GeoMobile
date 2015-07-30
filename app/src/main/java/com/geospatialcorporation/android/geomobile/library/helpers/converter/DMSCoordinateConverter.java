package com.geospatialcorporation.android.geomobile.library.helpers.converter;

import java.util.Arrays;
import java.util.List;

public class DMSCoordinateConverter implements ICoordinateConverter {

    public static final List<String> LAT = Arrays.asList("N", "S");
    public static final List<String> LONG = Arrays.asList("E", "W");

    @Override
    public String convert(double position, List<String> pos_neg) {
        double dd = Math.abs(position);

        int d = (int)dd;  // Truncate the decimals
        double t1 = (dd - d) * 60;
        int m = (int)t1;
        double s = (t1 - m) * 60;

        return d + " " + m + " " + String.format("%.4f", s) + " " + getDirection(position, pos_neg.get(0), pos_neg.get(1));
    }

    private String getDirection(double dd, String pos, String neg) {
        double direction = Math.signum(dd);

        if(direction == -1){
            return neg;
        }

        if(direction == 1){
            return pos;
        }

        return "";
    }
}
