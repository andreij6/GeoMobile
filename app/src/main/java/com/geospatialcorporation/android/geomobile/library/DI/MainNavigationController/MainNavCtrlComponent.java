package com.geospatialcorporation.android.geomobile.library.DI.MainNavigationController;

import com.geospatialcorporation.android.geomobile.library.DI.MainNavigationController.Implementations.MainNavCtrl;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {MainNavCtrlModule.class})
public interface MainNavCtrlComponent {

    MainNavCtrl provideMainNavCtrl();
}
