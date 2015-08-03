package com.geospatialcorporation.android.geomobile.ui.adapters.recycler;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.geospatialcorporation.android.geomobile.R;
import com.geospatialcorporation.android.geomobile.models.Layers.LayerAttributeColumn;
import com.geospatialcorporation.android.geomobile.ui.adapters.recycler.base.GeoHolderBase;
import com.geospatialcorporation.android.geomobile.ui.adapters.recycler.base.GeoRecyclerAdapterBase;

import java.util.List;

import butterknife.InjectView;

/**
 * Created by andre on 6/13/2015.
 */
public class AttributeAdapter extends GeoRecyclerAdapterBase<AttributeAdapter.Holder, LayerAttributeColumn> {

    public AttributeAdapter(Context context, List<LayerAttributeColumn> attributeColumnsList){
        super(context, attributeColumnsList, R.layout.recycler_list_layer_attribute_columns, Holder.class);
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        setView(parent, viewType);
        return new Holder(mView);
    }

    protected class Holder extends GeoHolderBase<LayerAttributeColumn> {

        @InjectView(R.id.nameColumn) TextView mNameColumn;
        @InjectView(R.id.typeColumn) TextView mTypeColumn;
        @InjectView(R.id.defaultValue) TextView mDefaultValue;
        @InjectView(R.id.hidden) CheckBox mHidden;


        public Holder(View itemView) {
            super(itemView);
        }

        public void bind(LayerAttributeColumn column){
            mDefaultValue.setText(column.getDefaultValue());
            mTypeColumn.setText(column.getDataTypeViewName());
            mNameColumn.setText(column.getName());
            mHidden.setChecked(column.getIsHidden());
        }

    }
}
