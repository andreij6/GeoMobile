package com.geospatialcorporation.android.geomobile.library.DI.MainNavigationController.Implementations;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.widget.Toast;

import com.geospatialcorporation.android.geomobile.application;
import com.geospatialcorporation.android.geomobile.ui.ClientSelectorActivity;
import com.geospatialcorporation.android.geomobile.ui.LoginActivity;
import com.geospatialcorporation.android.geomobile.ui.MainActivity;
import com.geospatialcorporation.android.geomobile.ui.fragments.GoogleMapFragment;
import com.geospatialcorporation.android.geomobile.ui.fragments.tree_fragments.AccountFragment;
import com.geospatialcorporation.android.geomobile.ui.fragments.tree_fragments.LibraryFragment;
import com.google.android.gms.maps.GoogleMap;

public class MainNavCtrl {

    public Fragment setAdminView(MainActivity mainActivity, GoogleMap map, GoogleMapFragment mapFragment, int position) {
        switch (position) {
            case ViewConstants.HEADER:
            case ViewConstants.MAP:
                if(map == null){
                    mapFragment = application.getMapFragment();
                }
                return mapFragment;
            case ViewConstants.LIBRARY:
                return new LibraryFragment();
            case ViewConstants.ACCOUNT:
                return new AccountFragment();
            case ViewConstants.ADMIN_CLIENTS:
                mainActivity.startActivity(new Intent(mainActivity, ClientSelectorActivity.class));
                mainActivity.finish();
                break;
            case ViewConstants.LOGOUT_ADMIN:
                application.Logout();
                mainActivity.startActivity(new Intent(mainActivity, LoginActivity.class));
                break;
            default:
                Toast.makeText(application.getAppContext(), "Drawer view not yet implemented.", Toast.LENGTH_LONG).show();
                break;
        }

        return mapFragment;
    }

    public Fragment setStandardView(MainActivity mainActivity, GoogleMap map, GoogleMapFragment mapFragment, int position) {
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
            case ViewConstants.LOGOUT_REGULAR:
                application.Logout();
                mainActivity.startActivity(new Intent(mainActivity, LoginActivity.class));
                break;
            default:
                Toast.makeText(application.getAppContext(), "Drawer view not yet implemented.", Toast.LENGTH_LONG).show();
                break;
        }
        return mapFragment;
    }

    private class ViewConstants {
        public static final int HEADER = 0;
        public static final int MAP = 1;
        //public static final int LAYER = 0;
        public static final int LIBRARY = 2;
        public static final int ACCOUNT = 3;
        public static final int LOGOUT_REGULAR = 4;
        public static final int ADMIN_CLIENTS = 4;
        public static final int LOGOUT_ADMIN = 5;

    }
}
