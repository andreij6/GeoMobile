package com.geospatialcorporation.android.geomobile.library.DI.Tasks.Interfaces;

import com.geospatialcorporation.android.geomobile.library.DI.Tasks.models.GetAllDocumentsParam;
import com.geospatialcorporation.android.geomobile.library.DI.Tasks.models.GetDocumentsParam;
import com.geospatialcorporation.android.geomobile.ui.fragments.dialogs.action_dialogs.document.MoveDocumentDialogFragment;

public interface IGetDocumentsTask {

    void getDocumentsByFolderId(GetDocumentsParam param, Integer folderId);

    void getAllDocuments(GetAllDocumentsParam param);

    void getDocumentFolders(MoveDocumentDialogFragment fragment);
}
