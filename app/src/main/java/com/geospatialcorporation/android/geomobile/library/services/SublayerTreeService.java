package com.geospatialcorporation.android.geomobile.library.services;

import android.util.Log;

import com.geospatialcorporation.android.geomobile.application;
import com.geospatialcorporation.android.geomobile.library.helpers.Interfaces.ITreeService;
import com.geospatialcorporation.android.geomobile.library.rest.SublayerService;
import com.geospatialcorporation.android.geomobile.models.Layers.Layer;
import com.geospatialcorporation.android.geomobile.models.RenameRequest;

import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by andre on 6/8/2015.
 */
public class SublayerTreeService implements ITreeService {
    private static String TAG = SublayerTreeService.class.getSimpleName();

    SublayerService mService;

    public SublayerTreeService(){
        mService = application.getRestAdapter().create(SublayerService.class);
    }
    @Override
    public Boolean rename(int id, String name) {

        RenameRequest request = new RenameRequest(name);

        mService.rename(id, request, new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {
                Log.d(TAG, "rename was a success");
            }

            @Override
            public void failure(RetrofitError error) {
                Log.d(TAG, "Error: " + error.getMessage());
            }
        });

        return true;
    }

    public void delete(int sublayerId){
        mService.delete(sublayerId, new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {
                Log.d(TAG, "sUCCEss");
            }

            @Override
            public void failure(RetrofitError error) {
                Log.d(TAG, error.getMessage());
            }
        });
    }

    public List<Layer> getSublayersByLayerId(int layerId){
        List<Layer> sublayers = null;

        try {
            sublayers = mService.getSublayers(layerId);
        }catch (RetrofitError error){
            Log.d(TAG, error.getMessage());
        } catch (Exception e){
            Log.d(TAG, e.getMessage());
        }

        return sublayers;

    }
}
