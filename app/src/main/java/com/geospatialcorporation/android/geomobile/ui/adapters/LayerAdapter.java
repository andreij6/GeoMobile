package com.geospatialcorporation.android.geomobile.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.geospatialcorporation.android.geomobile.models.Layers.Layer;

import java.util.List;

/**
 * Created by andre on 4/22/2015.
 */
public class LayerAdapter extends RecyclerView.Adapter<LayerAdapter.LayerViewHolder> {

    Context mContext;
    List<Layer> mLayers;

    public LayerAdapter(Context context, List<Layer> layers){
        mContext = context;
        mLayers = layers;
    }

    @Override
    public LayerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(LayerViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    protected class LayerViewHolder extends RecyclerView.ViewHolder {

        public LayerViewHolder(View itemView) {
            super(itemView);
        }
    }
}


