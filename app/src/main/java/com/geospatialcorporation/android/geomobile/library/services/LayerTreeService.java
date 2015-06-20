package com.geospatialcorporation.android.geomobile.library.services;

import android.util.Log;
import android.widget.Toast;

import com.geospatialcorporation.android.geomobile.application;
import com.geospatialcorporation.android.geomobile.database.DataRepository.IFullDataRepository;
import com.geospatialcorporation.android.geomobile.database.DataRepository.Implementations.Layers.LayersAppSource;
import com.geospatialcorporation.android.geomobile.library.helpers.Interfaces.ITreeService;
import com.geospatialcorporation.android.geomobile.library.requestcallback.RequestCallback;
import com.geospatialcorporation.android.geomobile.library.requestcallback.listener_implementations.LayerModifiedListener;
import com.geospatialcorporation.android.geomobile.library.rest.AttributeService;
import com.geospatialcorporation.android.geomobile.library.rest.LayerService;
import com.geospatialcorporation.android.geomobile.models.Layers.Layer;
import com.geospatialcorporation.android.geomobile.models.Layers.LayerAttributeColumn;
import com.geospatialcorporation.android.geomobile.models.Layers.Columns;
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
    private final static String TAG = LayerTreeService.class.getSimpleName();

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

        mLayerService.createLayer(layer, new RequestCallback<>(new LayerModifiedListener()));  //could change Response to back to LayerCreateResponse in the future. would have to make another listener
    }

    public void deleteLayer(final int id) {
        mLayerService.delete(id, new RequestCallback<>(new LayerModifiedListener(false)));
    }

    public Layer getLayer(int id) {
        Layer layer = LayerRepo.getById(id);

        if(layer == null){
            layer = mLayerService.getLayer(id);
        }

        return layer;
    }

    public void rename(final int id, final String name){

        if(AuthorizedToRename(id)) {
            mLayerService.rename(id, new RenameRequest(name), new RequestCallback<>(new LayerModifiedListener()));

            Layer layer = LayerRepo.getById(id);
            layer.setName(name);
            LayerRepo.update(layer, id);
        } else {
            Toast.makeText(application.getAppContext(), "Not Authorized to Rename Folder", Toast.LENGTH_LONG).show();
        }

    }

    public LayerDetailsVm getDetails(int id){
        return mLayerService.getDetails(id);
    }

    public List<LayerAttributeColumn> getLayerAttributeColumns(int id){
        return mAttributeService.getLayerAttributeColumns(id);
    }

    public void addLayerAttributeColumn(int id, List<Columns> data){
        mAttributeService.addLayerAttributeColumn(id, data, new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {
                Log.d(TAG, response.getBody().toString());
            }

            @Override
            public void failure(RetrofitError error) {
                Log.d(TAG, error.getBody().toString());
            }
        });
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
