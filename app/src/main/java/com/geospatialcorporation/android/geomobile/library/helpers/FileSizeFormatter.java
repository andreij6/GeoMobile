package com.geospatialcorporation.android.geomobile.library.helpers;

import java.text.NumberFormat;
import java.util.Locale;

public class FileSizeFormatter {

    public static String format(String sizeString){
        int size = Integer.parseInt(sizeString);

        String strSize = "";
        long kb = 1024; // Kilobyte
        long mb = 1024 * kb; // Megabyte
        long gb = 1024 * mb; // Gigabyte

        Locale currentLocale = Locale.getDefault();
        NumberFormat formatter = NumberFormat.getNumberInstance(currentLocale);
        formatter.setMaximumFractionDigits(2);
        formatter.setMinimumFractionDigits(2);

        if (size < kb) {
            strSize = size + " bytes";
        } else if (size < mb) {
            strSize = formatter.format(size / kb) + " KB";
        } else if (size < gb){
            strSize = formatter.format(size / mb) + " MB";
        } else {
            strSize = formatter.format(size / gb) + " GB";
        }

        return strSize;
    }
}
