package com.geospatialcorporation.android.geomobile.library.DI.UIHelpers.Implementations.DialogHelpers;

import android.content.Context;
import android.support.v4.app.FragmentManager;

import com.geospatialcorporation.android.geomobile.library.DI.UIHelpers.Interfaces.DialogHelpers.ISublayerDialog;
import com.geospatialcorporation.android.geomobile.models.Layers.Layer;
import com.geospatialcorporation.android.geomobile.ui.fragments.dialogs.action_dialogs.SublayerActionsDialogFragment;
import com.geospatialcorporation.android.geomobile.ui.fragments.dialogs.action_dialogs.sublayer.SublayerCreateActionDialogFragment;
import com.geospatialcorporation.android.geomobile.ui.fragments.dialogs.action_dialogs.sublayer.SublayerDeleteActionDialogFragment;
import com.geospatialcorporation.android.geomobile.ui.fragments.dialogs.action_dialogs.sublayer.SublayerRenameActionDialogFragment;

public class SublayerDialog implements ISublayerDialog {

    @Override
    public void create(Layer parent, Context context, FragmentManager manager) {
        SublayerCreateActionDialogFragment scadf = new SublayerCreateActionDialogFragment();
        scadf.init(context, parent);
        scadf.show(manager, "Add Sublayer");
    }

    @Override
    public void delete(Layer entity, Context context, FragmentManager manager) {
        SublayerDeleteActionDialogFragment sdad = new SublayerDeleteActionDialogFragment();
        sdad.init(context, entity);
        sdad.show(manager, "Delete Sublayer");
    }

    @Override
    public void actions(Layer entity, Context context, FragmentManager manager) {

    }

    @Override
    public void rename(Layer entity, Context context, FragmentManager manager) {
        SublayerRenameActionDialogFragment srad = new SublayerRenameActionDialogFragment();
        srad.init(context, entity);
        srad.show(manager, "Rename Sublayer");
    }

    @Override
    public void modify(Layer entity, Context context, FragmentManager manager) {
        SublayerActionsDialogFragment msdf = new SublayerActionsDialogFragment();
        msdf.init(context, entity);
        msdf.show(manager, "Modify Sublayer");
    }
}
