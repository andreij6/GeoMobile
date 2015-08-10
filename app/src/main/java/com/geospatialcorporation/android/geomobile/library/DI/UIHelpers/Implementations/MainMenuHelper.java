package com.geospatialcorporation.android.geomobile.library.DI.UIHelpers.Implementations;

import com.geospatialcorporation.android.geomobile.application;
import com.geospatialcorporation.android.geomobile.library.DI.UIHelpers.Interfaces.IMainMenuHelper;
import com.geospatialcorporation.android.geomobile.ui.fragments.drawer.MainNavigationDrawerFragment;

public class MainMenuHelper implements IMainMenuHelper {

    MainNavigationDrawerFragment mNavigation;

    public MainMenuHelper(){
        mNavigation = application.getMainActivity().getMainMenuFragment();
    }

    @Override
    public void syncMenu(Integer relatedMenuItem) {
        mNavigation.selectItem(relatedMenuItem);
    }
}
