package com.geospatialcorporation.android.geomobile.ui.fragments.detail_fragment.feature_window_tabs;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.geospatialcorporation.android.geomobile.R;
import com.geospatialcorporation.android.geomobile.models.Layers.Columns;
import com.geospatialcorporation.android.geomobile.ui.adapters.recycler.FeatureWindow.AttributesAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.InjectView;


public class FeatureAttributesTab extends FeatureTabBase {

    @InjectView(R.id.featureWindowAttributesRecycler) RecyclerView mRecyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mLayout = R.layout.fragment_feature_window_attributes_tab;
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    protected void setRecycler() {
        if(mResponse == null || mResponse.getFeatures() == null || mResponse.getFeatures().get(0) == null){
            return;
        }

        List<FeatureColumnValue> cvs = MatchColumnValues(mResponse.getColumns(), mResponse.getFeatures().get(0).getAttributes());

        //TODO: code for the recycler view
        RecyclerView.LayoutManager manager = new LinearLayoutManager(getActivity());

        mRecyclerView.setLayoutManager(manager);

        AttributesAdapter adapter = new AttributesAdapter(getActivity(), cvs);

        mRecyclerView.setAdapter(adapter);
    }

    protected List<FeatureColumnValue> MatchColumnValues(List<Columns> columns, List<String> attributes) {
        List<FeatureColumnValue> columnValues = new ArrayList<>(columns.size());

        for(int c = 0; c < columns.size(); c++){
            columnValues.add(new FeatureColumnValue(columns.get(c).getName(), attributes.get(c)));
        }

        return columnValues;
    }

    public class FeatureColumnValue {
        //region Gs & Ss
        public String getColumnName() {
            return mColumnName;
        }

        public void setColumnName(String columnName) {
            mColumnName = columnName;
        }

        public String getColumnValue() {
            return mColumnValue;
        }

        public void setColumnValue(String columnValue) {
            mColumnValue = columnValue;
        }
        //endregion

        String mColumnName;
        String mColumnValue;

        public FeatureColumnValue(String columnName, String value){
            mColumnName = columnName;
            mColumnValue = value;
        }
    }
}
