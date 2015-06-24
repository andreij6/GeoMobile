package com.geospatialcorporation.android.geomobile.database.DataRepository;

import com.geospatialcorporation.android.geomobile.database.DataRepository.Implementations.ICreateDataRepository;

public interface IFullDataRepository<T> extends ICreateDataRepository<T>, IReadDataRepository<T>  {

    void update(T entity, int id);

    void Remove(int id);

}
