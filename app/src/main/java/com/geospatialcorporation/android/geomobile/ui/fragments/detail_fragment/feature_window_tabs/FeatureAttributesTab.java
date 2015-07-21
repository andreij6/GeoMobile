package com.geospatialcorporation.android.geomobile.ui.fragments.detail_fragment.feature_window_tabs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.geospatialcorporation.android.geomobile.R;
import com.geospatialcorporation.android.geomobile.models.Layers.Columns;

import java.util.ArrayList;
import java.util.List;


public class FeatureAttributesTab extends FeatureTabBase {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mLayout = R.layout.fragment_feature_window_attributes_tab;
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    protected void setRecycler() {
        List<FeatureColumnValue> cvs = MatchColumnValues(mResponse.getColumns(), mResponse.getFeatures().get(0).getAttributes());

        //TODO: code for the recycler view
    }

    protected List<FeatureColumnValue> MatchColumnValues(List<Columns> columns, List<String> attributes) {
        List<FeatureColumnValue> columnValues = new ArrayList<>(columns.size());

        for(int c = 0; c < columns.size(); c++){
            columnValues.add(new FeatureColumnValue(columns.get(c).getName(), attributes.get(c)));
        }

        return columnValues;
    }

    public class FeatureColumnValue {
        String mColumnName;
        String mColumnValue;

        public FeatureColumnValue(String columnName, String value){
            mColumnName = columnName;
            mColumnValue = value;
        }
    }
}
