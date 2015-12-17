package com.geospatialcorporation.android.geomobile.di.component;

import com.geospatialcorporation.android.geomobile.di.scope.ActivityScope;
import com.geospatialcorporation.android.geomobile.ui.activity.MainActivity;

import dagger.Component;

@ActivityScope
@Component(dependencies = AppComponent.class)
public interface ActivityComponent extends AppComponent {

    void inject(MainActivity mainActivity);
}
