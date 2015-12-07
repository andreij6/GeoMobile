package com.geospatialcorporation.android.geomobile.library.DI.Tasks.models;

import android.content.Context;
import android.support.v4.app.FragmentActivity;

import com.geospatialcorporation.android.geomobile.models.Layers.Layer;
import com.geospatialcorporation.android.geomobile.ui.Interfaces.IPostExecuter;

import java.util.List;

public class GetSublayersTaskParams extends ExecuterParamsBase{
    private Layer mLayer;
    private Context mContext;
    private List<Layer> mData;

    public GetSublayersTaskParams(List<Layer> data, Layer entity, FragmentActivity activity, IPostExecuter executer) {
        super(executer);
        mData = data;
        mLayer = entity;
        mContext = activity;
    }

    public Layer getLayer() {
        return mLayer;
    }

    public Context getContext() {
        return mContext;
    }

    public List<Layer> getData() {
        return mData;
    }
}
