package com.geospatialcorporation.android.geomobile.library.DI.UIHelpers.Implementations;

import com.geospatialcorporation.android.geomobile.application;
import com.geospatialcorporation.android.geomobile.library.DI.UIHelpers.Interfaces.IMainMenuHelper;
import com.geospatialcorporation.android.geomobile.ui.fragments.drawer.MainNavigationDrawerFragment;

public class MainMenuHelper implements IMainMenuHelper {

    MainNavigationDrawerFragment mNavigation;
    Boolean isAdmin;

    public MainMenuHelper(){

        mNavigation = application.getMainActivity().getMainMenuFragment();
        isAdmin = application.getIsAdminUser();
    }

    @Override
    public void syncMenu(Integer relatedMenuItem) {
        if(isAdmin && mNavigation != null) {
            mNavigation.selectItem(relatedMenuItem);
        }
    }
}
