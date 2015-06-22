package com.geospatialcorporation.android.geomobile.ui.adapters.recycler;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.geospatialcorporation.android.geomobile.R;
import com.geospatialcorporation.android.geomobile.models.Layers.Layer;
import com.geospatialcorporation.android.geomobile.ui.MainActivity;
import com.geospatialcorporation.android.geomobile.ui.adapters.recycler.base.GeoHolderBase;
import com.geospatialcorporation.android.geomobile.ui.adapters.recycler.base.GeoRecyclerAdapterBase;
import com.geospatialcorporation.android.geomobile.ui.fragments.GoogleMapFragment;
import com.geospatialcorporation.android.geomobile.ui.fragments.detail_fragment.LayerDetailFragment;

import java.util.List;

import butterknife.InjectView;

/**
 * Created by andre on 6/5/2015.
 */
public class LegendLayerAdapter extends GeoRecyclerAdapterBase<LegendLayerAdapter.Holder, Layer> {

    public LegendLayerAdapter(Context context, List<Layer> layers){
        super(context, layers, R.layout.recycler_legend_layers, Holder.class);
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        setView(parent, viewType);
        return new Holder(mView);
    }

    public class Holder extends GeoHolderBase<Layer> {

        @InjectView(R.id.layerNameTV) TextView mLayerName;
        @InjectView(R.id.isVisible)CheckBox isVisibleCB;
        @InjectView(R.id.sublayerExpander) ImageView gotoSublayer;

        private Layer mLayer;
        public Holder(View itemView) {
            super(itemView);
        }

        public void bind(Layer layer){
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

                frag.setArguments(mLayer.toBundle());

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
