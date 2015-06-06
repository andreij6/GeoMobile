package com.geospatialcorporation.android.geomobile.library.helpers;

import android.provider.Browser;
import android.support.v4.app.FragmentManager;
import android.content.Context;

import com.geospatialcorporation.android.geomobile.models.Folders.Folder;
import com.geospatialcorporation.android.geomobile.models.Layers.Layer;
import com.geospatialcorporation.android.geomobile.models.Library.Document;
import com.geospatialcorporation.android.geomobile.ui.fragments.dialogs.BookmarksDialogFragment;
import com.geospatialcorporation.android.geomobile.ui.fragments.dialogs.CreateFolderDialogFragment;
import com.geospatialcorporation.android.geomobile.ui.fragments.dialogs.CreateLayerDialogFragment;
import com.geospatialcorporation.android.geomobile.ui.fragments.dialogs.DeleteDocumentDialogFragment;
import com.geospatialcorporation.android.geomobile.ui.fragments.dialogs.DeleteFolderDialogFragment;
import com.geospatialcorporation.android.geomobile.ui.fragments.dialogs.DeleteLayerDialogFragment;
import com.geospatialcorporation.android.geomobile.ui.fragments.dialogs.UploadImageDialogFragment;

public class GeoDialogHelper {

    public static void createFolder(Context context, Folder folder, FragmentManager fm) {
        CreateFolderDialogFragment c = new CreateFolderDialogFragment();
        c.init(context, folder);
        c.show(fm, "Folder Creator");
    }

    public static void createLayer(Context context, Folder folder, FragmentManager fm) {
        CreateLayerDialogFragment l = new CreateLayerDialogFragment();
        l.init(context, folder);
        l.show(fm, "Layer Creator");
    }

    public static void deleteFolder(Context context, Folder folder, FragmentManager fm){
        DeleteFolderDialogFragment d = new DeleteFolderDialogFragment();
        d.init(context, folder);
        d.show(fm, "Delete Folder");
    }

    public static void deleteDocument(Context context, Document doc, FragmentManager fm){
        DeleteDocumentDialogFragment d = new DeleteDocumentDialogFragment();
        d.init(context, doc);
        d.show(fm, "Delete Document");
    }

    public static void deleteLayer(Context context, Layer layer, FragmentManager fm){
        DeleteLayerDialogFragment d = new DeleteLayerDialogFragment();
        d.init(context, layer);
        d.show(fm, "Delete Layer");
    }


    public static void uploadImage(Context context, Folder folder, FragmentManager fm) {
        UploadImageDialogFragment u = new UploadImageDialogFragment();
        u.init(context, folder);
        u.show(fm, "Upload Image");
    }

    public static void showBookmarks(Context context, FragmentManager fm) {
        BookmarksDialogFragment b = new BookmarksDialogFragment();
        b.init(context);
        b.show(fm, "Bookmarks");
    }
}
