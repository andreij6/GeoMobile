package com.geospatialcorporation.android.geomobile.ui.adapters;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.geospatialcorporation.android.geomobile.R;
import com.geospatialcorporation.android.geomobile.models.Folders.Folder;
import com.geospatialcorporation.android.geomobile.models.Layers.Layer;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by andre on 6/5/2015.
 */
public class LegendLayerAdapter extends RecyclerView.Adapter<LegendLayerAdapter.Holder> {

    List<Layer> mLayers;
    Context mContext;

    public LegendLayerAdapter(Context context, List<Folder> layerFolders){
        mContext = context;
        mLayers = getLayersFromFolders(layerFolders);
    }

    private List<Layer> getLayersFromFolders(List<Folder> layerFolders) {
        List<Layer> result = new ArrayList<>();

        for(Folder folder : layerFolders){
            if(folder.getLayers() != null || !folder.getLayers().isEmpty()){
                result.addAll(folder.getLayers());
            }
        }

        return result;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.recycler_legend_layers, parent, false);

        return new Holder(v);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        holder.bindLengendLayers(mLayers.get(position));
    }

    @Override
    public int getItemCount() {
        if(mLayers != null){
            return mLayers.size();
        } else {
            return 0;
        }
    }

    public class Holder extends RecyclerView.ViewHolder {

        @InjectView(R.id.layerNameTV) TextView mLayerName;
        @InjectView(R.id.isVisible)CheckBox isVisibleCB;
        @InjectView(R.id.sublayerExpander) ImageView gotoSublayer;

        private Layer mLayer;
        public Holder(View itemView) {
            super(itemView);
            ButterKnife.inject(this, itemView);
        }

        public void bindLengendLayers(Layer layer){
            mLayer = layer;

            mLayerName.setText(layer.getName());
            isVisibleCB.setChecked(layer.getIsShowing());
            gotoSublayer.setOnClickListener(GoToSublayer);
        }

        private View.OnClickListener GoToSublayer = new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Toast.makeText(mContext, "Go to Sublayers for Layer: " + mLayer.getName(), Toast.LENGTH_LONG).show();
            }
        };
    }
}
