package com.geospatialcorporation.android.geomobile.ui.fragments.dialogs.action_dialogs.document;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.geospatialcorporation.android.geomobile.R;
import com.geospatialcorporation.android.geomobile.application;
import com.geospatialcorporation.android.geomobile.library.DI.Tasks.Interfaces.IGetDocumentsTask;
import com.geospatialcorporation.android.geomobile.library.helpers.DataHelper;
import com.geospatialcorporation.android.geomobile.library.helpers.ItemSelectedListener;
import com.geospatialcorporation.android.geomobile.library.rest.TreeService;
import com.geospatialcorporation.android.geomobile.models.Document.Document;
import com.geospatialcorporation.android.geomobile.models.Folders.Folder;
import com.geospatialcorporation.android.geomobile.ui.Interfaces.ISpinnerListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class MoveDocumentDialogFragment extends DocumentActionDialogBase implements ISpinnerListener<Folder> {
    TreeService mTreeService;
    List<Folder> mFolders;
    Folder mSelected;

    @InjectView(R.id.spinner) Spinner mFolderSpinner;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){

        IGetDocumentsTask task = application.getTasksComponent().provideGetDocumentsTask();
        task.getDocumentFolders(this);

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
                        } else {
                            Toaster("Selected was null");
                        }
                    }
                }).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                }).create();
    }

    public void addItemsOnSpinner(List<Folder> folders) {

        mFolders = folders;

        List<String> list = new ArrayList<>();

        list.add("Choose A Folder");

        if(mFolders != null) {
            for (Folder folder : mFolders) {
                if (!FolderContainsDocument(folder)) {
                    list.add(folder.getName());
                } else {
                    mFolders.remove(folder);
                }
            }
        }

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(mContext, android.R.layout.simple_spinner_item, list);

        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        mFolderSpinner.setAdapter(dataAdapter);
    }

    public void addListenerOnSpinnerItemSelection() {
        mFolderSpinner.setOnItemSelectedListener(new ItemSelectedListener<>(this));
    }

    protected boolean FolderContainsDocument(Folder folder) {
        List<Document> documents = folder.getDocuments();
        Boolean result = false;
        for(Document document : documents){
            if(document.getId() == mDocument.getId()){
                result = true;
            }
        }

        return result;
    }

    @Override
    public void setSelected(Folder selected) {
        mSelected = selected;
    }

    @Override
    public List<Folder> getData() {
        return mFolders;
    }
}
