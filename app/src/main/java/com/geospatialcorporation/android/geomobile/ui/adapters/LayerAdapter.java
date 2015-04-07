package com.geospatialcorporation.android.geomobile.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.geospatialcorporation.android.geomobile.models.Layers.Layer;

import java.util.ArrayList;

/**
 * Created by andre on 4/7/2015.
 */
public class LayerAdapter extends RecyclerView.Adapter<LayerAdapter.LayerViewHolder>{

    private Context mContext;
    private ArrayList<Layer> mLayers;

    public LayerAdapter(Context context, ArrayList<Layer> layers){
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
        return mLayers.size();
    }

    protected class LayerViewHolder extends RecyclerView.ViewHolder {
        //regions Properties

        //endregion

        public LayerViewHolder(View itemView){
            super(itemView);
        }

        public void bindLayerItem(Layer item){

        }

        protected View.OnClickListener LayerItemClicked = new View.OnClickListener(){

            @Override
            public void onClick(View v) {

            }
        };
    }
}
