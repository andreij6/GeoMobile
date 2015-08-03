package com.geospatialcorporation.android.geomobile.library.helpers;

import android.support.v4.app.FragmentManager;
import android.content.Context;
import com.geospatialcorporation.android.geomobile.models.Folders.Folder;
import com.geospatialcorporation.android.geomobile.models.Layers.Layer;
import com.geospatialcorporation.android.geomobile.models.Document.Document;
import com.geospatialcorporation.android.geomobile.ui.fragments.dialogs.BookmarksDialogFragment;
import com.geospatialcorporation.android.geomobile.ui.fragments.dialogs.CreateFolderDialogFragment;
import com.geospatialcorporation.android.geomobile.ui.fragments.dialogs.CreateLayerDialogFragment;
import com.geospatialcorporation.android.geomobile.ui.fragments.dialogs.MapFeatureDocumentDialogFragment;
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

    public static void showBookmarks(Context context, FragmentManager fm, FloatingActionButton save, FloatingActionButton close, SlidingUpPanelLayout panel, GoogleMap m) {
        BookmarksDialogFragment b = new BookmarksDialogFragment();
        b.init(context, save, close, panel, m, fm);
        b.show(fm, "Bookmarks");
    }
}
