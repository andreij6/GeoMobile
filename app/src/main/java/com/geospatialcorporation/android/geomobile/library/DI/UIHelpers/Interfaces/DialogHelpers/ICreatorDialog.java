package com.geospatialcorporation.android.geomobile.library.DI.UIHelpers.Interfaces.DialogHelpers;

import android.content.Context;
import android.support.v4.app.FragmentManager;

public interface ICreatorDialog<T, P>  extends IEntityDialog<T> {
    void create(P parent, Context context, FragmentManager manager);
}
