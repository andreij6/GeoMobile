package com.geospatialcorporation.android.geomobile.library.helpers;

import android.text.format.DateUtils;

import com.geospatialcorporation.android.geomobile.application;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class DateTimeFormatter {
    public static String format(String dateString) {
        // from the server
        SimpleDateFormat parser = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS", application.getLocale());
        // to the user
        SimpleDateFormat formatter = new SimpleDateFormat("MMM dd, yyyy h:mm a", application.getLocale());

        String date = "";

        try {
            Date dateObj = parser.parse(dateString);
            date = formatter.format(dateObj);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return date;
    }
}
