package com.geospatialcorporation.android.geomobile.library.DI.Tasks.models;

import android.widget.EditText;

import com.geospatialcorporation.android.geomobile.library.DI.Tasks.Interfaces.IUserLoginTask;
import com.geospatialcorporation.android.geomobile.ui.activity.LoginActivity;

public class LoginUIModel {
    private EditText mPasswordView;
    private LoginActivity mContext;
    private IUserLoginTask mTask;

    public LoginUIModel(EditText editText, LoginActivity activity, IUserLoginTask task){
        mContext = activity;
        mPasswordView = editText;
        mTask = task;

    }

    public EditText getPasswordView() {
        return mPasswordView;
    }

    public LoginActivity getContext() {
        return mContext;
    }

    public IUserLoginTask getTask() { return mTask; }
}
