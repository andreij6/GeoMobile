package com.geospatialcorporation.android.geomobile.ui.fragments.detail_fragment.feature_window_tabs;

import android.os.Bundle;
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
import butterknife.OnClick;

/**
 * Created by andre on 7/6/2015.
 */
public class FeatureMapInfoTab extends FeatureTabBase {
    private static final String TAG = FeatureMapInfoTab.class.getSimpleName();

    @Bind(R.id.mapInfoTable) TableLayout mTableLayout;
    @Bind(R.id.moreInfo) TextView mMoreInfo;

    @OnClick(R.id.moreInfo)
    public void moreInfo(){
        if(mPanelManager.isExpanded()){
            mPanelManager.halfAnchor();
            mPanelManager.touch(true);
        } else {
            mPanelManager.expand();
            mPanelManager.touch(true);
        }

    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mLayout = R.layout.fragment_feature_window_mapinfo_tab;
        mAnalytics.trackScreen(new GoogleAnalyticEvent().MapInfoTab());
        View v = super.onCreateView(inflater, container, savedInstanceState);


        return v;
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
