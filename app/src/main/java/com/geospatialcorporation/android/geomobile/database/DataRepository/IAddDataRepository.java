package com.geospatialcorporation.android.geomobile.database.DataRepository;

import java.util.List;

public interface IAddDataRepository<T>  {
    void Add(int id, T entity);

    void Add(List<T> entity);
}
