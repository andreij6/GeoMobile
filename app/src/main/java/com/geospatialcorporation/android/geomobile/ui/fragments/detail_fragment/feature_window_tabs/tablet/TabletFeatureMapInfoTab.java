package com.geospatialcorporation.android.geomobile.ui.fragments.detail_fragment.feature_window_tabs.tablet;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.geospatialcorporation.android.geomobile.R;
import com.geospatialcorporation.android.geomobile.library.DI.Analytics.Models.GoogleAnalyticEvent;
import com.geospatialcorporation.android.geomobile.library.DI.FeatureWindow.Implementations.FeatureWindowDataParser;
import com.geospatialcorporation.android.geomobile.library.DI.FeatureWindow.models.FeatureWindowData;

import java.util.Collections;
import java.util.List;

import butterknife.Bind;

public class TabletFeatureMapInfoTab extends TabletFeatureTabBase {

    private static final String TAG = TabletFeatureMapInfoTab.class.getSimpleName();

    @Bind(R.id.mapInfoTable) TableLayout mTableLayout;

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_feature_window_mapinfo_tab;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mAnalytics.trackScreen(new GoogleAnalyticEvent().MapInfoTab());

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    protected void setDataView() {
        FeatureWindowData data = DataParser.parseResponse(mResponse, FeatureWindowDataParser.MAPINFO);

        setTable(data.getList(), mTableLayout, ":");
    }

    protected void setTable(List<FeatureWindowData.KeyValue> data, TableLayout table, String seperator){

        Collections.sort(data);

        for(FeatureWindowData.KeyValue keyValue : data) {
            TableRow row = new TableRow(mContext);

            TextView columnName = (TextView)mInflater.inflate(R.layout.template_feature_window_column_tv, null);
            columnName.setText(keyValue.getKey() + seperator);

            TextView columnValue = (TextView)mInflater.inflate(R.layout.template_feature_window_column_tv, null);
            columnValue.setText(keyValue.getValue());

            row.addView(columnName);
            row.addView(columnValue);

            table.addView(row);
        }

        table.setStretchAllColumns(true);
    }
}
