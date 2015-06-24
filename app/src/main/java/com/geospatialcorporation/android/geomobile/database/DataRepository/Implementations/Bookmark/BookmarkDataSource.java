package com.geospatialcorporation.android.geomobile.database.DataRepository.Implementations.Bookmark;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.provider.BaseColumns;

import com.geospatialcorporation.android.geomobile.database.DataRepository.Implementations.DataSourceGenericBase;
import com.geospatialcorporation.android.geomobile.database.sqlitehelper.GeoSQLiteHelper;
import com.geospatialcorporation.android.geomobile.models.Bookmarks.Bookmark;
import com.geospatialcorporation.android.geomobile.models.Bookmarks.BookmarkPosition;

/**
 * Created by andre on 6/6/2015.
 */
public class BookmarkDataSource extends DataSourceGenericBase<Bookmark> {

    public BookmarkDataSource(Context context) {
        super(context);
        mTableName = GeoSQLiteHelper.BOOKMARK_TABLE;
        mEntityIdColumn = BaseColumns._ID;
    }

    @Override
    protected void setEntityProperties(Bookmark entity, Cursor cursor) {
        entity.setId(getIntFromColumnName(cursor, BaseColumns._ID));
        entity.setName(getStringFromColumnName(cursor, GeoSQLiteHelper.COLUMN_BOOKMARK_NAME));

        BookmarkPosition bp = new BookmarkPosition();

        bp.setBearing(getFloatFromColumnName(cursor, GeoSQLiteHelper.COLUMN_BEARING));
        bp.setLat(getFloatFromColumnName(cursor, GeoSQLiteHelper.COLUMN_LATITUDE));
        bp.setLng(getFloatFromColumnName(cursor, GeoSQLiteHelper.COLUMN_LONGITUDE));
        bp.setTilt(getFloatFromColumnName(cursor, GeoSQLiteHelper.COLUMN_TILT));
        bp.setZoom(getFloatFromColumnName(cursor, GeoSQLiteHelper.COLUMN_ZOOM));

        entity.setPosition(bp);
    }

    @Override
    protected ContentValues setEntityContentValues(Bookmark bookmark) {
        ContentValues bValues = new ContentValues();

        bValues.put(GeoSQLiteHelper.COLUMN_BOOKMARK_NAME, bookmark.getName());
        bValues.put(GeoSQLiteHelper.COLUMN_BEARING, bookmark.getPosition().getBearing());
        bValues.put(GeoSQLiteHelper.COLUMN_TILT, bookmark.getPosition().getTilt());
        bValues.put(GeoSQLiteHelper.COLUMN_ZOOM, bookmark.getPosition().getZoom());
        bValues.put(GeoSQLiteHelper.COLUMN_LATITUDE, bookmark.getPosition().getLat());
        bValues.put(GeoSQLiteHelper.COLUMN_LONGITUDE, bookmark.getPosition().getLng());

        return bValues;
    }

    @Override
    protected Bookmark createSetEntityProperties(Cursor cursor) {
        Bookmark result = new Bookmark();

        setEntityProperties(result, cursor);

        return result;
    }

}
