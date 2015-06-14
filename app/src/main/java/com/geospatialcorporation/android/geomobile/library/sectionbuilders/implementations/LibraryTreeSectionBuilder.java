package com.geospatialcorporation.android.geomobile.library.sectionbuilders.implementations;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;

import com.geospatialcorporation.android.geomobile.R;
import com.geospatialcorporation.android.geomobile.library.sectionbuilders.ISectionBuilder;
import com.geospatialcorporation.android.geomobile.library.sectionbuilders.TreeSectionBuilderBase;
import com.geospatialcorporation.android.geomobile.models.Folders.Folder;
import com.geospatialcorporation.android.geomobile.ui.adapters.ListItemAdapter;
import com.geospatialcorporation.android.geomobile.ui.viewmodels.ListItem;

import java.util.List;

/**
 * Created by andre on 6/5/2015.
 */
public class LibraryTreeSectionBuilder extends TreeSectionBuilderBase<ListItem> implements ISectionBuilder<ListItem> {

    public LibraryTreeSectionBuilder(Context context, FragmentManager fm, Folder parent) {
        super(context, fm, parent, R.string.documents_section);
    }


    @Override
    public ISectionBuilder<ListItem> BuildAdapter(List<ListItem> data, int folderCount) {
        mData = data;

        AddEmptyData(mData);

        ListItemAdapter adapter = new ListItemAdapter(mContext, mData, ListItemAdapter.LIBRARY, mFragmentManager);

        buildAdapter(adapter, folderCount);

        return this;
    }

    private void AddEmptyData(List<ListItem> data) {
        for(int x = 0; x < 2; x++) {
            data.add(new ListItem(ListItem.DOCUMENT));
        }
    }

    @Override
    public ISectionBuilder<ListItem> setRecycler(RecyclerView recycler) {
        setupRecycler(recycler);
        return this;
    }
}
