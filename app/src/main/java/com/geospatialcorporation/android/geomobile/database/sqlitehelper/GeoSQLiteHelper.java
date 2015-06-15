package com.geospatialcorporation.android.geomobile.database.sqlitehelper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

public class GeoSQLiteHelper extends SQLiteOpenHelper {
    protected static final String DB_NAME = "geounderground.db";
    protected static final int DB_VERSION = 1;

    public GeoSQLiteHelper(Context context){ super(context, DB_NAME, null, DB_VERSION); }

    //region Layer Table Setup
    public static final String LAYER_TABLE = "LAYERS";
    public static final String COLUMN_LAYER_STYLEPATH = "StylePath";
    public static final String COLUMN_LAYER_GEOMETRY_TYPE_CODE_ID = "GeometryTypeCodeId";
    public static final String COLUMN_LAYER_IS_FIXED = "IsFixed";
    public static final String COLUMN_LAYER_IS_OWNER = "IsOwner";
    public static final String COLUMN_LAYER_NAME = "Name";
    public static final String COLUMN_LAYER_IS_SHOWING = "IsShowing";
    public static final String COLUMN_LAYER_EXTENT = "Extent";
    public static final String COLUMN_LAYER_LAYERID = "LayerId";

    public static String CREATE_LAYERS_TABLE =
            "CREATE TABLE " + LAYER_TABLE + " ("
            + BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_LAYER_LAYERID + " INTEGER, "
            + COLUMN_LAYER_NAME + " TEXT, "
            + COLUMN_LAYER_IS_OWNER + " TEXT, "
            + COLUMN_LAYER_EXTENT + " TEXT, "
            + COLUMN_LAYER_IS_FIXED + " INTEGER, "
            + COLUMN_LAYER_STYLEPATH + " TEXT, "
            + COLUMN_LAYER_IS_SHOWING + " INTEGER, "
            + COLUMN_LAYER_GEOMETRY_TYPE_CODE_ID + " INTEGER)";
    //endregion

    //region Folder Table Setup
    public static final String FOLDER_TABLE = "FOLDERS";
    public static final String COLUMN_FOLDER_NAME = "Name";
    public static final String COLUMN_FOLDER_IS_IMPORT = "IsImport";
    public static final String COLUMN_FOLDER_IS_FIXED = "IsFixed";
    public static final String COLUMN_FOLDER_ACCESSLEVEL = "AccessLevel";
    public static final String COLUMN_FOLDER_FOLDER_ID = "FolderId";

    public static String CREATE_FOLDERS_TABLE =
            "CREATE TABLE " + FOLDER_TABLE + " ("
            + BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_FOLDER_FOLDER_ID + " INTEGER, "
            + COLUMN_FOLDER_NAME + " TEXT, "
            + COLUMN_FOLDER_IS_IMPORT + " INTEGER, "
            + COLUMN_FOLDER_IS_FIXED + " INTEGER, "
            + COLUMN_FOLDER_ACCESSLEVEL + " INTEGER)";

    //endregion

    public static final String BOOKMARK_TABLE = "BOOKMARKS";
    public static final String COLUMN_BOOKMARK_NAME = "Name";
    public static final String COLUMN_BEARING = "Bearing";
    public static final String COLUMN_TILT = "Tilt";
    public static final String COLUMN_LATITUDE = "Latitude";
    public static final String COLUMN_LONGITUDE = "Longitude";
    public static final String COLUMN_ZOOM = "Zoom";

    public static String CREATE_BOOKMARKS_TABLE =
            "CREATE TABLE " + BOOKMARK_TABLE + " ("
            + BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_BOOKMARK_NAME + " TEXT, "
            + COLUMN_BEARING + " REAL, "
            + COLUMN_TILT + " REAL, "
            + COLUMN_LATITUDE + " REAL, "
            + COLUMN_LONGITUDE + " REAL, "
            + COLUMN_ZOOM + " REAL)";

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_LAYERS_TABLE);
        db.execSQL(CREATE_FOLDERS_TABLE);
        db.execSQL(CREATE_BOOKMARKS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
