package com.geospatialcorporation.android.geomobile.library.services.Library;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.geospatialcorporation.android.geomobile.library.DI.Tasks.models.GetDocumentsParam;
import com.geospatialcorporation.android.geomobile.library.DI.TreeServices.Interfaces.IDocumentTreeService;
import com.geospatialcorporation.android.geomobile.library.panelmanager.ISlidingPanelManager;
import com.geospatialcorporation.android.geomobile.models.Folders.Folder;
import com.geospatialcorporation.android.geomobile.models.ListItem;

import java.util.List;

public interface ILibraryProcessor {
    void handleArgs(Bundle arguments, GetDocumentsParam params);

    void hideProgressDialog();

    List<ListItem> getListItemData(Folder currentFolder);

    ISlidingPanelManager getPanelManager();

    void onOptionsButtonPressed(Folder currentFolder, FragmentManager fragmentManager, Fragment fragment);

    void closePanel();

    IDocumentTreeService getDocumentUploader();
}
