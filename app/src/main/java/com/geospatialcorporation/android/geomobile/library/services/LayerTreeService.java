package com.geospatialcorporation.android.geomobile.library.services;

import android.widget.Toast;

import com.geospatialcorporation.android.geomobile.application;
import com.geospatialcorporation.android.geomobile.database.DataRepository.IFullDataRepository;
import com.geospatialcorporation.android.geomobile.database.DataRepository.Implementations.Layers.LayersAppSource;
import com.geospatialcorporation.android.geomobile.library.helpers.Interfaces.ITreeService;
import com.geospatialcorporation.android.geomobile.library.rest.AttributeService;
import com.geospatialcorporation.android.geomobile.library.rest.LayerService;
import com.geospatialcorporation.android.geomobile.models.Layers.Layer;
import com.geospatialcorporation.android.geomobile.models.Layers.LayerAttributeColumns;
import com.geospatialcorporation.android.geomobile.models.Layers.LayerCreateRequest;
import com.geospatialcorporation.android.geomobile.models.Layers.LayerCreateResponse;
import com.geospatialcorporation.android.geomobile.models.Layers.LayerDetailsVm;
import com.geospatialcorporation.android.geomobile.models.RenameRequest;

import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by andre on 6/1/2015.
 */
public class LayerTreeService implements ITreeService {

    LayerService mLayerService;
    AttributeService mAttributeService;
    IFullDataRepository<Layer> LayerRepo;

    public LayerTreeService(){
        mLayerService = application.getRestAdapter().create(LayerService.class);
        mAttributeService = application.getRestAdapter().create(AttributeService.class);
        LayerRepo = new LayersAppSource();
    }

    public void createLayer(String name, int shape, int parentFolder){
        LayerCreateRequest layer = new LayerCreateRequest(shape, name, parentFolder);

        mLayerService.createLayer(layer, new Callback<LayerCreateResponse>() {
            @Override
            public void success(LayerCreateResponse layerCreateResponse, Response response) {
                Toast.makeText(application.getAppContext(), "Success", Toast.LENGTH_SHORT).show();
                Toast.makeText(application.getAppContext(), "Pull to Refresh", Toast.LENGTH_LONG).show();
            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(application.getAppContext(), "Failure", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void deleteLayer(final int id) {
        mLayerService.delete(id, new Callback<Layer>() {
            @Override
            public void success(Layer layer, Response response) {
                Toast.makeText(application.getAppContext(), "Success!", Toast.LENGTH_SHORT).show();
                Toast.makeText(application.getAppContext(), "Pull to Refresh", Toast.LENGTH_LONG).show();
                LayerRepo.Remove(id);
            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(application.getAppContext(), "Failure", Toast.LENGTH_LONG).show();
            }
        });

        LayerRepo.Remove(id);
    }

    public Layer getLayer(int id) {
        Layer layer = LayerRepo.getById(id);

        if(layer == null){
            layer = mLayerService.getLayer(id);
        }

        return layer;
    }

    public Boolean rename(final int id, final String name){

        if(!AuthorizedToRename(id)) return false;

        mLayerService.rename(id, new RenameRequest(name), new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {
                //Layer l = LayerRepo.getById(id);
                //l.setName(name);
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });

        return true;

    }

    public LayerDetailsVm getDetails(int id){
        return mLayerService.getDetails(id);
    }

    public List<LayerAttributeColumns> getLayerAttributeColumns(int id){
        return mAttributeService.getLayerAttributeColumns(id);
    }

    public List<LayerAttributeColumns> addLayerAttributeColumn(int id, List<LayerAttributeColumns> data){
        return mAttributeService.addLayerAttributeColumn(id, data);
    }

    public void deleteLayerAttributeColumn(int layerId, int columnId){
        mAttributeService.deleteColumn(layerId, columnId);
    }

    protected boolean AuthorizedToRename(int id) {
        //Layer l = LayerRepo.getById(id);

        //if(l != null && l.getIsOwner()){
        //    return true;
        //}

        return true;
    }
}
