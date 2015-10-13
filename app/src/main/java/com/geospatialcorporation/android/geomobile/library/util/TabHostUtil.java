package com.geospatialcorporation.android.geomobile.library.util;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.geospatialcorporation.android.geomobile.R;

public class TabHostUtil {

    public static View createTabView(Context context, int imageSelector) {

        View view = LayoutInflater.from(context).inflate(R.layout.tabs_bg, null);

        ImageView ti = (ImageView) view.findViewById(R.id.tabImage);

        ti.setImageDrawable(ContextCompat.getDrawable(context, imageSelector));

        return view;
    }
}
