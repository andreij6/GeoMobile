package com.geospatialcorporation.android.geomobile.database.DataRepository.Implementations;

import com.geospatialcorporation.android.geomobile.database.DataRepository.IAddDataRepository;
import com.geospatialcorporation.android.geomobile.database.DataRepository.IDataRepository;
import com.geospatialcorporation.android.geomobile.models.IdModel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by andre on 6/3/2015.
 */
public class AppSourceBase<T> implements IAppDataRepository<T> {

    HashMap<Integer, T> Data;

    public AppSourceBase(HashMap<Integer, T> data){
        Data = data;
    }

    @Override
    public T getById(int id) {
        return Data.get(id);
    }

    @Override
    public void update(T entity, int id) {
        Data.remove(id);
        Data.put(id, entity);
    }

    @Override
    public Iterable<T> getAll() {
        return Data.values();
    }

    @Override
    public void Remove(int id) {
        Data.remove(id);
    }

    //use add
    @Override
    public int Create(T type) {
        return 0;
    }

    @Override
    public void Create(Iterable<T> entities) {

    }

    @Override
    public void Add(int id, T entity) {
        Data.put(id, entity);
    }

    @Override
    public void Add(List<T> entities) {
        for(T entity : entities){
            IdModel e = (IdModel)entity;
            if(!Data.containsKey(e.getId())) {
                Data.put(e.getId(), entity);
            }

        }

    }
}
