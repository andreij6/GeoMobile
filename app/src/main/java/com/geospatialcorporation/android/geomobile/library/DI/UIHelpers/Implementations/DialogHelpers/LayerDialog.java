package com.geospatialcorporation.android.geomobile.library.DI.UIHelpers.Implementations.DialogHelpers;

import android.content.Context;
import android.support.v4.app.FragmentManager;

import com.geospatialcorporation.android.geomobile.library.DI.UIHelpers.Interfaces.DialogHelpers.ILayerDialog;
import com.geospatialcorporation.android.geomobile.models.Folders.Folder;
import com.geospatialcorporation.android.geomobile.models.Layers.Layer;
import com.geospatialcorporation.android.geomobile.ui.fragments.dialogs.CreateLayerDialogFragment;
import com.geospatialcorporation.android.geomobile.ui.fragments.dialogs.action_dialogs.LayerActionsDialogFragment;
import com.geospatialcorporation.android.geomobile.ui.fragments.dialogs.action_dialogs.layer.DeleteLayerDialogFragment;
import com.geospatialcorporation.android.geomobile.ui.fragments.dialogs.action_dialogs.layer.RenameLayerActionDialogFragment;

public class LayerDialog implements ILayerDialog {

    @Override
    public void create(Folder parent, Context context, FragmentManager manager) {
        CreateLayerDialogFragment l = new CreateLayerDialogFragment();
        l.init(context, parent);
        l.show(manager, "Layer Creator");
    }

    @Override
    public void delete(Layer entity, Context context, FragmentManager manager) {
        DeleteLayerDialogFragment d = new DeleteLayerDialogFragment();
        d.init(context, entity);
        d.show(manager, "Delete Layer");
    }

    @Override
    public void actions(Layer entity, Context context, FragmentManager manager) {
        LayerActionsDialogFragment ladf = new LayerActionsDialogFragment();
        ladf.init(context, entity);
        ladf.show(manager, "Layer Actions");
    }

    @Override
    public void rename(Layer entity, Context context, FragmentManager manager) {
        RenameLayerActionDialogFragment d = new RenameLayerActionDialogFragment();
        d.init(context, entity);
        d.show(manager, "Rename Layer");
    }


}
