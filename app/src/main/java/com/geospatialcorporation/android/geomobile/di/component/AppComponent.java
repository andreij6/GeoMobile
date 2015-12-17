package com.geospatialcorporation.android.geomobile.di.component;

import com.geospatialcorporation.android.geomobile.di.module.ApplicationModule;
import com.path.android.jobqueue.JobManager;

import javax.inject.Singleton;

import dagger.Component;
import de.greenrobot.event.EventBus;

@Singleton
@Component(modules = ApplicationModule.class)
public interface AppComponent {
    EventBus eventBus();

    JobManager jobManager();
}
