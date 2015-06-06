package com.geospatialcorporation.android.geomobile.library.sectionbuilders;

import android.support.v7.widget.RecyclerView;

import java.util.List;

/**
 * Created by andre on 6/5/2015.
 */
public interface ISectionBuilder<T> {

    ISectionBuilder<T> BuildAdapter(List<T> data, int folderCount);

    ISectionBuilder<T> setRecycler(RecyclerView recycler);
}
