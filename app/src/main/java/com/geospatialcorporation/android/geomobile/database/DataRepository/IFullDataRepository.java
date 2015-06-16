package com.geospatialcorporation.android.geomobile.database.DataRepository;

import com.geospatialcorporation.android.geomobile.database.DataRepository.Implementations.ICreateDataRepository;

import java.util.List;

public interface IFullDataRepository<T> extends ICreateDataRepository<T>, IReadDataRepository<T>  {

    void update(T entity, int id);

    void Remove(int id);

}
