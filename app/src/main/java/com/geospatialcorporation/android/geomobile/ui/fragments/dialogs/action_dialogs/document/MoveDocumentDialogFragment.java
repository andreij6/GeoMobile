package com.geospatialcorporation.android.geomobile.ui.fragments.dialogs.action_dialogs.document;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.geospatialcorporation.android.geomobile.R;
import com.geospatialcorporation.android.geomobile.application;
import com.geospatialcorporation.android.geomobile.library.helpers.DataHelper;
import com.geospatialcorporation.android.geomobile.library.rest.TreeService;
import com.geospatialcorporation.android.geomobile.models.Document.Document;
import com.geospatialcorporation.android.geomobile.models.Folders.Folder;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by andre on 6/12/2015.
 */
public class MoveDocumentDialogFragment extends DocumentActionDialogBase {
    TreeService mTreeService;
    List<Folder> mFolders;
    Folder mSelected;

    @InjectView(R.id.spinner) Spinner mFolderSpinner;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){

        new GetDocumentFoldersTask().execute();

        View v = getDialogView(R.layout.dialog_document_move);
        ButterKnife.inject(this, v);

        return getDialogBuilder()
                .setMessage(R.string.move_document)
                .setView(v)
                .setPositiveButton(R.string.finish, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (mSelected != null) {
                            Service.move(mDocument, mSelected.getId());
                        }
                    }
                }).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                }).create();
    }

    // add items into spinner dynamically
    public void addItemsOnSpinner() {

        List<String> list = new ArrayList<>();

        list.add("Choose A Folder");

        for(Folder folder : mFolders){
            if(!FolderContainsDocument(folder)) {
                list.add(folder.getName());
            } else {
                mFolders.remove(folder);
            }
        }

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(mContext, android.R.layout.simple_spinner_item, list);

        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        mFolderSpinner.setAdapter(dataAdapter);
    }

    public void addListenerOnSpinnerItemSelection() {
        mFolderSpinner.setOnItemSelectedListener(new CustomOnItemSelectedListener());
    }

    private boolean FolderContainsDocument(Folder folder) {
        List<Document> documents = folder.getDocuments();
        Boolean result = false;
        for(Document document : documents){
            if(document.getId() == mDocument.getId()){
                result = true;
            }
        }

        return result;
    }

    private class GetDocumentFoldersTask extends AsyncTask<Void, Void, List<Folder>> {

        @Override
        protected List<Folder> doInBackground(Void... params) {
            mTreeService = application.getRestAdapter().create(TreeService.class);

            List<Folder> folders = mTreeService.getDocuments();
            DataHelper dh = new DataHelper();
            mFolders = dh.getFoldersRecursively(folders.get(0), null);

            return mFolders;
        }

        @Override
        protected void onPostExecute(List<Folder> folders){
            addItemsOnSpinner();
            addListenerOnSpinnerItemSelection();
        }
    }

    protected class CustomOnItemSelectedListener implements AdapterView.OnItemSelectedListener {

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            if(id != 0) {
                int folderPosition = (int)id - 1;

                mSelected = mFolders.get(folderPosition);
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }
}
