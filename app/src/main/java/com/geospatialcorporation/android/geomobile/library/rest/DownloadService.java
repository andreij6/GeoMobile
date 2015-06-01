package com.geospatialcorporation.android.geomobile.library.rest;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.geospatialcorporation.android.geomobile.application;
import com.geospatialcorporation.android.geomobile.models.Library.Document;

import java.io.File;

public class DownloadService {
    private final String TAG = DownloadService.class.getSimpleName();

    public DownloadService(int docId, String docName) {
        Context appContext = application.getAppContext();

        String dir = Environment.DIRECTORY_DOWNLOADS;
        dir += "/geomobile";
        File fileDir = new File(dir);
        if (!fileDir.isDirectory()) {
            fileDir.mkdir();
        }

        // Download File
        DownloadManager.Request request =
                new DownloadManager.Request(Uri.parse("https://dev.geounderground.com/API/Documents/" + docId + "/File"));

        request.addRequestHeader("Authorization", "WebToken " + application.getAuthToken());
        request.setDescription(docName);
        request.setTitle(docName);

        // in order for this if to run, you must use the android 3.2 to compile your app
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            request.allowScanningByMediaScanner();
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        }

        request.setDestinationInExternalPublicDir(dir, docName);

        // get download service and enqueue file
        DownloadManager manager = (DownloadManager) appContext.getSystemService(appContext.DOWNLOAD_SERVICE);
        manager.enqueue(request);

        Toast.makeText(application.getAppContext(), "Downloading: " + docName + " Check notification for details.", Toast.LENGTH_LONG).show();
    }
}
