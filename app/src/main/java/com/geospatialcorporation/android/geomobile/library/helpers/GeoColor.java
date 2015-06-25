package com.geospatialcorporation.android.geomobile.library.helpers;

import android.graphics.Bitmap;
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

    public Bitmap changeColor(Bitmap iconBitmap, int fillColor) {
        int height = iconBitmap.getHeight();
        int width = iconBitmap.getWidth();

        float[] srcHSV = new float[3];
        float[] dstHSV = new float[3];

        Bitmap dstBitmap =  Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                int pixel = iconBitmap.getPixel(col, row);
                int alpha = Color.alpha(pixel);

                Color.colorToHSV(pixel, srcHSV);
                Color.colorToHSV(fillColor, dstHSV);

                dstBitmap.setPixel(col, row, Color.HSVToColor(alpha, dstHSV));
            }
        }

        return dstBitmap;
    }
}
