package com.geospatialcorporation.android.geomobile.ui.fragments.dialogs.action_dialogs.document;

import android.content.Context;

import com.geospatialcorporation.android.geomobile.application;
import com.geospatialcorporation.android.geomobile.library.DI.TreeServices.Interfaces.IDocumentTreeService;
import com.geospatialcorporation.android.geomobile.models.Document.Document;
import com.geospatialcorporation.android.geomobile.ui.fragments.dialogs.base.GeoDialogFragmentBase;

public class DocumentActionDialogBase extends GeoDialogFragmentBase {

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

    public void init(Context context, Document document){
        setContext(context);
        setDocument(document);
        Service = application.getTreeServiceComponent().provideDocumentTreeService();
    }
}
