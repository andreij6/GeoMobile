package com.geospatialcorporation.android.geomobile.library.DI.Tasks.models;

import com.geospatialcorporation.android.geomobile.models.Document.Document;
import com.geospatialcorporation.android.geomobile.ui.fragments.dialogs.MapFeatureDocumentDialogFragment;

import java.util.List;

public class GetAllDocumentsParam {
    private MapFeatureDocumentDialogFragment mFragment;
    private List<Document> mDocuments;

    public GetAllDocumentsParam(MapFeatureDocumentDialogFragment fragment, List<Document> documents){
        mFragment = fragment;
        mDocuments = documents;
    }

    public MapFeatureDocumentDialogFragment getFragment() {
        return mFragment;
    }

    public List<Document> getDocuments() {
        return mDocuments;
    }
}
