package com.geospatialcorporation.android.geomobile.ui.fragments.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.geospatialcorporation.android.geomobile.R;
import com.geospatialcorporation.android.geomobile.application;
import com.geospatialcorporation.android.geomobile.library.DI.Analytics.Models.GoogleAnalyticEvent;
import com.geospatialcorporation.android.geomobile.library.DI.TreeServices.Interfaces.IDocumentTreeService;
import com.geospatialcorporation.android.geomobile.models.Document.Document;
import com.geospatialcorporation.android.geomobile.ui.fragments.dialogs.base.GeoDialogFragmentBase;

public class DownloadDialogFragment extends GeoDialogFragmentBase {
    //region Getters & Setters
    public Document getDocument() {
        return mDocument;
    }

    public void setDocument(Document document) {
        mDocument = document;
    }
    //endregion

    Document mDocument;
    IDocumentTreeService mDocumentTreeService;


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);

        mAnalytics = application.getAnalyticsComponent().provideGeoAnalytics();

        LayoutInflater inflater = LayoutInflater.from(mContext);
        View v = inflater.inflate(R.layout.dialog_download, null);
        TextView documentName = (TextView)v.findViewById(R.id.documentName);
        documentName.setText(mDocument.getNameWithExt());
        builder.setTitle(R.string.download_file);
        builder.setView(v);

        builder.setPositiveButton(R.string.download, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    mAnalytics.trackClick(new GoogleAnalyticEvent().DownloadDocument());

                    mDocumentTreeService = application.getTreeServiceComponent().provideDocumentTreeService();
                    mDocumentTreeService.download(mDocument.getId(), mDocument.getNameWithExt());

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
