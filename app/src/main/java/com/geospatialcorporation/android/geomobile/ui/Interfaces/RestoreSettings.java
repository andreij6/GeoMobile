package com.geospatialcorporation.android.geomobile.ui.Interfaces;


import com.geospatialcorporation.android.geomobile.application;

public abstract class RestoreSettings<T> {

    public RestoreSettings(){
        mShouldRestore = false;
    }

    public RestoreSettings(T entity){
        reset(entity);
    }

    protected Boolean mShouldRestore;
    protected T mEntity;

    public Boolean getShouldRestore() {
        return mShouldRestore;
    }

    public void setShouldRestore(Boolean shouldRestore) {
        mShouldRestore = shouldRestore;
    }

    public T getEntityConditionally(T current){
        if(mShouldRestore) {
            mShouldRestore = false;
            return mEntity;
        } else {
            return current;
        }
    }

    public void setEntity(T entity){
        this.mEntity = entity;
    }

    public void reset(T value) {
        setShouldRestore(true);
        setEntity(value);
    }

    public T getEntity() {
        return mEntity;
    }


    public void onSaveInstance(T entity, String key) {
        reset(entity);
        application.setRestoreSettings(this, key);
    }
}
