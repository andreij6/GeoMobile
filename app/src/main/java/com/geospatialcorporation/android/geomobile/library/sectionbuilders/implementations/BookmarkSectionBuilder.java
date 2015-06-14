package com.geospatialcorporation.android.geomobile.library.sectionbuilders.implementations;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.geospatialcorporation.android.geomobile.R;
import com.geospatialcorporation.android.geomobile.library.sectionbuilders.ISectionBuilder;
import com.geospatialcorporation.android.geomobile.library.sectionbuilders.SectionBuilderBase;
import com.geospatialcorporation.android.geomobile.models.Bookmarks.Bookmark;
import com.geospatialcorporation.android.geomobile.ui.adapters.recycler.BookmarkAdapter;
import com.geospatialcorporation.android.geomobile.ui.adapters.SimpleSectionedRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

public class BookmarkSectionBuilder extends SectionBuilderBase<Bookmark> implements ISectionBuilder<Bookmark> {

    public BookmarkSectionBuilder(Context context){
        super(context);
    }

    public BookmarkSectionBuilder BuildAdapter(List<Bookmark> data, int defaultSectionCount) {
        mData = data;

        BookmarkAdapter adapter = new BookmarkAdapter(mContext, mData);

        List<SimpleSectionedRecyclerViewAdapter.Section> sections = new ArrayList<>();

        sections.add(new SimpleSectionedRecyclerViewAdapter.Section(0, "Default Section"));

        SimpleSectionedRecyclerViewAdapter.Section[] sectionArray = new SimpleSectionedRecyclerViewAdapter.Section[sections.size()];

        mSectionedAdapter = new SimpleSectionedRecyclerViewAdapter(mContext, R.layout.section_bookmark, R.id.section_text, adapter);

        mSectionedAdapter.setSections(sections.toArray(sectionArray));

        return this;
    }

    public BookmarkSectionBuilder setRecycler(RecyclerView recycler) {
        recycler.setAdapter(mSectionedAdapter);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(mContext);

        recycler.setLayoutManager(layoutManager);
        return this;
    }
}
