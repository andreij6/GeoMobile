package com.geospatialcorporation.android.geomobile.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

public class GeoUndergroundDataContract {
    public static final String AUTHORITY = "com.geospatialcorporation.android.geomobile2";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    public static final String PATH_FOLDER = "folder";
    public static final String PATH_LAYER = "layer";
    public static final String PATH_DOCUMENT = "document";
    public static final String PATH_SUBSCRIPTION = "subscription";

    public static final class Folders implements BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_DOCUMENT).build();

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

    public static final class Subscriptions {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_SUBSCRIPTION).build();

        public static final String CONTENT_TYPE =  ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + AUTHORITY + "/" + PATH_SUBSCRIPTION;
    }
}
