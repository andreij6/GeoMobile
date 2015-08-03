package com.geospatialcorporation.android.geomobile.library.helpers.converter;

import java.util.List;

public interface ICoordinateConverter {
    String convert(double position, List<String> pos_neg);

    List<String> getLatitiude_PosNeg();
    List<String> getLongitude_PosNeg();
}
