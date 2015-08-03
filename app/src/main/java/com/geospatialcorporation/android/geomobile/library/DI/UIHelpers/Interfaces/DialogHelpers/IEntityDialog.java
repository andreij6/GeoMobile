package com.geospatialcorporation.android.geomobile.library.DI.UIHelpers.Interfaces.DialogHelpers;

import android.content.Context;
import android.support.v4.app.FragmentManager;

public interface IEntityDialog<T> {
    void delete(T entity, Context context, FragmentManager manager);

    void actions(T entity, Context context, FragmentManager manager);

    void rename(T entity, Context context, FragmentManager manager);
}
