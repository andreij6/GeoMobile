package com.geospatialcorporation.android.geomobile.di.module;

import android.content.Context;

import com.geospatialcorporation.android.geomobile.application;
import com.geospatialcorporation.android.geomobile.job.BaseJob;
import com.geospatialcorporation.android.geomobile.library.util.L;
import com.path.android.jobqueue.Job;
import com.path.android.jobqueue.JobManager;
import com.path.android.jobqueue.config.Configuration;
import com.path.android.jobqueue.di.DependencyInjector;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import de.greenrobot.event.EventBus;

@Module
public class ApplicationModule {

    private final application mApp;

    public ApplicationModule(application app){
        mApp = app;
    }

    @Provides
    @Singleton
    public Context appContext(){
        return mApp;
    }

    @Provides
    @Singleton
    public EventBus eventBus(){
        return new EventBus();
    }

    @Provides
    @Singleton
    public JobManager jobManager() {
        Configuration config = new Configuration.Builder(mApp)
                .consumerKeepAlive(45)
                .maxConsumerCount(3)
                .minConsumerCount(1)
                .customLogger(L.getJobLogger())
                .injector(new DependencyInjector() {
                    @Override
                    public void inject(Job job) {
                        if (job instanceof BaseJob) {
                            ((BaseJob) job).inject(mApp.getAppComponent());
                        }
                    }
                })
                .build();
        return new JobManager(mApp, config);
    }
}
