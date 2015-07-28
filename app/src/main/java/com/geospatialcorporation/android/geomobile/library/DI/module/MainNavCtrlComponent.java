package com.geospatialcorporation.android.geomobile.library.DI.module;

import com.geospatialcorporation.android.geomobile.library.MainNavCtrl;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {MainNavCtrlModule.class})
public interface MainNavCtrlComponent {

    MainNavCtrl provideMainNavCtrl();
}
