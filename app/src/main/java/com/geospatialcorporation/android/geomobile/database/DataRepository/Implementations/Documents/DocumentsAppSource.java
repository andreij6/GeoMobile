package com.geospatialcorporation.android.geomobile.database.DataRepository.Implementations.Documents;

import com.geospatialcorporation.android.geomobile.application;
import com.geospatialcorporation.android.geomobile.database.DataRepository.Implementations.AppSourceBase;
import com.geospatialcorporation.android.geomobile.models.Document.Document;

import java.util.HashMap;

/**
 * Created by andre on 6/3/2015.
 */
public class DocumentsAppSource extends AppSourceBase<Document> {

    HashMap<Integer, Document> Data;

    public DocumentsAppSource(){
        super(application.getDocumentHashMap());
    }
}
