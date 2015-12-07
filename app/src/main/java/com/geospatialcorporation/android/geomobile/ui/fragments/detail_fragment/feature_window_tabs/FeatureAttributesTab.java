package com.geospatialcorporation.android.geomobile.ui.fragments.detail_fragment.feature_window_tabs;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.geospatialcorporation.android.geomobile.R;
import com.geospatialcorporation.android.geomobile.application;
import com.geospatialcorporation.android.geomobile.library.DI.Analytics.Models.GoogleAnalyticEvent;
import com.geospatialcorporation.android.geomobile.library.DI.TreeServices.Interfaces.IFolderTreeService;
import com.geospatialcorporation.android.geomobile.library.DI.UIHelpers.Interfaces.DialogHelpers.IAttributeDialog;
import com.geospatialcorporation.android.geomobile.library.constants.GeoPanel;
import com.geospatialcorporation.android.geomobile.library.helpers.DataHelper;
import com.geospatialcorporation.android.geomobile.library.panelmanager.PanelManager;
import com.geospatialcorporation.android.geomobile.models.AttributeValueVM;
import com.geospatialcorporation.android.geomobile.models.Folders.Folder;
import com.geospatialcorporation.android.geomobile.models.Layers.Columns;
import com.geospatialcorporation.android.geomobile.models.Query.map.response.featurewindow.WindowFeatures;
import com.geospatialcorporation.android.geomobile.ui.fragments.panel_fragments.map_fragment_panels.FeatureAttributePanelFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;


public class FeatureAttributesTab extends FeatureTabBase {

    private static final String TAG = FeatureAttributesTab.class.getSimpleName();

    @Bind(R.id.featureWindowAttributesTable) TableLayout mTableLayout;
    @Bind(R.id.moreInfo) TextView mMoreInfo;
    @Bind(R.id.edit_attributes) Button mEditAttributs;

    AttributeValueVM mData;
    IAttributeDialog mAttributeDialog;
    IFolderTreeService mFolderTreeService;


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

    @OnClick(R.id.edit_attributes)
    public void editAttributes(){
        //mAttributeDialog = application.getUIHelperComponent().provideAttributeDialog();

        //mAttributeDialog.edit(mData, getActivity(), getFragmentManager());
        Fragment f = new FeatureAttributePanelFragment();

        f.setArguments(mData.toBundle());

        if(mIsLandscape){
            application.getMainActivity()
                    .setDetailFrame(f);
        } else {

            application.getMainActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .addToBackStack("Feature Attributes")
                    .replace(R.id.slider_content, f)
                    .commit();

            mPanelManager.expand();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mLayout = R.layout.fragment_feature_window_attributes_tab;
        mAnalytics.trackScreen(new GoogleAnalyticEvent().FeatureAttributesTab());
        mFolderTreeService = application.getTreeServiceComponent().provideFolderTreeService();

        View v = super.onCreateView(inflater, container, savedInstanceState);

        if(!mIsLandscape){
            mPanelManager = new PanelManager(GeoPanel.MAP);
            mPanelManager.touch(true);
        }

        return v;
    }

    protected void setDataView() {
        if(mResponse == null || mResponse.getFeatures() == null || mResponse.getFeatures().get(0) == null){
            return;
        }

        Folder folder = mFolderTreeService.getParentFolderByLayerId(mResponse.getId());
        //
        if(folder != null){
            if(!folder.isEditable()) {
                mEditAttributs.setVisibility(View.GONE);
            }
        }

        mData = MatchColumnValues();

        setAttributeTable();
    }

    protected void setAttributeTable() {
        List<AttributeValueVM.Columns> columnsList = mData.getColumns();

        for(AttributeValueVM.Columns keyValue : columnsList) {
            TableRow row = new TableRow(mContext);

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
