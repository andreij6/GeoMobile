package com.geospatialcorporation.android.geomobile.library.DI.TreeServices.Implementations;

import android.widget.Toast;

import com.geospatialcorporation.android.geomobile.application;
import com.geospatialcorporation.android.geomobile.database.DataRepository.IFullDataRepository;
import com.geospatialcorporation.android.geomobile.library.DI.TreeServices.Interfaces.ILayerTreeService;
import com.geospatialcorporation.android.geomobile.library.requestcallback.RequestCallback;
import com.geospatialcorporation.android.geomobile.library.requestcallback.listener_implementations.AttributeColumnModifiedListener;
import com.geospatialcorporation.android.geomobile.library.requestcallback.listener_implementations.AttributeValueModifiedListener;
import com.geospatialcorporation.android.geomobile.library.requestcallback.listener_implementations.LayerModifiedListener;
import com.geospatialcorporation.android.geomobile.library.rest.AttributeService;
import com.geospatialcorporation.android.geomobile.library.rest.LayerService;
import com.geospatialcorporation.android.geomobile.models.AddAttributeRequest;
import com.geospatialcorporation.android.geomobile.models.Layers.Columns;
import com.geospatialcorporation.android.geomobile.models.Layers.EditLayerAttributesRequest;
import com.geospatialcorporation.android.geomobile.models.Layers.Layer;
import com.geospatialcorporation.android.geomobile.models.Layers.LayerAttributeColumn;
import com.geospatialcorporation.android.geomobile.models.Layers.LayerCreateRequest;
import com.geospatialcorporation.android.geomobile.models.Layers.LayerDetailsVm;
import com.geospatialcorporation.android.geomobile.models.RemoveMapFeatureDocumentRequest;
import com.geospatialcorporation.android.geomobile.models.RenameRequest;
import com.geospatialcorporation.android.geomobile.ui.fragments.GoogleMapFragment;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class LayerTreeService implements ILayerTreeService {

    LayerService mLayerService;
    AttributeService mAttributeService;
    IFullDataRepository<Layer> mLayerRepo;

    public LayerTreeService(IFullDataRepository<Layer> repository){
        mLayerService = application.getRestAdapter().create(LayerService.class);
        mAttributeService = application.getRestAdapter().create(AttributeService.class);
        mLayerRepo = repository;
    }

    //region Layer
    @Override
    public void create(String name, int shape, int parentFolder) {
        LayerCreateRequest layer = new LayerCreateRequest(shape, name, parentFolder);

        mLayerService.createLayer(layer, new RequestCallback<>(new LayerModifiedListener()));  //could change Response to back to LayerCreateResponse in the future. would have to make another listener
    }

    @Override
    public void delete(int id) {
        mLayerService.delete(id, new RequestCallback<>(new LayerModifiedListener()));

        mLayerRepo.Remove(id);
    }

    @Override
    public Layer get(int id) {
        Layer layer = mLayerRepo.getById(id);

        if(layer == null){
            layer = mLayerService.getLayer(id);
        }

        return layer;
    }

    @Override
    public void rename(int id, String name) {
        if(AuthorizedToRename(id)) {
            mLayerService.rename(id, new RenameRequest(name), new RequestCallback<>(new LayerModifiedListener()));

            Layer layer = mLayerRepo.getById(id);
            layer.setName(name);
            mLayerRepo.update(layer, id);
        } else {
            Toast.makeText(application.getAppContext(), "Not Authorized to Rename Layer", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public LayerDetailsVm getDetails(int id) {
        return mLayerService.getDetails(id);
    }
    //endregion

    //region LayerAttributeColumns
    @Override
    public List<LayerAttributeColumn> getColumns(int layerId) {
        return mAttributeService.getLayerAttributeColumns(layerId);

    }

    @Override
    public void addColumn(int id, AddAttributeRequest data) {
        mAttributeService.addLayerAttributeColumn(id, data, new RequestCallback<>(new AttributeColumnModifiedListener()));
    }

    @Override
    public void deleteColumn(int layerId, int columnId) {
        mAttributeService.deleteColumn(layerId, columnId);
    }
    //endregion

    @Override
    public void addMapFeatureDocument(int layerId, String featureId, int documentId) {
        mLayerService.addMapFeatureDocument(layerId, featureId, documentId, new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {
                GoogleMapFragment contentFrag = (GoogleMapFragment)application.getMainActivity().getContentFragment();

                contentFrag.refreshFeatureWindow(2);
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
    }

    @Override
    public void removeMapFeatureDocument(RemoveMapFeatureDocumentRequest request) {
        mLayerService.removeMapFeatureDocument(request.getLayerId(), request.getFeatureId(), request.getDoc().getId(), new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {
                GoogleMapFragment contentFrag = (GoogleMapFragment)application.getMainActivity().getContentFragment();

                contentFrag.refreshFeatureWindow(2);
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
    }

    @Override
    public void editAttributeValue(int layerId, EditLayerAttributesRequest request) {
        mLayerService.editAttributes(layerId, request, new RequestCallback<>(new AttributeValueModifiedListener()));
    }

    //region Helpers
    protected boolean AuthorizedToRename(int id) {
        //Layer l = mLayerRepo.getById(id);

        //if(l != null && l.getIsOwner()){
        //    return true;
        //}

        return true;
    }
    //endregion
}
