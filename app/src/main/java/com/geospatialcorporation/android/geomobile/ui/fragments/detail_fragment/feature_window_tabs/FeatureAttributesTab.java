package com.geospatialcorporation.android.geomobile.ui.fragments.detail_fragment.feature_window_tabs;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.geospatialcorporation.android.geomobile.R;
import com.geospatialcorporation.android.geomobile.application;
import com.geospatialcorporation.android.geomobile.library.DI.Analytics.Models.GoogleAnalyticEvent;
import com.geospatialcorporation.android.geomobile.library.DI.FeatureWindow.models.FeatureWindowData;
import com.geospatialcorporation.android.geomobile.library.DI.UIHelpers.Interfaces.DialogHelpers.IAttributeDialog;
import com.geospatialcorporation.android.geomobile.library.helpers.GeoDialogHelper;
import com.geospatialcorporation.android.geomobile.models.AttributeValueVM;
import com.geospatialcorporation.android.geomobile.models.Layers.AttributeValue;
import com.geospatialcorporation.android.geomobile.models.Layers.Columns;
import com.geospatialcorporation.android.geomobile.models.Query.map.response.featurewindow.WindowFeatures;
import com.geospatialcorporation.android.geomobile.models.Query.map.response.mapquery.Feature;

import java.util.ArrayList;
import java.util.List;

import butterknife.InjectView;
import butterknife.OnClick;


public class FeatureAttributesTab extends FeatureTabBase {

    private static final String TAG = FeatureAttributesTab.class.getSimpleName();

    @InjectView(R.id.featureWindowAttributesTable) TableLayout mTableLayout;
    AttributeValueVM mData;
    IAttributeDialog mAttributeDialog;

    @OnClick(R.id.edit_attributes)
    public void editAttributes(){
        mAttributeDialog = application.getUIHelperComponent().provideAttributeDialog();

        mAttributeDialog.edit(mData, getActivity(), getFragmentManager());
    }

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

        mData = MatchColumnValues();

        setAttributeTable();
    }

    protected void setAttributeTable() {
        List<AttributeValueVM.Columns> columnsList = mData.getColumns();

        for(AttributeValueVM.Columns keyValue : columnsList) {
            TableRow row = new TableRow(mContext);

            TextView columnName = (TextView)mInflater.inflate(R.layout.template_feature_window_column_tv, null);
            columnName.setText(keyValue.getKey() + ":");

            TextView columnValue = (TextView)mInflater.inflate(R.layout.template_feature_window_column_tv, null);
            columnValue.setText(keyValue.getValue());

            row.addView(columnName);
            row.addView(columnValue);

            mTableLayout.addView(row);
        }

        mTableLayout.setStretchAllColumns(true);
    }

    protected AttributeValueVM MatchColumnValues() {

        List<Columns> columns = mResponse.getColumns();
        WindowFeatures feature = mResponse.getFeatures().get(0);

        String FeatureId = feature.getId();
        List<String> attributes = feature.getAttributes();

        List<AttributeValueVM.Columns> columnValues = new ArrayList<>(columns.size());

        for(int c = 0; c < columns.size(); c++){
            columnValues.add(new AttributeValueVM.Columns(columns.get(c).getName(), attributes.get(c), columns.get(c).getId(), FeatureId, columns.get(c).getDataType()));
        }

        return new AttributeValueVM(mResponse.getId(), columnValues);
    }

}
