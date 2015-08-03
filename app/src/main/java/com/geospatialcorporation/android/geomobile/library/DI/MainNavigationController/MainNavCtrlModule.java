package com.geospatialcorporation.android.geomobile.library.DI.MainNavigationController;

import com.geospatialcorporation.android.geomobile.library.DI.MainNavigationController.Implementations.MainNavCtrl;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class MainNavCtrlModule {

    @Provides @Singleton
    MainNavCtrl provideMainNavCtrl(){ return new MainNavCtrl(); }
}
