package com.geospatialcorporation.android.geomobile.library.DI.TreeServices.Implementations;

import android.util.Log;

import com.geospatialcorporation.android.geomobile.application;
import com.geospatialcorporation.android.geomobile.library.DI.TreeServices.Interfaces.ISublayerTreeService;
import com.geospatialcorporation.android.geomobile.library.requestcallback.RequestCallback;
import com.geospatialcorporation.android.geomobile.library.requestcallback.listener_implementations.SublayerModifiedListner;
import com.geospatialcorporation.android.geomobile.library.rest.SublayerService;
import com.geospatialcorporation.android.geomobile.models.Layers.Layer;
import com.geospatialcorporation.android.geomobile.models.Layers.SublayerCreateRequest;
import com.geospatialcorporation.android.geomobile.models.RenameRequest;

import java.util.List;

public class SublayerTreeService implements ISublayerTreeService {
    private static String TAG = SublayerTreeService.class.getSimpleName();

    SublayerService mService;

    public SublayerTreeService(){
        mService = application.getRestAdapter().create(SublayerService.class);
    }

    @Override
    public void rename(int id, String name) {
        RenameRequest request = new RenameRequest(name);

        mService.rename(id, request, new RequestCallback<>(new SublayerModifiedListner()));

    }

    @Override
    public void delete(int sublayerId) {
        mService.delete(sublayerId, new RequestCallback<>(new SublayerModifiedListner()));
    }

    @Override
    public List<Layer> getSublayersByLayerId(int layerId) {
        List<Layer> sublayers = null;

        try {
            sublayers = mService.getSublayers(layerId);
        } catch (Exception e){
            //Log.d(TAG, e.getMessage());
        }

        return sublayers;
    }

    @Override
    public void createSublayer(SublayerCreateRequest model) {
        mService.create(model, new RequestCallback<>(new SublayerModifiedListner()));
    }
}
