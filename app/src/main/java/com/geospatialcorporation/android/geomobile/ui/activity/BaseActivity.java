package com.geospatialcorporation.android.geomobile.ui.activity;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;

import com.geospatialcorporation.android.geomobile.application;
import com.geospatialcorporation.android.geomobile.di.component.ActivityComponent;
import com.geospatialcorporation.android.geomobile.di.component.DaggerActivityComponent;
import com.path.android.jobqueue.TagConstraint;

import java.util.UUID;

public class BaseActivity extends AppCompatActivity {

    private ActivityComponent mComponent;
    private String mSessionId;

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        //mComponent = DaggerActivityComponent.builder()
        //                .appComponent(getApp().getAppComponent())
        //                .build();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mSessionId = UUID.randomUUID().toString();
    }

    @Override
    protected void onStop() {
        super.onStop();

        //getComponent()
        //        .jobManager()
        //        .cancelJobsInBackground(
        //                null, TagConstraint.ALL, mSessionId);
    }

    //region getters
    public application getApp() {
        return (application)getApplicationContext();
    }

    public ActivityComponent getComponent(){
        return  mComponent;
    }

    public String getSessionId(){
        return mSessionId;
    }
    //endregion
}
