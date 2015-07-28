package com.geospatialcorporation.android.geomobile.ui.adapters.recycler;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.geospatialcorporation.android.geomobile.R;
import com.geospatialcorporation.android.geomobile.library.helpers.GeoDialogHelper;
import com.geospatialcorporation.android.geomobile.models.Layers.Layer;
import com.geospatialcorporation.android.geomobile.ui.MainActivity;
import com.geospatialcorporation.android.geomobile.ui.adapters.recycler.base.GeoHolderBase;
import com.geospatialcorporation.android.geomobile.ui.adapters.recycler.base.GeoRecyclerAdapterBase;
import com.melnykov.fab.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by andre on 6/8/2015.
 */
public class SublayerAdapter extends GeoRecyclerAdapterBase<SublayerAdapter.Holder, Layer> {

    public SublayerAdapter(Context context, List<Layer> sublayers){
        super(context, sublayers, R.layout.recycler_list_sublayer, Holder.class);
        mData = sublayers == null ? new ArrayList<Layer>() : sublayers ;
        Layer allFeatures = new Layer("All Features");
        mData.add(0, allFeatures);
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        setView(parent, viewType);
        return new Holder(mView);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        if(mData.get(position).getName().equals("All Features")){
            holder.bindAllFeatures(mData.get(position));
        } else {
            holder.bind(mData.get(position));
        }
    }

    protected class Holder extends GeoHolderBase<Layer> {
        Layer mSublayer;

        @InjectView(R.id.sublayerLabel) TextView mSublayerName;
        @InjectView(R.id.fab_edit)FloatingActionButton mEdit;
        //@InjectView(R.id.showingCB) CheckBox mVisible;

        @OnClick(R.id.fab_edit)
        public void showEditDialog(){
            GeoDialogHelper.modifySublayer(mContext, mSublayer, ((MainActivity)mContext).getSupportFragmentManager());
        }

        public Holder(View itemView) {
            super(itemView);
        }

        public void bindAllFeatures(Layer layer){
            bindLayer(layer);

            mEdit.setVisibility(View.GONE);
        }

        public void bind(Layer layer) {
            bindLayer(layer);

            mEdit.setVisibility(View.VISIBLE);
        }

        public void bindLayer(Layer layer){
            mSublayerName.setText(layer.getName());
            //mVisible.setChecked(layer.getIsShowing());
            mSublayer = layer;
        }
    }
}
