package com.geospatialcorporation.android.geomobile.library.services;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.widget.Toast;

import com.geospatialcorporation.android.geomobile.application;

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
        String downloadUrl = application.getDomain() + "/API/Documents/" + docId + "/File";

        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(downloadUrl));

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
        DownloadManager manager = (DownloadManager) appContext.getSystemService(Context.DOWNLOAD_SERVICE);
        manager.enqueue(request);

        Toast.makeText(application.getAppContext(), "Downloading: " + docName + " Check notification for details.", Toast.LENGTH_LONG).show();
    }
}
