package com.geospatialcorporation.android.geomobile.database.DataRepository;

public interface IDataRepository<T> {

    T getById(int id);

    void update(T entity, int id);

    Iterable<T> getAll();

    void Remove(int id);

    int Create(T type);

    void Create(Iterable<T> entities);
}
