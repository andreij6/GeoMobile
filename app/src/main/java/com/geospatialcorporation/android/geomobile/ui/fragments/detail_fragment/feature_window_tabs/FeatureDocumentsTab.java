package com.geospatialcorporation.android.geomobile.ui.fragments.detail_fragment.feature_window_tabs;

import android.os.Bundle;
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
import com.geospatialcorporation.android.geomobile.library.DI.UIHelpers.Interfaces.DialogHelpers.IGeneralDialog;
import com.geospatialcorporation.android.geomobile.library.helpers.FileSizeFormatter;
import com.geospatialcorporation.android.geomobile.library.helpers.GeoDialogHelper;
import com.geospatialcorporation.android.geomobile.library.helpers.TableFactory;
import com.geospatialcorporation.android.geomobile.models.Query.map.response.featurewindow.MapFeatureFiles;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.InjectView;
import butterknife.OnClick;

public class FeatureDocumentsTab extends FeatureTabBase {

    int mLayerId;
    String mFeatureId;
    IGeneralDialog mDialog;
    List<FeatureWindowData.KeyValue> mData;

    @SuppressWarnings("unused")
    @OnClick(R.id.addDocument)
    public void addDocument(){
        mAnalytics.trackClick(new GoogleAnalyticEvent().ShowAddFeatureDocumentDialog());
        mDialog.addMapFeatureDocument(mLayerId, mFeatureId, mContext, getFragmentManager());
    }

    @InjectView(R.id.featureWindowDocumentsTable) TableLayout mTableLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mLayout = R.layout.fragment_feature_window_documents_tab;
        mDialog = application.getUIHelperComponent().provideGeneralDialog();
        mAnalytics.trackScreen(new GoogleAnalyticEvent().MapFeatureDocumentsTab());
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    protected void setDataView() {
        List<FeatureWindowData.KeyValue> data = getDocumentVMList(mResponse.getFeatures().get(0).getFiles());

        mLayerId = mResponse.getId();
        mFeatureId = mResponse.getFeatures().get(0).getId();

        mData = data;

        setDocumentsTable();
    }

    protected void setDocumentsTable(){
        mTableLayout.removeAllViews();

        TableFactory factory = new TableFactory(getActivity(), mTableLayout, mInflater);

        factory.addHeaders(R.layout.template_table_header, "Name", "Size", " ");

        mTableLayout = factory.build();

        for(FeatureWindowData.KeyValue keyValue : mData) {
            TableRow row = new TableRow(mContext);

            TextView name = (TextView)mInflater.inflate(R.layout.template_feature_window_column_tv, null);
            name.setText(keyValue.getKey());

            TextView fileSize = (TextView)mInflater.inflate(R.layout.template_feature_window_column_tv, null);
            String fileSizeStr = FileSizeFormatter.format(keyValue.getValue());
            fileSize.setText(fileSizeStr);

            TextView remove = (TextView)mInflater.inflate(R.layout.template_feature_window_column_tv, null);
            //remove.setText(R.string.remove);
            remove.setText("");

            row.addView(name);
            row.addView(fileSize);
            row.addView(remove);

            mTableLayout.addView(row);
        }
    }



    protected List<FeatureWindowData.KeyValue> getDocumentVMList(List<MapFeatureFiles> files) {
        List<FeatureWindowData.KeyValue> result = new ArrayList<>();

        if(files != null) {
            for (MapFeatureFiles file : files) {
                result.add(new FeatureWindowData.KeyValue(file.nameWithExt(), file.getSize() + ""));
            }
        }

        return result;
    }
}
