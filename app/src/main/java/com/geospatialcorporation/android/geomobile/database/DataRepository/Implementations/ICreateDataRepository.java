package com.geospatialcorporation.android.geomobile.database.DataRepository.Implementations;

/**
 * Created by andre on 6/16/2015.
 */
public interface ICreateDataRepository<T> {
    int Create(T type);

    void Create(Iterable<T> entities);
}
