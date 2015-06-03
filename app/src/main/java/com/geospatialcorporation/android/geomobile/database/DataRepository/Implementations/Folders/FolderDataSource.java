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

        GetEntityById = GetFoldersById;
        GetAllEntities = GetAllFolders;
        UpdateEntity = UpdateFolder;
        CreateEntity = CreateFolder;
        CreateMultiple = CreateMultipleFolders;
        mTableName = GeoSQLiteHelper.FOLDER_TABLE;
    }
    //endregion

    //region Action Helpers
    protected DbAction GetFoldersById = new DbAction(){
        @Override
        public void Run(){
            Cursor cursor = RawGetById(GeoSQLiteHelper.COLUMN_FOLDER_FOLDER_ID);

            if(cursor.moveToFirst()){
                SetFolderProperties(mEntity, cursor);
            }
        }
    };

    protected DbAction GetAllFolders = new DbAction(){
        @Override
        public void Run(){
            ArrayList<Folder> folders = new ArrayList<>();

            Cursor cursor = GetAllCursor();

            if(cursor.moveToNext()){
                do {
                    Folder folder = new Folder();

                    SetFolderProperties(folder, cursor);

                    folders.add(folder);
                }while(cursor.moveToNext());
            }

            mEntities = folders;
        }
    };

    protected DbAction UpdateFolder = new DbAction(){
        @Override
        public void Run(){
            ContentValues folderValues = SetFolderContentValues(mEntity);

            mDatabase.update(GeoSQLiteHelper.FOLDER_TABLE, folderValues, WhereId(mEntityId), null);
        }
    };


    protected DbAction CreateFolder = new DbAction(){
        @Override
        public void Run(){
            ContentValues folderValues = SetFolderContentValues(mEntity);

            InsertFolder(folderValues);
        }
    };

    protected DbAction CreateMultipleFolders = new DbAction(){
        @Override
        public void Run(){
            for(Folder folder : mEntities){
                ContentValues layerValues = SetFolderContentValues(folder);

                InsertFolder(layerValues);
            }
        }
    };


    //endregion

    //region Helpers
    private void InsertFolder(ContentValues folderValues) {
        mEntityId = (int)mDatabase.insert(GeoSQLiteHelper.FOLDER_TABLE, null, folderValues);
    }

    private void SetFolderProperties(Folder folder, Cursor cursor) {

        folder.setName(getStringFromColumnName(cursor, GeoSQLiteHelper.COLUMN_LAYER_NAME));
        folder.setMobileId(getIntFromColumnName(cursor, BaseColumns._ID));
        folder.setId(getIntFromColumnName(cursor, GeoSQLiteHelper.COLUMN_FOLDER_FOLDER_ID));
        folder.setIsFixed(getBooleanFromColumnName(cursor, GeoSQLiteHelper.COLUMN_FOLDER_IS_FIXED));
        folder.setIsImportFolder(getBooleanFromColumnName(cursor, GeoSQLiteHelper.COLUMN_FOLDER_IS_IMPORT));
        folder.setAccessLevel(getIntFromColumnName(cursor, GeoSQLiteHelper.COLUMN_FOLDER_ACCESSLEVEL));

    }

    protected ContentValues SetFolderContentValues(Folder folder) {
        ContentValues folderValues = new ContentValues();

        folderValues.put(GeoSQLiteHelper.COLUMN_FOLDER_FOLDER_ID, folder.getId());
        folderValues.put(GeoSQLiteHelper.COLUMN_LAYER_NAME, folder.getName());
        folderValues.put(GeoSQLiteHelper.COLUMN_LAYER_GEOMETRY_TYPE_CODE_ID, folder.getId());
        folderValues.put(GeoSQLiteHelper.COLUMN_LAYER_STYLEPATH, folder.getAccessLevel());
        folderValues.put(GeoSQLiteHelper.COLUMN_LAYER_IS_FIXED, ConvertBooleanToInt(folder.getIsFixed()));
        folderValues.put(GeoSQLiteHelper.COLUMN_LAYER_IS_OWNER, ConvertBooleanToInt(folder.getIsImportFolder()));

        return folderValues;
    }
    //endregion

}
