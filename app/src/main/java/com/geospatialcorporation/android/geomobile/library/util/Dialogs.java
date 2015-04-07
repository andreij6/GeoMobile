package com.geospatialcorporation.android.geomobile.library.util;

import android.content.Context;

import com.afollestad.materialdialogs.MaterialDialog;
import com.geospatialcorporation.android.geomobile.application;

import org.json.JSONObject;

public class Dialogs {

    public void success(String message) {
        new MaterialDialog.Builder(application.getAppContext())
                .title("Success")
                .content(message)
                .show();
    }

    public void success(String message, Context context) {
        new MaterialDialog.Builder(context)
                .title("Success")
                .content(message)
                .show();
    }

    public void success(JSONObject message) {
        new MaterialDialog.Builder(application.getAppContext())
                .title("Success")
                .content(message.toString())
                .show();
    }

    public void error(String message) {
        new MaterialDialog.Builder(application.getAppContext())
                .title("Error")
                .content(message)
                .show();
    }

    public void message(String message) {
        new MaterialDialog.Builder(application.getAppContext())
                .content(message)
                .show();
    }

    public void message(String message, Context context) {
        new MaterialDialog.Builder(context)
                .content(message)
                .show();
    }
}
