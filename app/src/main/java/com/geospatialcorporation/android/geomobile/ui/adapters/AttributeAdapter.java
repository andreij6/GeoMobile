package com.geospatialcorporation.android.geomobile.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.geospatialcorporation.android.geomobile.R;
import com.geospatialcorporation.android.geomobile.models.Layers.LayerAttributeColumns;
import com.geospatialcorporation.android.geomobile.ui.adapters.base.GeoHolderBase;
import com.geospatialcorporation.android.geomobile.ui.adapters.base.GeoRecyclerAdapterBase;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by andre on 6/13/2015.
 */
public class AttributeAdapter extends GeoRecyclerAdapterBase<AttributeAdapter.Holder, LayerAttributeColumns> {

    public AttributeAdapter(Context context, List<LayerAttributeColumns> attributeColumnsList){
        super(context, attributeColumnsList, R.layout.recycler_list_columns, Holder.class);
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        setView(parent, viewType);
        return new Holder(mView);
    }

    protected class Holder extends GeoHolderBase<LayerAttributeColumns> {

        @InjectView(R.id.nameColumn) TextView mNameColumn;
        @InjectView(R.id.typeColumn) TextView mTypeColumn;
        @InjectView(R.id.defaultValue) TextView mDefaultValue;


        public Holder(View itemView) {
            super(itemView);
        }

        public void bind(LayerAttributeColumns column){
            mDefaultValue.setText(column.getDefaultValue());
            mTypeColumn.setText(column.getDataTypeViewName());
            mNameColumn.setText(column.getName());
        }

    }
}
