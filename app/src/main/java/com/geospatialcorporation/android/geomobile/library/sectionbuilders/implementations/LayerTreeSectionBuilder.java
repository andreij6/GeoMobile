package com.geospatialcorporation.android.geomobile.library.sectionbuilders.implementations;

import android.support.v4.app.FragmentManager;
import android.content.Context;
import android.support.v7.widget.RecyclerView;

import com.geospatialcorporation.android.geomobile.R;
import com.geospatialcorporation.android.geomobile.library.panelmanager.ISlidingPanelManager;
import com.geospatialcorporation.android.geomobile.library.sectionbuilders.ISectionBuilder;
import com.geospatialcorporation.android.geomobile.library.sectionbuilders.TreeSectionBuilderBase;
import com.geospatialcorporation.android.geomobile.models.Folders.Folder;
import com.geospatialcorporation.android.geomobile.ui.adapters.recycler.ListItemAdapter;
import com.geospatialcorporation.android.geomobile.models.ListItem;

import java.util.List;

public class LayerTreeSectionBuilder extends TreeSectionBuilderBase<ListItem> implements ISectionBuilder<ListItem> {

    ISlidingPanelManager mPanelManager;

    public LayerTreeSectionBuilder(Context context, FragmentManager fm, Folder parent, ISlidingPanelManager panelManager) {
        super(context, fm, parent, R.string.layers_section);
        mPanelManager = panelManager;
    }

    @Override
    public ISectionBuilder<ListItem> BuildAdapter(List<ListItem> data, int folderCount) {
        mData = data;
        //AddEmptyData(mData);
        ListItemAdapter adapter = new ListItemAdapter(mContext, mData, ListItemAdapter.LAYER, mFragmentManager, mPanelManager);

        buildAdapter(adapter, folderCount);

        return this;
    }

    private void AddEmptyData(List<ListItem> data) {
        for(int x = 0; x < 2; x++) {
            data.add(new ListItem(ListItem.LAYER));
        }
    }

    @Override
    public ISectionBuilder<ListItem> setRecycler(RecyclerView recycler) {
        setupRecycler(recycler);

        return this;
    }


}
