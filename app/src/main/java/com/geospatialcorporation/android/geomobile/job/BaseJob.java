package com.geospatialcorporation.android.geomobile.job;

import android.support.annotation.IntDef;

import com.geospatialcorporation.android.geomobile.di.component.AppComponent;
import com.path.android.jobqueue.Job;
import com.path.android.jobqueue.Params;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

abstract public class BaseJob extends Job {


    @Retention(RetentionPolicy.SOURCE)
    @IntDef({UI_HIGH, BACKGROUND})
    public @interface Priority {


    }


    public static final int UI_HIGH = 10;
    public static final int BACKGROUND = 1;


    public BaseJob(Params params) {
        super(params);
    }


    protected boolean shouldRetry(Throwable throwable) {
        if (throwable instanceof NetworkException) {
            NetworkException exception = (NetworkException) throwable;
            return exception.shouldRetry();
        }
        return true;
    }


    public void inject(AppComponent appComponent) {


    }
}