package com.geospatialcorporation.android.geomobile.database.DataRepository.Implementations;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.geospatialcorporation.android.geomobile.database.DataRepository.IFullDataRepository;
import com.geospatialcorporation.android.geomobile.database.datasource.DataSourceBase;

import java.util.ArrayList;
import java.util.List;

public abstract class DataSourceGenericBase<T> extends DataSourceBase implements IFullDataRepository<T> {

    //region Constructor
    public DataSourceGenericBase(Context context)
    {
        super(context);
    }
    //endregion

    //region Things Override in child Classes
    protected String mTableName;
    protected String mEntityIdColumn;

    //region Abstract Methods to override
    protected abstract void setEntityProperties(T entity, Cursor cursor);
    protected abstract ContentValues setEntityContentValues(T entity);
    protected abstract T createSetEntityProperties(Cursor cursor);
    //endregion
    //endregion

    //region Properties
    protected T mEntity;
    protected Iterable<T> mEntities;
    protected int mEntityId;
    //endregion

    //region Action Shells
    protected DbAction GetEntityById = new DbAction() {
        @Override
        public void Run() {
            Cursor cursor = RawGetById(mEntityIdColumn);

            if(cursor.moveToFirst()){
                setEntityProperties(mEntity, cursor);
            }
        }
    };
    protected DbAction UpdateEntity = new DbAction() {
        @Override
        public void Run() {
            ContentValues entityValues = setEntityContentValues(mEntity);

            mDatabase.update(mTableName, entityValues, WhereId(mEntityId), null);
        }
    };
    protected DbAction GetAllEntities = new DbAction() {
        @Override
        public void Run() {
            List<T> result = new ArrayList<>();

            Cursor cursor = GetAllCursor();

            if(cursor.moveToFirst()){
                do {
                    T newEntity = createSetEntityProperties(cursor);

                    result.add(newEntity);

                }while(cursor.moveToNext());
            }

            mEntities = result;
        }
    };
    protected DbAction CreateEntity = new DbAction() {
        @Override
        public void Run() {
            ContentValues contentValues = setEntityContentValues(mEntity);

            insertEntity(contentValues);
        }
    };
    protected DbAction CreateMultiple = new DbAction() {
        @Override
        public void Run() {
            for(T entity : mEntities){
                ContentValues contentValues = setEntityContentValues(mEntity);

                insertEntity(contentValues);
            }
        }
    };
    //endregion

    //region Common Implementations
    protected void insertEntity(ContentValues contentValues){
        mEntityId = (int)mDatabase.insert(mTableName, null, contentValues);
    }

    protected DbAction RemoveEntity = new DbAction(){
        @Override
        public void Run(){
            mDatabase.delete(mTableName, WhereId(mEntityId), null);
        }
    };
    //endregion

    //region Public Interface Shells
    public T getById(int id){
        mEntityId = id;

        inReadTransaction(GetEntityById);

        return mEntity;
    }

    public void update(T entity, int id){
        mEntity = entity;
        mEntityId = id;

        inWriteTransaction(UpdateEntity);
    }

    public List<T> getAll(){
        inReadTransaction(GetAllEntities);

        List<T> result = new ArrayList<>();

        if(mEntities != null) {
            for (T entity : mEntities) {
                result.add(entity);
            }
        }

        return result;
    }

    public void Remove(int id){
        mEntity = getById(id);
        mEntityId = id;

        inWriteTransaction(RemoveEntity);
    }

    public int Create(T entity){
        mEntity = entity;

        inWriteTransaction(CreateEntity);

        return mEntityId;
    }

    public void Create(Iterable<T> entities){
        mEntities = entities;

        inWriteTransaction(CreateMultiple);
    }
    //endregion

    //region Standard Helpers

    protected Cursor RawGetById(String idColumn){
        return mDatabase.rawQuery("SELECT * FROM " + mTableName + " WHERE " + idColumn + " = " + mEntityId, null);
    }

    protected Cursor GetAllCursor(){
        return mDatabase.rawQuery("SELECT * FROM " + mTableName, null);
    }
    //endregion
}
