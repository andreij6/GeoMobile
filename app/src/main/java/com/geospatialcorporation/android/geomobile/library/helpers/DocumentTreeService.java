package com.geospatialcorporation.android.geomobile.library.helpers;

import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.widget.Toast;

import com.geospatialcorporation.android.geomobile.application;
import com.geospatialcorporation.android.geomobile.library.rest.DocumentService;
import com.geospatialcorporation.android.geomobile.models.Folders.Folder;
import com.geospatialcorporation.android.geomobile.models.Library.DocumentCreateResponse;

import java.io.File;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedFile;

/**
 * Created by andre on 6/2/2015.
 */
public class DocumentTreeService {

    DocumentService Service;

    public DocumentTreeService(){
        Service = application.getRestAdapter().create(DocumentService.class);
    }

    public void SendDocument(Folder currentFolder, Uri data) {
        TypedFile typedFile = new TypedFile("multipart/form-data", new File(data.getPath()));
        Service.create(currentFolder.getId(), typedFile, new Callback<DocumentCreateResponse>() {
            @Override
            public void success(DocumentCreateResponse documentCreateResponse, Response response) {

                Toast.makeText(application.getAppContext(), "Success!", Toast.LENGTH_SHORT).show();
                Toast.makeText(application.getAppContext(), "Pull Down Refresh", Toast.LENGTH_LONG).show();
            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(application.getAppContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void SendTakenImage(Folder currentFolder, Uri data) {
        final TypedFile t = new TypedFile("image/jpg", new File(data.getPath()));
        final String path = data.getPath();

        SendImage(currentFolder, t, path);
    }

    public void SendPickedImage(Folder currentFolder, Uri data) {
        String actualPath = getRealPathFromUri(data);
        String mimeType = getMimeTypeOfFile(actualPath);

        final TypedFile t = new TypedFile(mimeType, new File(actualPath));

        SendImage(currentFolder, t, actualPath);
    }

    private void SendImage(Folder currentFolder, final TypedFile t, final String path) {
        Service.create(currentFolder.getId(), t, new Callback<DocumentCreateResponse>() {
            @Override
            public void success(DocumentCreateResponse documentCreateResponse, Response response) {

                Toast.makeText(application.getAppContext(), "Success!", Toast.LENGTH_SHORT).show();
                Toast.makeText(application.getAppContext(), "Pull Down Refresh", Toast.LENGTH_LONG).show();
            }

            @Override
            public void failure(RetrofitError error) {

                Toast.makeText(application.getAppContext(), "Name: " + t.fileName(), Toast.LENGTH_LONG).show();
                Toast.makeText(application.getAppContext(), "Path: " + path, Toast.LENGTH_LONG).show();
                Toast.makeText(application.getAppContext(), error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    public String getMimeTypeOfFile(String pathName) {
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(pathName, opt);
        return opt.outMimeType;
    }

    public static String getRealPathFromUri(Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = { MediaStore.Images.Media.DATA };
            cursor = application.getAppContext().getContentResolver().query(contentUri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }
}
