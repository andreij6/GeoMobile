package com.geospatialcorporation.android.geomobile.database.datasource;

public interface IDataRepository<T> {

    T GetById(int id);

    void Update(T entity, int id);

    Iterable<T> GetAll();

    void Remove(T entity);

    int Create(T type);

    void Create(Iterable<T> entities);
}
