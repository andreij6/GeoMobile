package com.geospatialcorporation.android.geomobile.library.sectionbuilders.implementations;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;

import com.geospatialcorporation.android.geomobile.R;
import com.geospatialcorporation.android.geomobile.library.panelmanager.ISlidingPanelManager;
import com.geospatialcorporation.android.geomobile.library.sectionbuilders.ISectionBuilder;
import com.geospatialcorporation.android.geomobile.library.sectionbuilders.TreeSectionBuilderBase;
import com.geospatialcorporation.android.geomobile.models.Folders.Folder;
import com.geospatialcorporation.android.geomobile.models.ListItem;
import com.geospatialcorporation.android.geomobile.ui.adapters.recycler.ListItemAdapter;

import java.util.List;

public class LibraryTreeSectionBuilder extends TreeSectionBuilderBase<ListItem> implements ISectionBuilder<ListItem> {

    ISlidingPanelManager mPanelManager;

    public LibraryTreeSectionBuilder(Context context, FragmentManager fm, Folder parent, ISlidingPanelManager panelManager) {
        super(context, fm, parent, R.string.documents_section);
        mPanelManager = panelManager;
    }

    @Override
    public ISectionBuilder<ListItem> BuildAdapter(List<ListItem> data, int folderCount) {
        mData = data;

        ListItemAdapter adapter = new ListItemAdapter(mContext, mData, ListItemAdapter.LIBRARY, mFragmentManager, mPanelManager);

        buildAdapter(adapter, folderCount);

        return this;
    }

    @Override
    public ISectionBuilder<ListItem> setRecycler(RecyclerView recycler) {
        setupRecycler(recycler);
        return this;
    }
}
