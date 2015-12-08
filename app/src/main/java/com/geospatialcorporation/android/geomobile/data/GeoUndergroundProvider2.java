package com.geospatialcorporation.android.geomobile.data;


import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.Nullable;

import com.geospatialcorporation.android.geomobile.application;
import com.geospatialcorporation.android.geomobile.ui.MainActivity;
import com.snappydb.DB;
import com.snappydb.DBFactory;
import com.snappydb.SnappydbException;

public class GeoUndergroundProvider2 extends ContentProvider {

    public static final String AUTHORITY = "com.geospatialcorporation.android.geomobile2";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    public static final String PATH_FOLDER = "folder";
    public static final String PATH_LAYER = "layer";
    public static final String PATH_DOCUMENT = "document";

    @Override
    public boolean onCreate() {
        Boolean wasCreated = false;
        try {
            DB snappydb = DBFactory.open(getContext());

            if(snappydb != null){
                wasCreated = true;
            }
        } catch (SnappydbException e) {
            e.printStackTrace();
            wasCreated = false;
        } catch(Exception e){
            wasCreated = false;
        } finally {
            return wasCreated;
        }
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        return null;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }

    public static final class Folders {
        public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + PATH_FOLDER);

        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + AUTHORITY + "/" + PATH_FOLDER;
    }

    public static final class Layers {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_LAYER).build();

        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + AUTHORITY + "/" + PATH_LAYER;
    }

    public static final class Documents {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_DOCUMENT).build();

        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + AUTHORITY + "/" + PATH_DOCUMENT;
    }
}
