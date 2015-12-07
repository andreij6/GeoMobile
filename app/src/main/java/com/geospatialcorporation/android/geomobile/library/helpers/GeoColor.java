package com.geospatialcorporation.android.geomobile.library.helpers;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import com.geospatialcorporation.android.geomobile.application;

public class GeoColor {

    public int parseColor(String geocolor){
        String color = geocolor.substring(0, 6);
        String alpha = geocolor.substring(6);

        String finalColor = String.format("#%s%s", alpha, color);

        return Color.parseColor(finalColor);
    }

    public Drawable changeColor(Drawable drawable, int desiredColor){
        Bitmap iconBitmap = ((BitmapDrawable) drawable).getBitmap();

        Bitmap coloredBitmap = new GeoColor().changeColor(iconBitmap, desiredColor);

        return new BitmapDrawable(application.getAppContext().getResources(), coloredBitmap);
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
