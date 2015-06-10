package com.geospatialcorporation.android.geomobile.ui.adapters;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
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
import com.geospatialcorporation.android.geomobile.application;
import com.geospatialcorporation.android.geomobile.models.Folders.Folder;
import com.geospatialcorporation.android.geomobile.models.Layers.Layer;
import com.geospatialcorporation.android.geomobile.ui.MainActivity;
import com.geospatialcorporation.android.geomobile.ui.fragments.GoogleMapFragment;
import com.geospatialcorporation.android.geomobile.ui.fragments.detail_fragment.LayerDetailFragment;

import org.apache.commons.io.output.TaggedOutputStream;

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
        holder.bindLegendLayers(mLayers.get(position));
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

        public void bindLegendLayers(Layer layer){
            mLayer = layer;

            mLayerName.setText(layer.getName());
            mLayerName.setOnClickListener(ZoomToLayer);
            isVisibleCB.setChecked(layer.getIsShowing());
            gotoSublayer.setOnClickListener(GoToSublayer);
        }

        //region Click Listeners
        private View.OnClickListener GoToSublayer = new View.OnClickListener(){
            @Override
            public void onClick(View v){
                MainActivity activity = (MainActivity)mContext;
                Fragment frag = new LayerDetailFragment();
                FragmentManager fm = activity.getSupportFragmentManager();

                Bundle b = new Bundle();
                b.putParcelable(Layer.LAYER_INTENT, mLayer);
                frag.setArguments(b);

                fm.beginTransaction()
                        .replace(R.id.content_frame, frag)
                        .addToBackStack(null)
                        .commit();

                closeDrawer(activity);
            }
        };

        private View.OnClickListener ZoomToLayer = new View.OnClickListener(){
            @Override
            public void onClick(View v){
                MainActivity activity = (MainActivity)mContext;

                Fragment currentFragment = activity.getContentFragment();

                if(currentFragment instanceof GoogleMapFragment){
                    zoomToLayer();
                } else {
                    goToMap(activity);
                }

                closeDrawer(activity);
            }
        };

        protected void zoomToLayer(){
            Toast.makeText(mContext, "Zoom to " + mLayer.getName() + " Layer", Toast.LENGTH_LONG).show();
        }

        protected void closeDrawer(MainActivity activity) {
            DrawerLayout mDrawerLayout = activity.getRightDrawer();
            View layerView = activity.getLayerListView();

            mDrawerLayout.closeDrawer(layerView);
        }

        protected void goToMap(MainActivity activity) {
            Toast.makeText(mContext, "Zoom to " + mLayer.getName() + " Layer", Toast.LENGTH_LONG).show();

            FragmentManager fm = activity.getSupportFragmentManager();

            fm.beginTransaction()
                    .replace(R.id.content_frame, new GoogleMapFragment())
                    .addToBackStack(null)
                    .commit();
        }
        //endregion
    }
}
