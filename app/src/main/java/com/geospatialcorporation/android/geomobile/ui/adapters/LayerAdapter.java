package com.geospatialcorporation.android.geomobile.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.geospatialcorporation.android.geomobile.R;
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_list_layer, parent, false);

        return new LayerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(LayerViewHolder holder, int position) {
        holder.bindLayerItem(mLayers.get(position));
    }

    @Override
    public int getItemCount() {
        return mLayers.size();
    }

    protected class LayerViewHolder extends RecyclerView.ViewHolder {
        //region Properties
        TextView mLayerName;
        //endregion

        public LayerViewHolder(View itemView){
            super(itemView);
            mLayerName = (TextView)itemView.findViewById(R.id.layerNameLabel);
        }

        public void bindLayerItem(Layer layer){
            mLayerName.setText(layer.getName());
        }

        protected View.OnClickListener LayerItemClicked = new View.OnClickListener(){

            @Override
            public void onClick(View v) {

            }
        };
    }
}
