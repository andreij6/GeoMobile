package com.geospatialcorporation.android.geomobile.ui.fragments.detail_fragment.feature_window_tabs;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.geospatialcorporation.android.geomobile.R;
import com.geospatialcorporation.android.geomobile.application;
import com.geospatialcorporation.android.geomobile.library.DI.Analytics.Models.GoogleAnalyticEvent;
import com.geospatialcorporation.android.geomobile.library.DI.TreeServices.Interfaces.IDocumentTreeService;
import com.geospatialcorporation.android.geomobile.library.DI.TreeServices.Interfaces.ILayerTreeService;
import com.geospatialcorporation.android.geomobile.library.DI.UIHelpers.Interfaces.DialogHelpers.IGeneralDialog;
import com.geospatialcorporation.android.geomobile.library.helpers.TableFactory;
import com.geospatialcorporation.android.geomobile.models.MapFeatureDocumentVM;
import com.geospatialcorporation.android.geomobile.models.Query.map.response.featurewindow.MapFeatureFiles;
import com.geospatialcorporation.android.geomobile.models.RemoveMapFeatureDocumentRequest;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

public class FeatureDocumentsTab extends FeatureTabBase {

    int mLayerId;
    String mFeatureId;
    IGeneralDialog mDialog;
    List<MapFeatureDocumentVM> mData;

    ILayerTreeService mLayerTreeService;
    IDocumentTreeService mDocumentTreeService;

    @SuppressWarnings("unused")
    @OnClick(R.id.addDocument)
    public void addDocument(){
        mAnalytics.trackClick(new GoogleAnalyticEvent().ShowAddFeatureDocumentDialog());
        mDialog.addMapFeatureDocument(mLayerId, mFeatureId, mContext, getFragmentManager());
    }

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

    @Bind(R.id.featureWindowDocumentsTable) TableLayout mTableLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mLayout = R.layout.fragment_feature_window_documents_tab;
        mDialog = application.getUIHelperComponent().provideGeneralDialog();
        mDocumentTreeService = application.getTreeServiceComponent().provideDocumentTreeService();
        mAnalytics.trackScreen(new GoogleAnalyticEvent().MapFeatureDocumentsTab());
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    protected void setDataView() {
        List<MapFeatureDocumentVM> data = getDocumentVMList(mResponse.getFeatures().get(0).getFiles());

        mLayerId = mResponse.getId();
        mFeatureId = mResponse.getFeatures().get(0).getId();

        mData = data;

        setDocumentsTable();
    }

    protected void setDocumentsTable(){
        mTableLayout.removeAllViews();

        TableFactory factory = new TableFactory(getActivity(), mTableLayout, mInflater);

        factory.addHeaders(R.layout.template_table_header, "Name", "Size", " ", " ");

        mTableLayout = factory.build();

        for(final MapFeatureDocumentVM doc : mData) {
            TableRow row = new TableRow(mContext);

            TextView name = (TextView)mInflater.inflate(R.layout.template_feature_window_column_tv, null);
            name.setText(doc.getTrimmedName());

            TextView fileSize = (TextView)mInflater.inflate(R.layout.template_feature_window_column_tv, null);
            fileSize.setText(doc.getFormattedSize());

            ImageView download = (ImageView)mInflater.inflate(R.layout.template_feature_window_column_iv, null);
            download.setContentDescription(mContext.getString(R.string.download));
            download.setImageDrawable(ContextCompat.getDrawable(mContext,R.drawable.ic_file_download_black_24dp));
            download.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mDocumentTreeService.download(doc.getId(), doc.getName());
                }
            });

            ImageView remove = (ImageView)mInflater.inflate(R.layout.template_feature_window_column_iv, null);
            remove.setContentDescription(mContext.getString(R.string.remove));
            remove.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_delete_black_24dp));
            remove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    RemoveMapFeatureDocumentRequest request = new RemoveMapFeatureDocumentRequest(doc, mLayerId, mFeatureId, getActivity());

                    mDialog.removeMapFeatureDocument(request);
                }
            });

            row.addView(name);
            row.addView(fileSize);
            row.addView(download);
            row.addView(remove);

            mTableLayout.addView(row);
            mTableLayout.setStretchAllColumns(true);
        }

        mTableLayout.setStretchAllColumns(true);
    }

    protected List<MapFeatureDocumentVM> getDocumentVMList(List<MapFeatureFiles> files) {
        List<MapFeatureDocumentVM> result = new ArrayList<>();

        if(files != null) {
            for (MapFeatureFiles file : files) {
                result.add(new MapFeatureDocumentVM(file.nameWithExt(), file.getSize(), file.getId()));
            }
        }

        return result;
    }
}
