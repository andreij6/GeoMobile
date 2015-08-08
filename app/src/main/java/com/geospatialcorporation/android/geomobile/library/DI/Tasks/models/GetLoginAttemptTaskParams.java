package com.geospatialcorporation.android.geomobile.library.DI.Tasks.models;

public class GetLoginAttemptTaskParams {
    String mAttemptId;

    public GetLoginAttemptTaskParams(String attemptId){
        mAttemptId = attemptId;
    }

    public String getAttemptId() { return mAttemptId; }
}
