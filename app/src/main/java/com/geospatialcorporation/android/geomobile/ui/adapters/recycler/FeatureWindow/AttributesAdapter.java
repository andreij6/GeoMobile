package com.geospatialcorporation.android.geomobile.ui.adapters.recycler.FeatureWindow;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.geospatialcorporation.android.geomobile.R;
import com.geospatialcorporation.android.geomobile.ui.adapters.recycler.base.GeoHolderBase;
import com.geospatialcorporation.android.geomobile.ui.adapters.recycler.base.GeoRecyclerAdapterBase;
import com.geospatialcorporation.android.geomobile.ui.fragments.detail_fragment.feature_window_tabs.FeatureAttributesTab;

import java.util.List;

import butterknife.InjectView;

/**
 * Created by andre on 7/22/2015.
 */
public class AttributesAdapter extends GeoRecyclerAdapterBase<AttributesAdapter.Holder, FeatureAttributesTab.FeatureColumnValue> {

    public AttributesAdapter(Context context, List<FeatureAttributesTab.FeatureColumnValue> data) {
        super(context, data, R.layout.recycler_feature_window_attributes, Holder.class);
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        setView(parent, viewType);
        return new Holder(mView);
    }

    protected class Holder extends GeoHolderBase<FeatureAttributesTab.FeatureColumnValue>{

        @InjectView(R.id.columnName) TextView mColumnName;
        @InjectView(R.id.columnValue) TextView mValue;

        public Holder(View itemView) {
            super(itemView);
        }

        @Override
        public void bind(FeatureAttributesTab.FeatureColumnValue item) {
            mColumnName.setText(item.getColumnName() + ":");
            mValue.setText(item.getColumnValue());
        }
    }
}
