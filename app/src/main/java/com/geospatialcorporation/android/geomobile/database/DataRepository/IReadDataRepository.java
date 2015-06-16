package com.geospatialcorporation.android.geomobile.database.DataRepository;

import java.util.List;

/**
 * Created by andre on 6/16/2015.
 */
public interface IReadDataRepository<T> {

    List<T> getAll();

    T getById(int id);
}
