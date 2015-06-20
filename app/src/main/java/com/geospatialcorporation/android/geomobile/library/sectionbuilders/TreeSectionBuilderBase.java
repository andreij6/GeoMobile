package com.geospatialcorporation.android.geomobile.library.sectionbuilders;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.geospatialcorporation.android.geomobile.R;
import com.geospatialcorporation.android.geomobile.models.Folders.Folder;
import com.geospatialcorporation.android.geomobile.ui.adapters.recycler.ListItemAdapter;
import com.geospatialcorporation.android.geomobile.ui.adapters.SimpleSectionedRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

public abstract class TreeSectionBuilderBase<T> extends SectionBuilderBase<T>{

    protected FragmentManager mFragmentManager;
    protected Folder mParent;
    protected String mFolderSectionName;
    protected String mTreeSectionName;

    public TreeSectionBuilderBase(Context context, FragmentManager fm, Folder parentFolder, int sectionId) {
        super(context);
        mFragmentManager = fm;
        mParent = parentFolder;
        mFolderSectionName = context.getResources().getString(R.string.folders_section);
        mTreeSectionName = context.getResources().getString(sectionId);
    }

    protected void buildAdapter(ListItemAdapter adapter, int folderCount){
        List<SimpleSectionedRecyclerViewAdapter.Section> sections = new ArrayList<SimpleSectionedRecyclerViewAdapter.Section>();

        if(mParent != null){
            sections.add(new SimpleSectionedRecyclerViewAdapter.Section(0, setParentName()));
            sections.add(new SimpleSectionedRecyclerViewAdapter.Section(1, mFolderSectionName));
            CheckEmptyData(sections, folderCount);
        } else {
            sections.add(new SimpleSectionedRecyclerViewAdapter.Section(0, mFolderSectionName));
            sections.add(new SimpleSectionedRecyclerViewAdapter.Section(folderCount, mTreeSectionName));
        }

        SimpleSectionedRecyclerViewAdapter.Section[] sectionsArray = new SimpleSectionedRecyclerViewAdapter.Section[sections.size()];

        mSectionedAdapter = new
                SimpleSectionedRecyclerViewAdapter(mContext,  R.layout.section_tree, R.id.section_text, adapter);

        mSectionedAdapter.setSections(sections.toArray(sectionsArray));

    }

    protected void setupRecycler(RecyclerView r){
        r.setAdapter(mSectionedAdapter);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(mContext);

        r.setLayoutManager(layoutManager);
    }

    protected String setParentName() {
        return mParent.getIsFixed() && !mParent.getIsImportFolder() && mParent.getName().trim().length() == 1
                ? "Back to ROOT" : "Back to " + mParent.getName();
    }

    protected void CheckEmptyData(List<SimpleSectionedRecyclerViewAdapter.Section> sections, int folderCount) {
        if(folderCount == 0) {
            sections.add(new SimpleSectionedRecyclerViewAdapter.Section(folderCount + 2, mTreeSectionName));
        } else {
            sections.add(new SimpleSectionedRecyclerViewAdapter.Section(folderCount + 1, mTreeSectionName));
        }
    }
}
