package com.geospatialcorporation.android.geomobile.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.geospatialcorporation.android.geomobile.R;
import com.geospatialcorporation.android.geomobile.library.helpers.GeoDialogHelper;
import com.geospatialcorporation.android.geomobile.library.rest.SublayerService;
import com.geospatialcorporation.android.geomobile.models.Layers.Layer;
import com.geospatialcorporation.android.geomobile.ui.MainActivity;
import com.melnykov.fab.FloatingActionButton;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by andre on 6/8/2015.
 */
public class SublayerAdapter extends RecyclerView.Adapter<SublayerAdapter.ViewHolder> {
    Context mContext;
    List<Layer> mData;

    public SublayerAdapter(Context context, List<Layer> sublayers){
        mContext = context;
        mData = sublayers;
        Layer allFeatures = new Layer("All Features");
        mData.add(0, allFeatures);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_list_sublayer, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if(mData.get(position).getName().equals("All Features")){
            holder.bindAllFeatures(mData.get(position));
        } else {
            holder.bindSublayer(mData.get(position));
        }
    }

    @Override
    public int getItemCount() {
        if(mData != null){
            return mData.size();
        } else {
            return 0;
        }

    }

    protected class ViewHolder extends RecyclerView.ViewHolder {
        Layer mSublayer;

        @InjectView(R.id.sublayerLabel) TextView mSublayerName;
        @InjectView(R.id.fab_edit)FloatingActionButton mEdit;
        @InjectView(R.id.showingCB) CheckBox mVisible;

        @OnClick(R.id.fab_edit)
        public void showEditDialog(){
            GeoDialogHelper.modifySublayer(mContext, mSublayer, ((MainActivity)mContext).getSupportFragmentManager());
        }

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.inject(this, itemView);
        }

        public void bindAllFeatures(Layer layer){
            mSublayerName.setText(layer.getName());
            mVisible.setChecked(layer.getIsShowing());
            mSublayer = layer;
            mEdit.setVisibility(View.GONE);
        }

        public void bindSublayer(Layer layer) {
            mSublayerName.setText(layer.getName());
            mVisible.setChecked(layer.getIsShowing());
            mSublayer = layer;

            mEdit.setVisibility(View.VISIBLE);
        }
    }
}
