package com.geospatialcorporation.android.geomobile.library.helpers;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.geospatialcorporation.android.geomobile.R;
import com.geospatialcorporation.android.geomobile.models.Folders.Folder;
import com.geospatialcorporation.android.geomobile.models.Layers.Layer;
import com.geospatialcorporation.android.geomobile.models.Library.Document;
import com.geospatialcorporation.android.geomobile.ui.adapters.ListItemAdapter;
import com.geospatialcorporation.android.geomobile.ui.adapters.SimpleSectionedRecyclerViewAdapter;
import com.geospatialcorporation.android.geomobile.ui.viewmodels.ListItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by andre on 4/23/2015.
 */

public class SectionTreeBuilder {
    DataHelper mHelper;
    List<ListItem> mListItems;
    List<Folder> mFolders;
    Context mContext;
    SimpleSectionedRecyclerViewAdapter mSectionedAdapter;

    public SectionTreeBuilder(Context context){
        mHelper = new DataHelper();
        mListItems = new ArrayList<>();
        mContext = context;
    }

    public SectionTreeBuilder AddLayerData(List<Layer> layers, List<Folder> infolders){
        mFolders = checkInFolders(infolders);

        mListItems = mHelper.CombineLayerItems(layers, mFolders);

        return this;
    }

    public SectionTreeBuilder AddLibraryData(List<Document> documents, List<Folder> infolders){
        mFolders = checkInFolders(infolders);

        mListItems = mHelper.CombineLibraryItems(documents, mFolders);

        return this;
    }

    private List<Folder> checkInFolders(List<Folder> infolders){
        List<Folder> folders = infolders == null ? new ArrayList<Folder>() : infolders;

        return folders;
    }

    public SectionTreeBuilder BuildAdapter(String adapterType){
        ListItemAdapter listItemAdapter = new ListItemAdapter(mContext, mListItems, adapterType);
        String section1 = mContext.getResources().getString(R.string.folders_section);
        String section2;

        if(adapterType == ListItemAdapter.LIBRARY){
            section2 = mContext.getResources().getString(R.string.documents_section);
        } else {
            section2 = mContext.getResources().getString(R.string.layers_section);
        }

        List<SimpleSectionedRecyclerViewAdapter.Section> sections = new ArrayList<SimpleSectionedRecyclerViewAdapter.Section>();

        sections.add(new SimpleSectionedRecyclerViewAdapter.Section(0, section1));
        sections.add(new SimpleSectionedRecyclerViewAdapter.Section(mFolders.size(), section2));

        SimpleSectionedRecyclerViewAdapter.Section[] dummy = new SimpleSectionedRecyclerViewAdapter.Section[sections.size()];

        mSectionedAdapter = new
                SimpleSectionedRecyclerViewAdapter(mContext,  R.layout.section, R.id.section_text, listItemAdapter);

        mSectionedAdapter.setSections(sections.toArray(dummy));

        return this;
    }

    public SectionTreeBuilder setRecycler(RecyclerView r){
        r.setAdapter(mSectionedAdapter);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(mContext);

        r.setLayoutManager(layoutManager);

        return this;
    }
}

