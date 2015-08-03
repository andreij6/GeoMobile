package com.geospatialcorporation.android.geomobile.library.DI.Tasks.models;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;

import com.geospatialcorporation.android.geomobile.models.Layers.Layer;
import com.geospatialcorporation.android.geomobile.models.Layers.LayerAttributeColumn;
import com.geospatialcorporation.android.geomobile.ui.Interfaces.IPostExecuter;

import java.util.List;

public class GetLayerAttributesTaskParams extends ExecuterParamsBase {

    Layer mEntity;
    Context mContext;
    List<LayerAttributeColumn> mColumns;

    public GetLayerAttributesTaskParams(Layer entity, FragmentActivity activity, List<LayerAttributeColumn> attributeColumns, IPostExecuter executer) {
        super(executer);
        mEntity = entity;
        mColumns = attributeColumns;
        mContext = activity;
    }

    public Layer getEntity() {
        return mEntity;
    }

    public Context getContext() {
        return mContext;
    }

    public List<LayerAttributeColumn> getColumns() {
        return mColumns;
    }
}
