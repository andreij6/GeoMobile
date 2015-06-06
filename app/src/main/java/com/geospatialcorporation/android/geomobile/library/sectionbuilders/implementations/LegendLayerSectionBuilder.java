package com.geospatialcorporation.android.geomobile.library.sectionbuilders.implementations;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.geospatialcorporation.android.geomobile.R;
import com.geospatialcorporation.android.geomobile.library.sectionbuilders.ISectionBuilder;
import com.geospatialcorporation.android.geomobile.library.sectionbuilders.SectionBuilderBase;
import com.geospatialcorporation.android.geomobile.models.Folders.Folder;
import com.geospatialcorporation.android.geomobile.models.Layers.Layer;
import com.geospatialcorporation.android.geomobile.ui.adapters.LegendLayerAdapter;
import com.geospatialcorporation.android.geomobile.ui.adapters.SimpleSectionedRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

import retrofit.RetrofitError;

/**
 * Created by andre on 6/5/2015.
 */
public class LegendLayerSectionBuilder extends SectionBuilderBase<Folder> implements ISectionBuilder<Folder> {

    public LegendLayerSectionBuilder(Context context) {
        super(context);
    }

    @Override
    public ISectionBuilder<Folder> BuildAdapter(List<Folder> data, int folderCount) {
        mData = data;

        LegendLayerAdapter adapter = new LegendLayerAdapter(mContext, data);

        List<SimpleSectionedRecyclerViewAdapter.Section> sections = new ArrayList<>();

        if(data != null){
            int skip = 0;
            for(Folder folder : data){
                sections.add(new SimpleSectionedRecyclerViewAdapter.Section(skip, folder.getName()));
                skip = getSkipValue(folder, skip);

            }
        }

        SimpleSectionedRecyclerViewAdapter.Section[] sectionArray = new SimpleSectionedRecyclerViewAdapter.Section[sections.size()];

        mSectionedAdapter = new SimpleSectionedRecyclerViewAdapter(mContext, R.layout.section_legend_layer, R.id.section_text, adapter);

        mSectionedAdapter.setSections(sections.toArray(sectionArray));

        return this;
    }

    protected int getSkipValue(Folder folder, int skip) {
        if(folder != null && folder.getLayers() != null){
            skip += folder.getLayers().size();
        }

        return skip++;
    }

    @Override
    public ISectionBuilder<Folder> setRecycler(RecyclerView recycler) {
        recycler.setAdapter(mSectionedAdapter);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(mContext);

        recycler.setLayoutManager(layoutManager);

        return this;
    }


}
