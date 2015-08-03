package com.geospatialcorporation.android.geomobile.library.DI.UIHelpers.Interfaces.DialogHelpers;

import android.content.Context;
import android.support.v4.app.FragmentManager;

import com.geospatialcorporation.android.geomobile.models.Document.Document;
import com.geospatialcorporation.android.geomobile.models.Folders.Folder;

public interface IDocumentDialog extends IEntityDialog<Document> {

    void move(Document document, Context context, FragmentManager manager);

    void uploadImage(Folder parent, Context context, FragmentManager manager);
}
