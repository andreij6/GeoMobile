package com.geospatialcorporation.android.geomobile.library.helpers;

import android.content.Context;
import android.support.v4.app.FragmentManager;

import com.geospatialcorporation.android.geomobile.ui.fragments.dialogs.BookmarksDialogFragment;
import com.google.android.gms.maps.GoogleMap;
import com.melnykov.fab.FloatingActionButton;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

public class GeoDialogHelper {

    public static void showBookmarks(Context context, FragmentManager fm, FloatingActionButton save, FloatingActionButton close, SlidingUpPanelLayout panel, GoogleMap m) {
        BookmarksDialogFragment b = new BookmarksDialogFragment();
        b.init(context, save, close, panel, m, fm);
        b.show(fm, "Bookmarks");
    }
}
