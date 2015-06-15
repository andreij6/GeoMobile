package com.geospatialcorporation.android.geomobile.database.DataRepository.Implementations.Bookmark;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.provider.BaseColumns;

import com.geospatialcorporation.android.geomobile.database.DataRepository.Implementations.DataSourceGenericBase;
import com.geospatialcorporation.android.geomobile.database.sqlitehelper.GeoSQLiteHelper;
import com.geospatialcorporation.android.geomobile.models.Bookmarks.Bookmark;
import com.geospatialcorporation.android.geomobile.models.Bookmarks.BookmarkPosition;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by andre on 6/6/2015.
 */
public class BookmarkDataSource extends DataSourceGenericBase<Bookmark> {

    public BookmarkDataSource(Context context) {
        super(context);
        GetEntityById = GetBookmarkById;
        GetAllEntities = GetAllBookmarks;
        UpdateEntity = UpdateBookmark;
        CreateEntity = CreateBookmark;
        CreateMultiple = CreateBookmarks;
        mTableName = GeoSQLiteHelper.BOOKMARK_TABLE;
    }

    //region DbActions

    protected DbAction GetBookmarkById = new DbAction() {
        @Override
        public void Run() {
            Cursor cursor = RawGetById(BaseColumns._ID);

            if(cursor.moveToFirst()){
                SetBookmarkProperties(mEntity, cursor);
            }
        }
    };

    protected DbAction GetAllBookmarks = new DbAction() {
        @Override
        public void Run() {
            List<Bookmark> bookmarks = new ArrayList<>();

            Cursor cursor = GetAllCursor();

            if(cursor.moveToNext()){
                do {
                    Bookmark bookmark = new Bookmark();
                    SetBookmarkProperties(bookmark, cursor);
                    bookmarks.add(bookmark);
                }while (cursor.moveToNext());
            }

            mEntities = bookmarks;
        }
    };

    protected DbAction UpdateBookmark = new DbAction() {
        @Override
        public void Run() {
            ContentValues bookmarkValues = setBookmarkContentValues(mEntity);  //Just updates the name not position

            mDatabase.update(GeoSQLiteHelper.BOOKMARK_TABLE, bookmarkValues, WhereId(mEntityId), null);
        }
    };

    protected DbAction CreateBookmark = new DbAction() {
        @Override
        public void Run() {
            ContentValues bookmarkValues = setBookmarkContentValues(mEntity);

            InsertBookmark(bookmarkValues);
        }
    };

    protected DbAction CreateBookmarks = new DbAction() {
        @Override
        public void Run() {
            for(Bookmark bookmark : mEntities) {
                ContentValues bookmarkValues = setBookmarkContentValues(bookmark);

                InsertBookmark(bookmarkValues);
            }
        }
    };
    //endregion

    //region Helpers
    private void InsertBookmark(ContentValues bookmarkValues) {
        mEntityId = (int)mDatabase.insert(GeoSQLiteHelper.BOOKMARK_TABLE, null, bookmarkValues);
    }

    protected void SetBookmarkProperties(Bookmark entity, Cursor cursor) {
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

    public ContentValues setBookmarkContentValues(Bookmark bookmark) {
        ContentValues bValues = new ContentValues();

        bValues.put(GeoSQLiteHelper.COLUMN_BOOKMARK_NAME, bookmark.getName());
        bValues.put(GeoSQLiteHelper.COLUMN_BEARING, bookmark.getPosition().getBearing());
        bValues.put(GeoSQLiteHelper.COLUMN_TILT, bookmark.getPosition().getTilt());
        bValues.put(GeoSQLiteHelper.COLUMN_ZOOM, bookmark.getPosition().getZoom());
        bValues.put(GeoSQLiteHelper.COLUMN_LATITUDE, bookmark.getPosition().getLat());
        bValues.put(GeoSQLiteHelper.COLUMN_LONGITUDE, bookmark.getPosition().getLng());

        return bValues;
    }
    //endregion
}
