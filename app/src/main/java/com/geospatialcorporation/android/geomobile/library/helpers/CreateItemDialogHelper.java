package com.geospatialcorporation.android.geomobile.library.helpers;

import android.app.FragmentManager;
import android.content.Context;

import com.geospatialcorporation.android.geomobile.models.Folders.Folder;
import com.geospatialcorporation.android.geomobile.ui.fragments.dialogs.CreateFolderDialogFragment;
import com.geospatialcorporation.android.geomobile.ui.fragments.dialogs.CreateLayerDialogFragment;

/**
 * Created by andre on 6/1/2015.
 */
public class CreateItemDialogHelper {

    public static void createFolder(Context context, Folder folder, FragmentManager fm) {
        CreateFolderDialogFragment c = new CreateFolderDialogFragment();
        c.setContext(context);
        c.setFolder(folder);
        c.show(fm, "Folder Creator");
    }

    public static void createLayer(Context context, Folder folder, FragmentManager fm) {
        CreateLayerDialogFragment l = new CreateLayerDialogFragment();
        l.setContext(context);
        l.setFolder(folder);
        l.show(fm, "Layer Creator");
    }
}
