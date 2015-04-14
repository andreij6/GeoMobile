package com.geospatialcorporation.android.geomobile.database.datasource.base;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

import com.geospatialcorporation.android.geomobile.database.sqlitehelper.GeoSQLiteHelper;
import com.geospatialcorporation.android.geomobile.models.Layers.Extent;
import com.google.gson.Gson;

import java.util.Date;

/**
 * Created by andre on 4/10/2015.
 */
public abstract class DataSourceBase {

    protected Context mContext;
    protected GeoSQLiteHelper mGeoDbHelper;
    protected SQLiteDatabase mDatabase;

    public DataSourceBase(Context context){
        mContext = context;
        mGeoDbHelper = new GeoSQLiteHelper(context);
    }

    protected SQLiteDatabase open(){
        return mGeoDbHelper.getWritableDatabase();
    }

    protected void close(SQLiteDatabase db){
        db.close();
    }

    protected void InTransaction(DbAction action){
        mDatabase = open();
        mDatabase.beginTransaction();

        try{
            action.Run();

            mDatabase.setTransactionSuccessful();
            mDatabase.endTransaction();
        } catch (Exception e){
            e.printStackTrace();
        } finally {
            close(mDatabase);
        }



    }

    protected void InReadTransaction(DbAction action){
        mDatabase  = open();

        action.Run();

        close(mDatabase);
    }

    //region general Helpers
    protected int ConvertBooleanToInt(Boolean value) {
        if(value != null)
            return value ? 1 : 0;
        else
            return 1;
    }

    protected String ConvertExtentToJSONString(Extent extent){
        Gson gson = new Gson();

        return gson.toJson(extent);
    }

    protected String WhereId(int id) {
        return  BaseColumns._ID + " = " + id;
    }
    //endregion

    //region Get Value From Column Name Helpers
    protected String getStringFromColumnName(Cursor cursor, String columnName){
        int columnIndex = cursor.getColumnIndex(columnName);

        return cursor.getString(columnIndex);
    }

    protected int getIntFromColumnName(Cursor cursor, String columnName){
        int columnIndex = cursor.getColumnIndex(columnName);

        return cursor.getInt(columnIndex);
    }

    protected Boolean getBooleanFromColumnName(Cursor cursor, String columnName){
        int columnIndex = cursor.getColumnIndex(columnName);

        Integer value = cursor.getInt(columnIndex);

        if(value == 1) {
            return true;
        } else {
            return false;
        }
    }

    protected Date getDateFromColumnName(Cursor cursor, String columnName){
        int columnIndex = cursor.getColumnIndex(columnName);

        String dateString = cursor.getString(columnIndex);

        return new Date(Long.parseLong(dateString));
    }

    protected Extent getExtentFromColumnName(Cursor cursor, String columnName){
        String extentString = getStringFromColumnName(cursor, columnName);

        Gson gson = new Gson();

        return gson.fromJson(extentString, Extent.class);
    }
    //endregion

    public class DbAction {
        public void Run(){

        }
    }

}
