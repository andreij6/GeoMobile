package com.geospatialcorporation.android.geomobile.library.sectionbuilders;

import android.content.Context;

import com.geospatialcorporation.android.geomobile.ui.adapters.SimpleSectionedRecyclerViewAdapter;

import java.util.List;

/**
 * Created by andre on 6/5/2015.
 */
public abstract class SectionBuilderBase<T> {

    protected Context mContext;
    protected List<T> mData;
    protected SimpleSectionedRecyclerViewAdapter mSectionedAdapter;

    public SectionBuilderBase(Context context){
        mContext = context;
    }
}
