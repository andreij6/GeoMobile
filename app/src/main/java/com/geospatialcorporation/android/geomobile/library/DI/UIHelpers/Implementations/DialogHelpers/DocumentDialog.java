package com.geospatialcorporation.android.geomobile.library.DI.UIHelpers.Implementations.DialogHelpers;

import android.content.Context;
import android.support.v4.app.FragmentManager;

import com.geospatialcorporation.android.geomobile.library.DI.UIHelpers.Interfaces.DialogHelpers.IDocumentDialog;
import com.geospatialcorporation.android.geomobile.models.Document.Document;
import com.geospatialcorporation.android.geomobile.models.Folders.Folder;
import com.geospatialcorporation.android.geomobile.ui.fragments.dialogs.UploadImageDialogFragment;
import com.geospatialcorporation.android.geomobile.ui.fragments.dialogs.action_dialogs.DocumentActionsDialogFragment;
import com.geospatialcorporation.android.geomobile.ui.fragments.dialogs.action_dialogs.document.DeleteDocumentDialogFragment;
import com.geospatialcorporation.android.geomobile.ui.fragments.dialogs.action_dialogs.document.MoveDocumentDialogFragment;
import com.geospatialcorporation.android.geomobile.ui.fragments.dialogs.action_dialogs.document.RenameDocumentDialogFragment;

public class DocumentDialog implements IDocumentDialog {

    @Override
    public void move(Document document, Context context, FragmentManager manager) {
        MoveDocumentDialogFragment move = new MoveDocumentDialogFragment();
        move.init(context, document);
        move.show(manager, "Move Document");
    }

    @Override
    public void uploadImage(Folder parent, Context context, FragmentManager manager) {
        UploadImageDialogFragment u = new UploadImageDialogFragment();
        u.init(context, parent);
        u.show(manager, "Upload Image");
    }

    @Override
    public void delete(Document entity, Context context, FragmentManager manager) {
        DeleteDocumentDialogFragment d = new DeleteDocumentDialogFragment();
        d.init(context, entity);
        d.show(manager, "Delete Document");
    }

    @Override
    public void actions(Document entity, Context context, FragmentManager manager) {
        DocumentActionsDialogFragment dadf = new DocumentActionsDialogFragment();
        dadf.init(context, entity);
        dadf.show(manager, "Document Actions");
    }

    @Override
    public void rename(Document entity, Context context, FragmentManager manager) {
        RenameDocumentDialogFragment rdd = new RenameDocumentDialogFragment();
        rdd.init(context, entity);
        rdd.show(manager, "Rename Document");
    }
}
