package com.geospatialcorporation.android.geomobile.ui.fragments.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.geospatialcorporation.android.geomobile.R;
import com.geospatialcorporation.android.geomobile.application;
import com.geospatialcorporation.android.geomobile.library.helpers.DataHelper;
import com.geospatialcorporation.android.geomobile.library.rest.TreeService;
import com.geospatialcorporation.android.geomobile.library.services.FolderTreeService;
import com.geospatialcorporation.android.geomobile.library.services.LayerTreeService;
import com.geospatialcorporation.android.geomobile.models.Document.Document;
import com.geospatialcorporation.android.geomobile.models.Folders.Folder;
import com.geospatialcorporation.android.geomobile.ui.fragments.dialogs.base.GeoDialogFragmentBase;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class MapFeatureDocumentDialogFragment extends GeoDialogFragmentBase {

    FolderTreeService mFolderTreeService;
    LayerTreeService mLayerTreeService;
    List<Document> mDocuments;
    Document mSelected;
    int mLayerId;
    String mFeatureId;
    @InjectView(R.id.document_spinner) Spinner mDocumentSpinner;

    public void init(Context context, int layerId, String featureId) {
        mContext = context;
        mLayerId = layerId;
        mFeatureId = featureId;
        mLayerTreeService = new LayerTreeService();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        new GetDocumentsTask().execute();

        AlertDialog.Builder builder = getDialogBuilder();

        View v = getDialogView(R.layout.dialog_mapfeature_document);
        ButterKnife.inject(this, v);

        builder.setTitle(R.string.add_mapfeature_document)
                .setView(v)
                .setPositiveButton(R.string.add, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mLayerTreeService.addMapFeatureDocument(mLayerId, mFeatureId, mSelected.getId());
                        dialog.cancel();
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        return builder.create();
    }

    public void addListenerOnSpinnerItemSelection() {
        mDocumentSpinner.setOnItemSelectedListener(new CustomOnDocSelectedListener());
    }

    public void addItemsOnSpinner() {

        List<String> list = new ArrayList<>();

        list.add("Choose A Document");

        for(Document doc : mDocuments){
            list.add(doc.getName());
        }

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(mContext, android.R.layout.simple_spinner_item, list);

        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        mDocumentSpinner.setAdapter(dataAdapter);
    }

    private class GetDocumentsTask extends AsyncTask<Void, Void, List<Document>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mFolderTreeService = new FolderTreeService();
            mDocuments = new ArrayList<>();
        }

        @Override
        protected List<Document> doInBackground(Void... params) {
            TreeService treeService = application.getRestAdapter().create(TreeService.class);

            List<Folder> folders = treeService.getDocuments();
            DataHelper dh = new DataHelper();
            List<Folder> allDocFolders = dh.getFoldersRecursively(folders.get(0), null);

            for(Folder folder : allDocFolders){
                List<Document> documents = mFolderTreeService.getDocumentsByFolder(folder.getId());

                mDocuments.addAll(documents);
            }

            return mDocuments;
        }

        @Override
        protected void onPostExecute(List<Document> folders){
            addItemsOnSpinner();
            addListenerOnSpinnerItemSelection();
        }
    }

    protected class CustomOnDocSelectedListener implements AdapterView.OnItemSelectedListener {

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            if(id != 0) {
                int docPosition = (int)id - 1;

                mSelected = mDocuments.get(docPosition);
            }


        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }
}
