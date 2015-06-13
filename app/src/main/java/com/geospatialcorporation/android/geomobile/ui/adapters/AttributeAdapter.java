package com.geospatialcorporation.android.geomobile.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.geospatialcorporation.android.geomobile.R;
import com.geospatialcorporation.android.geomobile.models.Layers.LayerAttributeColumns;

import org.w3c.dom.Attr;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by andre on 6/13/2015.
 */
public class AttributeAdapter extends RecyclerView.Adapter<AttributeAdapter.ViewHolder> {

    Context mContext;
    List<LayerAttributeColumns> mData;

    public AttributeAdapter(Context context, List<LayerAttributeColumns> attributeColumnsList){
        mContext = context;
        mData = attributeColumnsList;
    }

    @Override
    public AttributeAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_list_columns, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(AttributeAdapter.ViewHolder holder, int position) {
        holder.bindColumn(mData.get(position));
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

        @InjectView(R.id.nameColumn) TextView mNameColumn;
        @InjectView(R.id.typeColumn) TextView mTypeColumn;
        @InjectView(R.id.defaultValue) TextView mDefaultValue;


        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.inject(this, itemView);
        }

        public void bindColumn(LayerAttributeColumns column){
            mDefaultValue.setText(column.getDefaultValue());
            mTypeColumn.setText(column.getDataTypeViewName());
            mNameColumn.setText(column.getName());
        }

    }
}
