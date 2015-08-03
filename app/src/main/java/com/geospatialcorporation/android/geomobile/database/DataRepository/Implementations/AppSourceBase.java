package com.geospatialcorporation.android.geomobile.database.DataRepository.Implementations;

import com.geospatialcorporation.android.geomobile.database.DataRepository.IAddDataRepository;
import com.geospatialcorporation.android.geomobile.database.DataRepository.IFullDataRepository;
import com.geospatialcorporation.android.geomobile.models.Interfaces.IdModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by andre on 6/3/2015.
 */
public class AppSourceBase<T> implements IFullDataRepository<T>, IAddDataRepository<T> {

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
    public List<T> getAll() {
        List<T> result = new ArrayList<>();

        for(T entity : Data.values()){
            result.add(entity);
        }

        return result;
    }

    @Override
    public void Remove(int id) {
        Data.remove(id);
    }

    //use add
    @Override
    public int Create(T entity) {
        IdModel idModel = (IdModel)entity;

        Data.put(idModel.getId(), entity);

        return idModel.getId();
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
