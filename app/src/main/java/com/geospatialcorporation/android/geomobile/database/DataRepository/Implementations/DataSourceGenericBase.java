package com.geospatialcorporation.android.geomobile.database.DataRepository.Implementations;

import android.content.Context;
import android.database.Cursor;

import com.geospatialcorporation.android.geomobile.database.DataRepository.IDataRepository;
import com.geospatialcorporation.android.geomobile.database.datasource.DataSourceBase;

import java.util.ArrayList;
import java.util.List;

public abstract class DataSourceGenericBase<T> extends DataSourceBase implements IDataRepository<T> {

    //region Constructor
    public DataSourceGenericBase(Context context)
    {
        super(context);
    }
    //endregion

    //region Properties
    protected T mEntity;
    protected Iterable<T> mEntities;
    protected int mEntityId;
    protected String mTableName;
    //endregion

    //region Dependencies
    protected DbAction GetEntityById;
    protected DbAction UpdateEntity;
    protected DbAction GetAllEntities;
    protected DbAction CreateEntity;
    protected DbAction CreateMultiple;
    //endregion

    //region Public Interface Methods
    public T getById(int id){
        mEntityId = id;

        InReadTransaction(GetEntityById);

        return mEntity;
    }

    public void update(T entity, int id){
        mEntity = entity;
        mEntityId = id;

        InTransaction(UpdateEntity);
    }

    public List<T> getAll(){
        InTransaction(GetAllEntities);

        List<T> result = new ArrayList<>();

        for(T entity : mEntities){
            result.add(entity);
        }

        return result;
    }

    public void Remove(int id){
        mEntity = getById(id);
        mEntityId = id;

        InTransaction(RemoveEntity);
    }

    public int Create(T entity){
        mEntity = entity;

        InTransaction(CreateEntity);

        return mEntityId;
    }

    public void Create(Iterable<T> entities){
        mEntities = entities;

        InTransaction(CreateMultiple);
    }
    //endregion

    //region Standard Helpers

    protected DbAction RemoveEntity = new DbAction(){
        @Override
        public void Run(){
            mDatabase.delete(mTableName, WhereId(mEntityId), null);
        }
    };

    protected Cursor RawGetById(String idColumn){
        return mDatabase.rawQuery("SELECT * FROM " + mTableName + " WHERE " + idColumn + " = " + mEntityId, null);
    }

    protected Cursor GetAllCursor(){
        return mDatabase.rawQuery("SELECT * FROM " + mTableName, null);
    }
    //endregion
}
