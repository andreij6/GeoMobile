package com.geospatialcorporation.android.geomobile.ui.fragments.detail_fragment.feature_window_tabs;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.geospatialcorporation.android.geomobile.R;
import com.geospatialcorporation.android.geomobile.library.DI.FeatureWindow.DaggerFeatureWindowComponent;
import com.geospatialcorporation.android.geomobile.library.DI.FeatureWindow.FeatureWindowComponent;
import com.geospatialcorporation.android.geomobile.library.DI.FeatureWindow.IFeatureWindowDataParser;
import com.geospatialcorporation.android.geomobile.library.DI.FeatureWindow.models.FeatureWindowData;
import com.geospatialcorporation.android.geomobile.models.Query.map.response.featurewindow.FeatureQueryResponse;
import com.geospatialcorporation.android.geomobile.models.Query.map.response.featurewindow.ParcelableFeatureQueryResponse;
import com.geospatialcorporation.android.geomobile.ui.fragments.GeoViewFragmentBase;

import java.util.List;

import butterknife.ButterKnife;

/**
 * Created by andre on 7/7/2015.
 */
public abstract class FeatureTabBase extends GeoViewFragmentBase {

    FeatureQueryResponse mResponse;
    int mLayout;
    IFeatureWindowDataParser DataParser;
    protected LayoutInflater mInflater;
    Context mContext;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(mLayout, container, false);
        ButterKnife.inject(this, v);
        mInflater = inflater;
        handleArgs();
        mContext = getActivity();
        FeatureWindowComponent component = DaggerFeatureWindowComponent.builder().build();

        DataParser = component.provideDataParser();

        setDataView();

        return v;
    }

    protected void handleArgs() {
        Bundle args = getArguments();

        ParcelableFeatureQueryResponse data = args.getParcelable(ParcelableFeatureQueryResponse.FEATURE_QUERY_RESPONSE);

        mResponse = data.getFeatureQueryResponse().get(0); //should only be one for a feature window

    }

    protected abstract void setDataView();

    protected void setTable(List<FeatureWindowData.KeyValue> data, TableLayout table, String seperator){

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
    }
}
