package com.geospatialcorporation.android.geomobile.library.helpers.converter;

import java.util.Arrays;
import java.util.List;

public class DMSCoordinateConverter implements ICoordinateConverter {

    @Override
    public String convert(double position, List<String> pos_neg) {
        double dd = Math.abs(position);

        int d = (int)dd;  // Truncate the decimals
        double t1 = (dd - d) * 60;
        int m = (int)t1;
        double s = (t1 - m) * 60;

        String degrees = d + "";
        String degreesWithSymbol = degrees + (char) 0x00B0;

        return degreesWithSymbol + " " + m + "' " + String.format("%.4f", s) + "\" " + getDirection(position, pos_neg.get(0), pos_neg.get(1));
    }

    @Override
    public List<String> getLatitiude_PosNeg() {
        return Arrays.asList("N", "S");
    }

    @Override
    public List<String> getLongitude_PosNeg() {
        return Arrays.asList("E", "W");
    }


    protected String getDirection(double dd, String pos, String neg) {
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
