package com.geospatialcorporation.android.geomobile.database.DataRepository.Implementations.Folders;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.provider.BaseColumns;

import com.geospatialcorporation.android.geomobile.database.DataRepository.Implementations.DataSourceGenericBase;
import com.geospatialcorporation.android.geomobile.database.sqlitehelper.GeoSQLiteHelper;
import com.geospatialcorporation.android.geomobile.models.Folders.Folder;

import java.util.ArrayList;

public class FolderDataSource extends DataSourceGenericBase<Folder> {

    //region Constructor
    public FolderDataSource(Context context){
        super(context);
        mTableName = GeoSQLiteHelper.FOLDER_TABLE;
        mEntityIdColumn = GeoSQLiteHelper.COLUMN_FOLDER_FOLDER_ID;
    }

    @Override
    protected void setEntityProperties(Folder folder, Cursor cursor) {
        folder.setName(getStringFromColumnName(cursor, GeoSQLiteHelper.COLUMN_LAYER_NAME));
        folder.setMobileId(getIntFromColumnName(cursor, BaseColumns._ID));
        folder.setId(getIntFromColumnName(cursor, GeoSQLiteHelper.COLUMN_FOLDER_FOLDER_ID));
        folder.setIsFixed(getBooleanFromColumnName(cursor, GeoSQLiteHelper.COLUMN_FOLDER_IS_FIXED));
        folder.setIsImportFolder(getBooleanFromColumnName(cursor, GeoSQLiteHelper.COLUMN_FOLDER_IS_IMPORT));
        folder.setAccessLevel(getIntFromColumnName(cursor, GeoSQLiteHelper.COLUMN_FOLDER_ACCESSLEVEL));
    }

    @Override
    protected ContentValues setEntityContentValues(Folder folder) {
        ContentValues folderValues = new ContentValues();

        folderValues.put(GeoSQLiteHelper.COLUMN_FOLDER_FOLDER_ID, folder.getId());
        folderValues.put(GeoSQLiteHelper.COLUMN_LAYER_NAME, folder.getName());
        folderValues.put(GeoSQLiteHelper.COLUMN_LAYER_GEOMETRY_TYPE_CODE_ID, folder.getId());
        folderValues.put(GeoSQLiteHelper.COLUMN_LAYER_STYLEPATH, folder.getAccessLevel());
        folderValues.put(GeoSQLiteHelper.COLUMN_LAYER_IS_FIXED, ConvertBooleanToInt(folder.getIsFixed()));
        folderValues.put(GeoSQLiteHelper.COLUMN_LAYER_IS_OWNER, ConvertBooleanToInt(folder.getIsImportFolder()));

        return folderValues;
    }

    @Override
    protected Folder createSetEntityProperties(Cursor cursor) {
        Folder result = new Folder();

        setEntityProperties(result, cursor);

        return result;
    }
    //endregion


}
