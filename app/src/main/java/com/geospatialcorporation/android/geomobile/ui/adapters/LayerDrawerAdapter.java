package com.geospatialcorporation.android.geomobile.ui.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.geospatialcorporation.android.geomobile.R;
import com.geospatialcorporation.android.geomobile.models.Layers.Layer;
import com.geospatialcorporation.android.geomobile.ui.MainActivity;

import java.util.List;

/**
 * Created by andre on 6/2/2015.
 */
public class LayerDrawerAdapter extends ArrayAdapter<Layer> {

    private List<Layer> mLayers;

    public LayerDrawerAdapter(Context context, List<Layer> layers) {
        super(context, R.layout.drawer_layer_list_item, layers);
        mLayers = layers;
    }

    private class ViewHolder {

        public void setView(View v){
            mLayerCheckBox = (CheckBox)v.findViewById(R.id.isVisible);
            mSublayerExpander = (ImageView)v.findViewById(R.id.sublayerExpander);
            mLayerName = (TextView)v.findViewById(R.id.layerNameTV);
        }

        TextView mLayerName;
        CheckBox mLayerCheckBox;
        ImageView mSublayerExpander;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;


        if (convertView == null) {
            LayoutInflater vi = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = vi.inflate(R.layout.drawer_layer_list_item, null);

            holder = new ViewHolder();
            holder.setView(convertView);
            convertView.setTag(holder);

            holder.mLayerCheckBox.setOnClickListener(showLayer());
            holder.mSublayerExpander.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getContext(), "Show Sublayers", Toast.LENGTH_LONG).show();

                }
            });


        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Layer layer = mLayers.get(position);
        holder.mLayerName.setText(layer.getName());
        holder.mLayerCheckBox.setChecked(layer.getIsShowing());

        return convertView;

    }

    private View.OnClickListener showLayer() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        };
    }

}
