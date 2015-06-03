package com.geospatialcorporation.android.geomobile.database.DataRepository;

import java.util.List;

/**
 * Created by andre on 6/3/2015.
 */
public interface IAddDataRepository<T>  {
    void Add(int id, T entity);

    void Add(List<T> entity);
}
