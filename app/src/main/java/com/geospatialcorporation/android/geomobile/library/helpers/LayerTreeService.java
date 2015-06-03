package com.geospatialcorporation.android.geomobile.library.helpers;

import android.widget.Toast;

import com.geospatialcorporation.android.geomobile.application;
import com.geospatialcorporation.android.geomobile.database.DataRepository.Implementations.IAppDataRepository;
import com.geospatialcorporation.android.geomobile.database.DataRepository.Implementations.Layers.LayersAppSource;
import com.geospatialcorporation.android.geomobile.library.rest.LayerService;
import com.geospatialcorporation.android.geomobile.models.Layers.Layer;
import com.geospatialcorporation.android.geomobile.models.Layers.LayerCreateRequest;
import com.geospatialcorporation.android.geomobile.models.Layers.LayerCreateResponse;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by andre on 6/1/2015.
 */
public class LayerTreeService {

    LayerService mLayerService;
    IAppDataRepository<Layer> LayerRepo;

    public LayerTreeService(){
        mLayerService = application.getRestAdapter().create(LayerService.class);
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

    public void deleteLayer(Integer id) {
        mLayerService.delete(id, new Callback<Layer>() {
            @Override
            public void success(Layer layer, Response response) {
                Toast.makeText(application.getAppContext(), "Success!", Toast.LENGTH_SHORT).show();
                Toast.makeText(application.getAppContext(), "Pull to Refresh", Toast.LENGTH_LONG).show();
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
}
