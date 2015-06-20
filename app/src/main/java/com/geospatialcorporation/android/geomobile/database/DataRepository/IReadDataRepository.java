package com.geospatialcorporation.android.geomobile.database.DataRepository;

import java.util.List;

public interface IReadDataRepository<T> {

    List<T> getAll();

    T getById(int id);
}
