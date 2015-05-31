package com.geospatialcorporation.android.geomobile.library.helpers;

import android.app.FragmentManager;
import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.geospatialcorporation.android.geomobile.R;
import com.geospatialcorporation.android.geomobile.models.Folders.Folder;
import com.geospatialcorporation.android.geomobile.models.Layers.Layer;
import com.geospatialcorporation.android.geomobile.models.Library.Document;
import com.geospatialcorporation.android.geomobile.ui.adapters.ListItemAdapter;
import com.geospatialcorporation.android.geomobile.ui.adapters.SimpleSectionedRecyclerViewAdapter;
import com.geospatialcorporation.android.geomobile.ui.viewmodels.ListItem;

import java.util.ArrayList;
import java.util.List;

public class SectionTreeBuilder {
    private static final String TAG = SectionTreeBuilder.class.getSimpleName();

    DataHelper mHelper;
    List<ListItem> mListItems;
    List<Folder> mFolders;
    Folder mParent;
    Context mContext;
    SimpleSectionedRecyclerViewAdapter mSectionedAdapter;
    FragmentManager mFragmentManager;

    public SectionTreeBuilder(Context context, FragmentManager fm){
        mHelper = new DataHelper();
        mListItems = new ArrayList<>();
        mContext = context;
        mFragmentManager = fm;
    }

    public SectionTreeBuilder AddLayerData(List<Layer> layers, List<Folder> infolders, Folder parent){
        mFolders = infolders != null ? infolders : new ArrayList<Folder>();
        mParent = parent;
        mListItems = mHelper.CombineLayerItems(layers, mFolders, mParent);

        return this;
    }

    public SectionTreeBuilder AddLibraryData(List<Document> documents, List<Folder> infolders, Folder parent){
        mFolders = infolders != null ? infolders : new ArrayList<Folder>();
        mParent = parent;
        mListItems = mHelper.CombineLibraryItems(documents, mFolders, mParent);

        return this;
    }

    public SectionTreeBuilder BuildAdapter(String adapterType){
        ListItemAdapter listItemAdapter = new ListItemAdapter(mContext, mListItems, adapterType, mFragmentManager);
        String folderSection = mContext.getResources().getString(R.string.folders_section);
        String treeSpecificSection;

        if(adapterType.equals(ListItemAdapter.LIBRARY)){
            treeSpecificSection = mContext.getResources().getString(R.string.documents_section);
        } else {
            treeSpecificSection = mContext.getResources().getString(R.string.layers_section);
        }

        List<SimpleSectionedRecyclerViewAdapter.Section> sections = new ArrayList<SimpleSectionedRecyclerViewAdapter.Section>();

        Log.d(TAG, "Parent included: ".concat((mParent != null) ? mParent.getName() : "false"));

        if (mParent != null) {
            String parentSection = mParent.getName();
            sections.add(new SimpleSectionedRecyclerViewAdapter.Section(0, parentSection));
            sections.add(new SimpleSectionedRecyclerViewAdapter.Section(1, folderSection));
            sections.add(new SimpleSectionedRecyclerViewAdapter.Section(mFolders.size()+1, treeSpecificSection));
        } else {
            sections.add(new SimpleSectionedRecyclerViewAdapter.Section(0, folderSection));
            sections.add(new SimpleSectionedRecyclerViewAdapter.Section(mFolders.size(), treeSpecificSection));
        }

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

