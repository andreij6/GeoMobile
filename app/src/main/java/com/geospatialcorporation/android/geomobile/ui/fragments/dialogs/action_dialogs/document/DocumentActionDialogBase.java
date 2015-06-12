package com.geospatialcorporation.android.geomobile.ui.fragments.dialogs.action_dialogs.document;

import android.content.Context;

import com.geospatialcorporation.android.geomobile.library.services.DocumentTreeService;
import com.geospatialcorporation.android.geomobile.models.Document.Document;
import com.geospatialcorporation.android.geomobile.ui.fragments.dialogs.base.GeoDialogFragmentBase;

/**
 * Created by andre on 6/12/2015.
 */
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
    DocumentTreeService Service;

    public void init(Context context, Document document){
        setContext(context);
        setDocument(document);
        Service = new DocumentTreeService();
    }
}
