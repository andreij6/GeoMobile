package com.geospatialcorporation.android.geomobile.ui.fragments.dialogs.action_dialogs.document;

import android.content.Context;
import android.os.Bundle;

import com.geospatialcorporation.android.geomobile.application;
import com.geospatialcorporation.android.geomobile.library.DI.TreeServices.Interfaces.IDocumentTreeService;
import com.geospatialcorporation.android.geomobile.models.Document.Document;
import com.geospatialcorporation.android.geomobile.ui.fragments.dialogs.base.GeoDialogFragmentBase;

public class DocumentActionDialogBase extends GeoDialogFragmentBase {

    private static final String TAG = DocumentActionDialogBase.class.getSimpleName();
    protected static final String DOCUMENT_DIALOG = "doc_dialog";

    //region Getters & Setters
    public Document getDocument() {
        return mDocument;
    }

    public void setDocument(Document document) {
        mDocument = document;
    }
    //endregion

    Document mDocument;
    IDocumentTreeService Service;

    public void init(Document document){
        setContext(getActivity());
        setDocument(document);
        Service = application.getTreeServiceComponent().provideDocumentTreeService();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(DOCUMENT_DIALOG, mDocument);
        super.onSaveInstanceState(outState);
    }
}
