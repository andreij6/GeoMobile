package com.geospatialcorporation.android.geomobile.library.DI.MainNavigationController.Implementations;

import android.content.Intent;
import android.support.v4.app.Fragment;

import com.geospatialcorporation.android.geomobile.application;
import com.geospatialcorporation.android.geomobile.ui.SubscriptionSelectorActivity;
import com.geospatialcorporation.android.geomobile.ui.LoginActivity;
import com.geospatialcorporation.android.geomobile.ui.MainActivity;
import com.geospatialcorporation.android.geomobile.ui.fragments.GoogleMapFragment;
import com.geospatialcorporation.android.geomobile.ui.fragments.tree_fragments.AccountFragment;
import com.geospatialcorporation.android.geomobile.ui.fragments.tree_fragments.LibraryFragment;
import com.google.android.gms.maps.GoogleMap;

public class MainNavCtrl {

    public Fragment setAdminView(MainActivity mainActivity, GoogleMap map, GoogleMapFragment mapFragment, int position) {
        Fragment currentFragment = application.getMainActivity().getContentFragment();

        switch (position) {
            case ViewConstants.HEADER:
            case ViewConstants.MAP:
                if(currentFragment instanceof GoogleMapFragment){
                    return null;
                } else {
                    if (map == null) {
                        mapFragment = application.getMapFragment();
                    }
                    return mapFragment;
                }
            case ViewConstants.LIBRARY:
                if(currentFragment instanceof LibraryFragment){
                    return null;
                }
                return new LibraryFragment();
            case ViewConstants.ACCOUNT:
                if(currentFragment instanceof AccountFragment){
                    return null;
                }
                return new AccountFragment();
            case ViewConstants.ADMIN_CLIENTS:
                mainActivity.startActivity(new Intent(mainActivity, SubscriptionSelectorActivity.class));
                mainActivity.finish();
                break;
            default:
                return null;
        }

        return mapFragment;
    }

    public Fragment setStandardView(GoogleMap map, GoogleMapFragment mapFragment, int position) {
        switch (position) {
            case ViewConstants.HEADER:
            case ViewConstants.MAP:
                if(map == null) {
                    mapFragment = application.getMapFragment();
                }
                return mapFragment;
            case ViewConstants.LIBRARY:
                return new LibraryFragment();
            case ViewConstants.ACCOUNT:
                return new AccountFragment();
            default:
                return null;
        }
    }

    private class ViewConstants {
        public static final int HEADER = 0;
        public static final int MAP = 1;
        //public static final int LAYER = 0;
        public static final int LIBRARY = 2;
        public static final int ACCOUNT = 3;
        public static final int ADMIN_CLIENTS = 4;

    }
}
