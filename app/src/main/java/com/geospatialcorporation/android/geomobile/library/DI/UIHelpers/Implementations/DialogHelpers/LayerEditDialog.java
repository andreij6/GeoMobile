package com.geospatialcorporation.android.geomobile.library.DI.UIHelpers.Implementations.DialogHelpers;

import android.content.Context;
import android.support.v4.app.FragmentManager;

import com.geospatialcorporation.android.geomobile.library.DI.UIHelpers.Interfaces.DialogHelpers.ILayerEditDialog;
import com.geospatialcorporation.android.geomobile.ui.fragments.dialogs.action_dialogs.layer.EditLayersDialogFragment;

public class LayerEditDialog implements ILayerEditDialog {

    @Override
    public void editLayers(Context context, FragmentManager manager) {
        EditLayersDialogFragment e = new EditLayersDialogFragment();

        e.init(context);

        e.show(manager, "Edit Layer");
    }
}
