package com.geospatialcorporation.android.geomobile.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.geospatialcorporation.android.geomobile.R;
import com.geospatialcorporation.android.geomobile.models.Layers.Layer;

import java.util.List;

/**
 * Created by andre on 6/2/2015.
 */
public class LayerDrawerAdapter extends ArrayAdapter<Layer> {

    private List<Layer> mLayers;

    public LayerDrawerAdapter(Context context, List<Layer> layers) {
        super(context, R.layout.drawer_right_list_item, layers);
        mLayers = layers;
    }

    private class ViewHolder {
        TextView layerName;
        CheckBox isVisible;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;

        if (convertView == null) {
            LayoutInflater vi = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = vi.inflate(R.layout.drawer_right_list_item, null);

            holder = new ViewHolder();
            holder.layerName = (TextView) convertView.findViewById(R.id.layerNameTV);
            holder.isVisible = (CheckBox) convertView.findViewById(R.id.isVisible);
            convertView.setTag(holder);

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CheckBox cb = (CheckBox) v.findViewById(R.id.isVisible);
                    if(cb.isChecked()){
                        cb.setChecked(false);
                    } else {
                        cb.setChecked(true);
                    }
                }
            });
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Layer layer = mLayers.get(position);
        holder.layerName.setText(layer.getName());
        holder.isVisible.setChecked(layer.getIsShowing());

        return convertView;

    }

}
