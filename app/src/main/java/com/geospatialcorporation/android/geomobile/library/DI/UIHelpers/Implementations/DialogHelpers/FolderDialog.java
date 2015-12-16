package com.geospatialcorporation.android.geomobile.library.DI.UIHelpers.Implementations.DialogHelpers;

import android.content.Context;
import android.support.v4.app.FragmentManager;

import com.geospatialcorporation.android.geomobile.library.DI.UIHelpers.Interfaces.DialogHelpers.IFolderDialog;
import com.geospatialcorporation.android.geomobile.models.Folders.Folder;
import com.geospatialcorporation.android.geomobile.ui.fragments.dialogs.CreateFolderDialogFragment;
import com.geospatialcorporation.android.geomobile.ui.fragments.dialogs.DeleteFolderDialogFragment;
import com.geospatialcorporation.android.geomobile.ui.fragments.dialogs.action_dialogs.folder.RenameFolderActionDialogFragment;

public class FolderDialog implements IFolderDialog {

    @Override
    public void create(Folder parent, Context context, FragmentManager manager) {
        CreateFolderDialogFragment c = new CreateFolderDialogFragment();
        c.init(parent);
        c.show(manager, "Folder Creator");
    }

    @Override
    public void delete(Folder entity, Context context, FragmentManager manager) {
        DeleteFolderDialogFragment d = new DeleteFolderDialogFragment();
        d.init(entity);
        d.show(manager, "Delete Folder");
    }

    @Override
    public void actions(Folder entity, Context context, FragmentManager manager) {

    }

    @Override
    public void rename(Folder entity, Context context, FragmentManager manager) {
        RenameFolderActionDialogFragment r = new RenameFolderActionDialogFragment();
        r.init(entity);
        r.show(manager, "Rename Folder");
    }


}
