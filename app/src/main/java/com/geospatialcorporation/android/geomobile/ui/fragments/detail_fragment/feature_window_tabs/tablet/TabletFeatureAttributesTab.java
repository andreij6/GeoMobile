package com.geospatialcorporation.android.geomobile.ui.fragments.detail_fragment.feature_window_tabs.tablet;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.geospatialcorporation.android.geomobile.R;
import com.geospatialcorporation.android.geomobile.application;
import com.geospatialcorporation.android.geomobile.library.DI.Analytics.Models.GoogleAnalyticEvent;
import com.geospatialcorporation.android.geomobile.library.DI.UIHelpers.Interfaces.DialogHelpers.IAttributeDialog;
import com.geospatialcorporation.android.geomobile.library.helpers.DataHelper;
import com.geospatialcorporation.android.geomobile.models.AttributeValueVM;
import com.geospatialcorporation.android.geomobile.models.Layers.Columns;
import com.geospatialcorporation.android.geomobile.models.Query.map.response.featurewindow.WindowFeatures;
import com.geospatialcorporation.android.geomobile.ui.fragments.panel_fragments.map_fragment_panels.FeatureAttributePanelFragment;
import com.geospatialcorporation.android.geomobile.ui.fragments.panel_fragments.map_fragment_panels.TabletFeatureAttributePanelFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

public class TabletFeatureAttributesTab extends TabletFeatureTabBase {

    AttributeValueVM mData;
    IAttributeDialog mAttributeDialog;

    @Bind(R.id.featureWindowAttributesTable) TableLayout mTableLayout;

    @OnClick(R.id.edit_attributes)
    public void editAttributes(){
        Fragment f = new TabletFeatureAttributePanelFragment();

        f.setArguments(mData.toBundle());

        application.getMainTabletActivity().getSupportFragmentManager()
                .beginTransaction()
                .addToBackStack("Feature Attributes")
                .replace(R.id.info_frame, f)
                .commit();
    }

    @Override
    protected int getLayoutResId() {
        return  R.layout.fragment_feature_window_attributes_tab_tablet;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAttributeDialog = application.getUIHelperComponent().provideAttributeDialog();

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mAnalytics.trackScreen(new GoogleAnalyticEvent().FeatureAttributesTab());

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
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
            TableRow row = new TableRow(getActivity());

            TextView columnName = (TextView)mInflater.inflate(R.layout.template_feature_window_column_tv, null);
            String cName = DataHelper.trimString(keyValue.getKey(), 20);
            columnName.setText(cName + " :");

            TextView columnValue = (TextView)mInflater.inflate(R.layout.template_feature_window_column_tv, null);
            String valueString = DataHelper.trimString(keyValue.getValue(), 20);

            columnValue.setText(valueString);

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
            if(columns.get(c).getName().equals("Id")) {
                columnValues.add(new AttributeValueVM.Columns(columns.get(c).getName(), attributes.get(c), columns.get(c).getId(), FeatureId, columns.get(c).getDataType(), false));
            } else {
                columnValues.add(new AttributeValueVM.Columns(columns.get(c).getName(), attributes.get(c), columns.get(c).getId(), FeatureId, columns.get(c).getDataType(), true));
            }
        }

        return new AttributeValueVM(mResponse.getId(), columnValues);
    }

}
