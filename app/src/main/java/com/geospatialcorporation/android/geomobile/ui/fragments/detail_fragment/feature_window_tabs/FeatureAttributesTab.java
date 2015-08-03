package com.geospatialcorporation.android.geomobile.ui.fragments.detail_fragment.feature_window_tabs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;

import com.geospatialcorporation.android.geomobile.R;
import com.geospatialcorporation.android.geomobile.library.DI.Analytics.Models.GoogleAnalyticEvent;
import com.geospatialcorporation.android.geomobile.library.DI.FeatureWindow.models.FeatureWindowData;
import com.geospatialcorporation.android.geomobile.models.Layers.Columns;

import java.util.ArrayList;
import java.util.List;

import butterknife.InjectView;


public class FeatureAttributesTab extends FeatureTabBase {

    @InjectView(R.id.featureWindowAttributesTable) TableLayout mTableLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mLayout = R.layout.fragment_feature_window_attributes_tab;
        mAnalytics.trackScreen(new GoogleAnalyticEvent().FeatureAttributesTab());
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    protected void setDataView() {
        if(mResponse == null || mResponse.getFeatures() == null || mResponse.getFeatures().get(0) == null){
            return;
        }

        List<FeatureWindowData.KeyValue> data = MatchColumnValues(mResponse.getColumns(), mResponse.getFeatures().get(0).getAttributes());

        setTable(data, mTableLayout, ":");
    }

    protected List<FeatureWindowData.KeyValue> MatchColumnValues(List<Columns> columns, List<String> attributes) {
        List<FeatureWindowData.KeyValue> columnValues = new ArrayList<>(columns.size());

        for(int c = 0; c < columns.size(); c++){
            columnValues.add(new FeatureWindowData.KeyValue(columns.get(c).getName(), attributes.get(c)));
        }

        return columnValues;
    }

}
