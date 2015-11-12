package com.geospatialcorporation.android.geomobile.ui.fragments.detail_fragment.feature_window_tabs.tablet;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.geospatialcorporation.android.geomobile.R;
import com.geospatialcorporation.android.geomobile.application;
import com.geospatialcorporation.android.geomobile.library.DI.Analytics.Models.GoogleAnalyticEvent;
import com.geospatialcorporation.android.geomobile.library.DI.TreeServices.Interfaces.IDocumentTreeService;
import com.geospatialcorporation.android.geomobile.library.DI.UIHelpers.Interfaces.DialogHelpers.IGeneralDialog;
import com.geospatialcorporation.android.geomobile.library.DocumentSentCallback;
import com.geospatialcorporation.android.geomobile.library.ISendFileCallback;
import com.geospatialcorporation.android.geomobile.library.helpers.DataHelper;
import com.geospatialcorporation.android.geomobile.library.helpers.TableFactory;
import com.geospatialcorporation.android.geomobile.library.rest.TreeService;
import com.geospatialcorporation.android.geomobile.models.Folders.Folder;
import com.geospatialcorporation.android.geomobile.models.MapFeatureDocumentVM;
import com.geospatialcorporation.android.geomobile.models.Query.map.response.featurewindow.MapFeatureFiles;
import com.geospatialcorporation.android.geomobile.models.RemoveMapFeatureDocumentRequest;
import com.geospatialcorporation.android.geomobile.ui.MainActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

public class TabletFeatureDocumentsTab extends TabletFeatureTabBase {

    int mLayerId;
    String mFeatureId;
    IGeneralDialog mDialog;
    List<MapFeatureDocumentVM> mData;
    Folder mParentFolder;

    IDocumentTreeService mDocumentTreeService;

    @Bind(R.id.addDocument) Button mAddDocument;
    @Bind(R.id.featureWindowDocumentsTable) TableLayout mTableLayout;

    @SuppressWarnings("unused")
    @OnClick(R.id.addDocument)
    public void addDocument(){
        mAnalytics.trackClick(new GoogleAnalyticEvent().ShowAddFeatureDocumentDialog());
        mDialog.addMapFeatureDocument(mLayerId, mFeatureId, mContext, getFragmentManager());
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDialog = application.getUIHelperComponent().provideGeneralDialog();
        mDocumentTreeService = application.getTreeServiceComponent().provideDocumentTreeService();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = super.onCreateView(inflater, container, savedInstanceState);

        mAnalytics.trackScreen(new GoogleAnalyticEvent().MapFeatureDocumentsTab());

        return v;
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_feature_window_documents_tab_tablet;
    }

    @Override
    protected void setDataView() {
        List<MapFeatureDocumentVM> data = getDocumentVMList(mResponse.getFeatures().get(0).getFiles());

        mLayerId = mResponse.getId();
        mFeatureId = mResponse.getFeatures().get(0).getId();

        mParentFolder =  mFolderTreeService.getParentFolderByLayerId(mResponse.getId());

        if(mParentFolder != null) {
            if (!mParentFolder.isEditable()) {
                mAddDocument.setVisibility(View.GONE);
            }
        }

        mData = data;

        setDocumentsTable();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            new GetLibraryImportFolderTask(requestCode, resultCode, data).execute();
        }

    }

    protected void setDocumentsTable(){
        mTableLayout.removeAllViews();

        TableFactory factory = new TableFactory(getActivity(), mTableLayout, mInflater);

        String[] headers;

        if(mParentFolder != null && mParentFolder.isEditable()) {
            headers = new String[]{"Name", "Size", " ", " "};
        } else {
            headers = new String[]{"Name", "Size", " "};
        }

        factory.addHeaders(R.layout.template_table_header, headers);

        mTableLayout = factory.build();

        for(final MapFeatureDocumentVM doc : mData) {
            TableRow row = new TableRow(mContext);

            TextView name = (TextView)mInflater.inflate(R.layout.template_feature_window_column_tv, null);
            name.setText(doc.getTrimmedName());

            TextView fileSize = (TextView)mInflater.inflate(R.layout.template_feature_window_column_tv, null);
            fileSize.setText(doc.getFormattedSize());

            ImageView download = (ImageView)mInflater.inflate(R.layout.template_feature_window_column_iv, null);
            download.setContentDescription(mContext.getString(R.string.download));
            download.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_file_download_black_24dp));
            download.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mDocumentTreeService.download(doc.getId(), doc.getName());
                }
            });

            row.addView(name);
            row.addView(fileSize);
            row.addView(download);

            if(mParentFolder != null && mParentFolder.isEditable()) {
                ImageView remove = (ImageView) mInflater.inflate(R.layout.template_feature_window_column_iv, null);
                remove.setContentDescription(mContext.getString(R.string.remove));
                remove.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_delete_black_24dp));
                remove.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        RemoveMapFeatureDocumentRequest request = new RemoveMapFeatureDocumentRequest(doc, mLayerId, mFeatureId, getActivity());

                        mDialog.removeMapFeatureDocument(request);
                    }
                });

                row.addView(remove);
            }



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

    protected class GetLibraryImportFolderTask extends AsyncTask<Void, Void, Folder> {

        int RequestCode;
        int ResultCode;
        Intent Data;
        TreeService mTreeService;
        DataHelper mHelper;
        IDocumentTreeService mUploader;

        public GetLibraryImportFolderTask(int requestCode, int resultCode, Intent data){
            RequestCode = requestCode;
            ResultCode = resultCode;
            Data = data;
            mTreeService = application.getRestAdapter().create(TreeService.class);
            mHelper = new DataHelper();
        }

        @Override
        protected Folder doInBackground(Void... params) {
            List<Folder> documentsTree = mTreeService.getDocuments();

            Folder rootFolder = documentsTree.get(0);

            List<Folder> allFolders = mHelper.getFoldersRecursively(rootFolder, rootFolder.getParent());

            Folder result = null;

            for(Folder folder : allFolders){
                if(folder.getIsImportFolder()){
                    result = folder;
                }
            }

            return result;
        }

        @Override
        protected void onPostExecute(Folder importFolder) {
            mUploader = application.getTreeServiceComponent().provideDocumentTreeService();

            int layerId = application.getFeatureWindowLayerId();
            String featureId = application.getFeatureWindowFeatureId();

            ISendFileCallback sendFileCallback = new DocumentSentCallback(layerId, featureId);

            if (RequestCode == MainActivity.MediaConstants.PICK_FILE_REQUEST_FEATUREWINDOW) {

                mUploader.sendDocument(importFolder, Data.getData(), sendFileCallback);

            }

            if(RequestCode == MainActivity.MediaConstants.PICK_IMAGE_REQUEST_FEATUREWINDOW){

                mUploader.sendPickedImage(importFolder, Data.getData(), sendFileCallback);

            }

            if(RequestCode == MainActivity.MediaConstants.TAKE_IMAGE_REQUEST_FEATUREWINDOW) {

                mUploader.sendTakenImage(importFolder, application.mMediaUri, sendFileCallback);

            }
        }
    }
}
