package com.geospatialcorporation.android.geomobile.library.DI.UIHelpers.Implementations.DialogHelpers;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

import com.geospatialcorporation.android.geomobile.library.DI.UIHelpers.Interfaces.DialogHelpers.IAttributeDialog;
import com.geospatialcorporation.android.geomobile.models.AttributeValueVM;
import com.geospatialcorporation.android.geomobile.models.Layers.Layer;
import com.geospatialcorporation.android.geomobile.ui.fragments.dialogs.action_dialogs.attributes.CreateAttributeDialogFragment;
import com.geospatialcorporation.android.geomobile.ui.fragments.dialogs.action_dialogs.attributes.EditAttributesDialogFragment;

public class AttributeDialog implements IAttributeDialog {

    @Override
    public void edit(AttributeValueVM data, Context context, FragmentManager manager) {
        EditAttributesDialogFragment adf = new EditAttributesDialogFragment();
        adf.init(context, data);
        adf.show(manager, "Edit Attributes");
    }

    @Override
    public void create(Layer entity, Context context, FragmentManager fragmentManager) {
        CreateAttributeDialogFragment cadf = new CreateAttributeDialogFragment();
        cadf.init(context, entity);
        cadf.show(fragmentManager, "Add Attribute");
    }
}
