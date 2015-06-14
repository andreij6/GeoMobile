package com.geospatialcorporation.android.geomobile.ui.adapters.base;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import butterknife.ButterKnife;

/**
 * Created by andre on 6/14/2015.
 */
public abstract class GeoHolderBase<T> extends RecyclerView.ViewHolder {

    public GeoHolderBase(View itemView) {
        super(itemView);
        ButterKnife.inject(this, itemView);
    }

    public abstract void bind(T item);

}
