package com.geospatialcorporation.android.geomobile.data;


import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;
import android.support.annotation.Nullable;

import com.geospatialcorporation.android.geomobile.application;
import com.geospatialcorporation.android.geomobile.models.Document.Document;
import com.geospatialcorporation.android.geomobile.ui.MainActivity;
import com.snappydb.DB;
import com.snappydb.DBFactory;
import com.snappydb.SnappydbException;

public class GeoUndergroundProvider2 extends ContentProvider {

    static final int FOLDER = 100;
    static final int LAYER = 200;
    static final int DOCUMENT = 300;
    static final int SUBSCRIPTION = 400;

    private DB mSnappyDB;

    @Override
    public boolean onCreate() {
        Boolean wasCreated = false;
        try {
            mSnappyDB = DBFactory.open(getContext());

            if(mSnappyDB != null){
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
        final int match = sUriMatcher.match(uri);

        switch (match) {
            // Student: Uncomment and fill out these two cases
            case FOLDER:
                return GeoUndergroundDataContract.Folders.CONTENT_TYPE;
            case LAYER:
                return GeoUndergroundDataContract.Layers.CONTENT_TYPE;
            case DOCUMENT:
                return GeoUndergroundDataContract.Documents.CONTENT_TYPE;
            case SUBSCRIPTION:
                return GeoUndergroundDataContract.Subscriptions.CONTENT_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
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

    private static final UriMatcher sUriMatcher = buildUriMatcher();

    private static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = GeoUndergroundDataContract.AUTHORITY;

        // For each type of URI you want to add, create a corresponding code.
        matcher.addURI(authority, GeoUndergroundDataContract.PATH_FOLDER, FOLDER);
        matcher.addURI(authority, GeoUndergroundDataContract.PATH_LAYER, LAYER);
        matcher.addURI(authority, GeoUndergroundDataContract.PATH_DOCUMENT, DOCUMENT);
        matcher.addURI(authority, GeoUndergroundDataContract.PATH_SUBSCRIPTION, SUBSCRIPTION);

        return matcher;
    }
}
