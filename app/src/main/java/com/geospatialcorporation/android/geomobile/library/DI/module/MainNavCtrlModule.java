package com.geospatialcorporation.android.geomobile.library.DI.module;

import com.geospatialcorporation.android.geomobile.library.MainNavCtrl;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class MainNavCtrlModule {

    @Provides @Singleton
    MainNavCtrl provideMainNavCtrl(){ return new MainNavCtrl(); }
}
