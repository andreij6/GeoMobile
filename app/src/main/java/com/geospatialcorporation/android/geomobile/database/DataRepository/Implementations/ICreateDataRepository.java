package com.geospatialcorporation.android.geomobile.database.DataRepository.Implementations;

public interface ICreateDataRepository<T> {
    int Create(T type);

    void Create(Iterable<T> entities);
}
