package com.geospatialcorporation.android.geomobile.library.DI.UIHelpers.Implementations.DialogHelpers;

import android.content.Context;
import android.support.v4.app.FragmentManager;

import com.geospatialcorporation.android.geomobile.library.DI.UIHelpers.Interfaces.DialogHelpers.IGeneralDialog;
import com.geospatialcorporation.android.geomobile.models.Folders.Folder;
import com.geospatialcorporation.android.geomobile.ui.fragments.dialogs.LibraryActionDialogFragment;
import com.geospatialcorporation.android.geomobile.ui.fragments.dialogs.MapFeatureDocumentDialogFragment;

public class GeneralDialog implements IGeneralDialog {

    @Override
    public void addMapFeatureDocument(int layerId, String featureId, Context context, FragmentManager manager) {
        MapFeatureDocumentDialogFragment mfddf = new MapFeatureDocumentDialogFragment();
        mfddf.init(context, layerId, featureId);
        mfddf.show(manager, "Add Document to Feature");
    }

    @Override
    public void editAttributes(Context context, FragmentManager manager) {

    }

    @Override
    public void libraryAction(Folder folder, Context context, FragmentManager manager) {
        LibraryActionDialogFragment l = new LibraryActionDialogFragment();

        l.setContext(context);
        l.setFolder(folder);
        l.show(manager, "library actions");
    }


}
