package com.geospatialcorporation.android.geomobile.library.DI.ErrorHandler.Interfaces;

import android.app.Activity;

public interface IGeoErrorHandler {

    Thread.UncaughtExceptionHandler UncaughtExceptionHandler(Activity activity);

}
