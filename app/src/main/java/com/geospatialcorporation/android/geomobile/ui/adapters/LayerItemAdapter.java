package com.geospatialcorporation.android.geomobile.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.geospatialcorporation.android.geomobile.models.LayerItem;

import java.util.ArrayList;

/**
 * Created by andre on 4/7/2015.
 */
public class LayerItemAdapter extends RecyclerView.Adapter<LayerItemAdapter.LayerItemViewHolder>{

    private Context mContext;
    private ArrayList<LayerItem> mLayerItems;

    public LayerItemAdapter(Context context, ArrayList<LayerItem> layerItems){
        mContext = context;
        mLayerItems = layerItems;
    }

    @Override
    public LayerItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(LayerItemViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return mLayerItems.size();
    }

    protected class LayerItemViewHolder extends RecyclerView.ViewHolder {
        //regions Properties

        //endregion

        public LayerItemViewHolder(View itemView){
            super(itemView);
        }

        public void bindLayerItem(LayerItem item){

        }

        protected View.OnClickListener LayerItemClicked = new View.OnClickListener(){

            @Override
            public void onClick(View v) {

            }
        };
    }
}
