package com.geospatialcorporation.android.geomobile.ui.fragments.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.geospatialcorporation.android.geomobile.R;
import com.geospatialcorporation.android.geomobile.library.rest.DownloadService;
import com.geospatialcorporation.android.geomobile.models.Library.Document;
import com.geospatialcorporation.android.geomobile.ui.viewmodels.ListItem;

public class DownloadDialogFragment extends DialogFragment {
    //region Getters & Setters
    public Document getDocument() {
        return mDocument;
    }

    public void setDocument(Document document) {
        mDocument = document;
    }

    public Context getContext() {
        return mContext;
    }

    public void setContext(Context context) {
        mContext = context;
    }



    public ListItem getListItem() {
        return mListItem;
    }

    public void setListItem(ListItem listItem) {
        mListItem = listItem;
    }
    //endregion

    Document mDocument;
    Context mContext;
    ListItem mListItem;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);

        LayoutInflater inflater = LayoutInflater.from(mContext);
        View v = inflater.inflate(R.layout.dialog_download, null);
        TextView documentName = (TextView)v.findViewById(R.id.documentName);
        documentName.setText(mDocument.getName());

        builder.setView(v);

        builder.setPositiveButton(R.string.download, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    new DownloadService(mDocument.getId(), mDocument.getName());
                }
            })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        DownloadDialogFragment.this.getDialog().cancel();
                    }
                });

        return builder.create();
    }
}
