package com.geospatialcorporation.android.geomobile.database.datasource.base;

/**
 * Created by andre on 4/10/2015.
 */
public interface IDataRepository<T> {

    T GetById(int id);

    void Update(T entity, int id);

    Iterable<T> GetAll();

    void Remove(T entity);

    int Create(T type);

    void Create(Iterable<T> entities);
}
