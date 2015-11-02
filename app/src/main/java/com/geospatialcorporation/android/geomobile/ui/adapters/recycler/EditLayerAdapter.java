package com.geospatialcorporation.android.geomobile.ui.adapters.recycler;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.geospatialcorporation.android.geomobile.R;
import com.geospatialcorporation.android.geomobile.application;
import com.geospatialcorporation.android.geomobile.models.Layers.LegendLayer;
import com.geospatialcorporation.android.geomobile.ui.adapters.recycler.base.GeoHolderBase;
import com.geospatialcorporation.android.geomobile.ui.adapters.recycler.base.GeoRecyclerAdapterBase;
import com.geospatialcorporation.android.geomobile.ui.fragments.GoogleMapFragment;
import com.geospatialcorporation.android.geomobile.ui.fragments.dialogs.action_dialogs.layer.EditLayersDialogFragment;

import java.util.List;

import butterknife.Bind;

public class EditLayerAdapter extends GeoRecyclerAdapterBase<EditLayerAdapter.Holder, LegendLayer> {

    EditLayersDialogFragment mDialog;

    public EditLayerAdapter(Context context, List<LegendLayer> layers, EditLayersDialogFragment editLayersDialogFragment) {
        super(context, layers, R.layout.recycler_edit_layers, Holder.class);
        mDialog = editLayersDialogFragment;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        setView(parent, viewType);
        return new Holder(mView);
    }

    protected class Holder extends GeoHolderBase<LegendLayer> {

        @Bind(R.id.iconIV) ImageView mIcon;
        @Bind(R.id.layerNameTV) TextView mLayerName;

        public Holder(View v){super(v);}

        @Override
        public void bind(final LegendLayer response) {
            mIcon.setImageDrawable(response.getLegendIcon());
            mLayerName.setText(response.getLayer().getName());

            mView.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View v) {
                    if(application.getIsTablet()){

                    } else {

                        GoogleMapFragment map = application.getMapFragment();

                        map.editLayer(response);

                        mDialog.close();
                    }
                }
            });
        }

    }
}
