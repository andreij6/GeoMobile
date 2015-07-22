package com.geospatialcorporation.android.geomobile.library.helpers;

import android.support.v4.app.FragmentManager;
import android.content.Context;
import com.geospatialcorporation.android.geomobile.models.Folders.Folder;
import com.geospatialcorporation.android.geomobile.models.Layers.Layer;
import com.geospatialcorporation.android.geomobile.models.Document.Document;
import com.geospatialcorporation.android.geomobile.ui.fragments.dialogs.BookmarksDialogFragment;
import com.geospatialcorporation.android.geomobile.ui.fragments.dialogs.CreateFolderDialogFragment;
import com.geospatialcorporation.android.geomobile.ui.fragments.dialogs.CreateLayerDialogFragment;
import com.geospatialcorporation.android.geomobile.ui.fragments.dialogs.action_dialogs.document.DeleteDocumentDialogFragment;
import com.geospatialcorporation.android.geomobile.ui.fragments.dialogs.DeleteFolderDialogFragment;
import com.geospatialcorporation.android.geomobile.ui.fragments.dialogs.action_dialogs.layer.DeleteLayerDialogFragment;
import com.geospatialcorporation.android.geomobile.ui.fragments.dialogs.action_dialogs.DocumentActionsDialogFragment;
import com.geospatialcorporation.android.geomobile.ui.fragments.dialogs.action_dialogs.FolderActionsDialogFragment;
import com.geospatialcorporation.android.geomobile.ui.fragments.dialogs.action_dialogs.LayerActionsDialogFragment;
import com.geospatialcorporation.android.geomobile.ui.fragments.dialogs.action_dialogs.SublayerActionsDialogFragment;
import com.geospatialcorporation.android.geomobile.ui.fragments.dialogs.UploadImageDialogFragment;
import com.geospatialcorporation.android.geomobile.ui.fragments.dialogs.action_dialogs.document.MoveDocumentDialogFragment;
import com.geospatialcorporation.android.geomobile.ui.fragments.dialogs.action_dialogs.document.RenameDocumentDialogFragment;
import com.geospatialcorporation.android.geomobile.ui.fragments.dialogs.action_dialogs.folder.RenameFolderActionDialogFragment;
import com.geospatialcorporation.android.geomobile.ui.fragments.dialogs.action_dialogs.layer.RenameLayerActionDialogFragment;
import com.geospatialcorporation.android.geomobile.ui.fragments.dialogs.action_dialogs.sublayer.SublayerDeleteActionDialogFragment;
import com.geospatialcorporation.android.geomobile.ui.fragments.dialogs.action_dialogs.sublayer.SublayerRenameActionDialogFragment;
import com.google.android.gms.maps.GoogleMap;
import com.melnykov.fab.FloatingActionButton;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

public class GeoDialogHelper {

    //region Folder
    public static void createFolder(Context context, Folder folder, FragmentManager fm) {
        CreateFolderDialogFragment c = new CreateFolderDialogFragment();
        c.init(context, folder);
        c.show(fm, "Folder Creator");
    }

    public static void deleteFolder(Context context, Folder folder, FragmentManager fm){
        DeleteFolderDialogFragment d = new DeleteFolderDialogFragment();
        d.init(context, folder);
        d.show(fm, "Delete Folder");
    }

    public static void renameFolder(Context context, Folder folder, FragmentManager fragmentManager) {
        RenameFolderActionDialogFragment r = new RenameFolderActionDialogFragment();
        r.init(context, folder);
        r.show(fragmentManager, "Rename Folder");
    }

    public static void folderActions(Context context, Folder folder, FragmentManager fm){
        FolderActionsDialogFragment fad = new FolderActionsDialogFragment();
        fad.init(context, folder);
        fad.show(fm, "Folder Actions");
    }
    //endregion

    //region Layer
    public static void createLayer(Context context, Folder folder, FragmentManager fm) {
        CreateLayerDialogFragment l = new CreateLayerDialogFragment();
        l.init(context, folder);
        l.show(fm, "Layer Creator");
    }

    public static void deleteLayer(Context context, Layer layer, FragmentManager fm){
        DeleteLayerDialogFragment d = new DeleteLayerDialogFragment();
        d.init(context, layer);
        d.show(fm, "Delete Layer");
    }

    public static void showLayerActions(Context context, Layer layer, FragmentManager fm) {
        LayerActionsDialogFragment ladf = new LayerActionsDialogFragment();
        ladf.init(context, layer);
        ladf.show(fm, "Layer Actions");
    }

    public static void renameLayer(Context context, Layer layer, FragmentManager fm){
        RenameLayerActionDialogFragment d = new RenameLayerActionDialogFragment();
        d.init(context, layer);
        d.show(fm, "Rename Layer");
    }
    //endregion

    //region Document
    public static void deleteDocument(Context context, Document doc, FragmentManager fm){
        DeleteDocumentDialogFragment d = new DeleteDocumentDialogFragment();
        d.init(context, doc);
        d.show(fm, "Delete Document");
    }

    public static void renameDocument(Context context, Document document, FragmentManager fm) {
        RenameDocumentDialogFragment rdd = new RenameDocumentDialogFragment();
        rdd.init(context, document);
        rdd.show(fm, "Rename Document");
    }

    public static void moveDocument(Context context, Document document, FragmentManager fragmentManager) {
        MoveDocumentDialogFragment move = new MoveDocumentDialogFragment();
        move.init(context, document);
        move.show(fragmentManager, "Move Document");
    }

    public static void uploadImage(Context context, Folder folder, FragmentManager fm) {
        UploadImageDialogFragment u = new UploadImageDialogFragment();
        u.init(context, folder);
        u.show(fm, "Upload Image");
    }

    public static void showDocumentActions(Context context, Document doc, FragmentManager fm) {
        DocumentActionsDialogFragment dadf = new DocumentActionsDialogFragment();
        dadf.init(context, doc);
        dadf.show(fm, "Document Actions");
    }
    //endregion

    public static void showBookmarks(Context context, FragmentManager fm, FloatingActionButton save, FloatingActionButton close, SlidingUpPanelLayout panel, GoogleMap m) {
        BookmarksDialogFragment b = new BookmarksDialogFragment();
        b.init(context, save, close, panel, m, fm);
        b.show(fm, "Bookmarks");
    }

    public static void modifySublayer(Context context, Layer sublayer, FragmentManager fm){
        SublayerActionsDialogFragment msdf = new SublayerActionsDialogFragment();
        msdf.init(context, sublayer);
        msdf.show(fm, "Modify Sublayer");
    }

    public static void renameSublayer(Context context, Layer sublayer, FragmentManager fragmentManager) {
        SublayerRenameActionDialogFragment srad = new SublayerRenameActionDialogFragment();
        srad.init(context, sublayer);
        srad.show(fragmentManager, "Rename Sublayer");
    }

    public static void deleteSublayer(Context context, Layer sublayer, FragmentManager fragmentManager) {
        SublayerDeleteActionDialogFragment sdad = new SublayerDeleteActionDialogFragment();
        sdad.init(context, sublayer);
        sdad.show(fragmentManager, "Delete Sublayer");
    }

}
