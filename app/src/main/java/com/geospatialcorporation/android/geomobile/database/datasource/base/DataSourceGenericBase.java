package com.geospatialcorporation.android.geomobile.database.datasource.base;

import android.content.Context;
import android.database.Cursor;

public abstract class DataSourceGenericBase<T> extends DataSourceBase implements IDataRepository<T>{

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
    public T GetById(int id){
        mEntityId = id;

        InReadTransaction(GetEntityById);

        return mEntity;
    }

    public void Update(T entity, int id){
        mEntity = entity;
        mEntityId = id;

        InTransaction(UpdateEntity);
    }

    public Iterable<T> GetAll(){
        InTransaction(GetAllEntities);

        return mEntities;
    }

    public void Remove(T entity){
        mEntity = entity;

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
