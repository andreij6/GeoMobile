package com.geospatialcorporation.android.geomobile.library.sectionbuilders.implementations;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.geospatialcorporation.android.geomobile.R;
import com.geospatialcorporation.android.geomobile.application;
import com.geospatialcorporation.android.geomobile.library.sectionbuilders.ISectionBuilder;
import com.geospatialcorporation.android.geomobile.library.sectionbuilders.SectionBuilderBase;
import com.geospatialcorporation.android.geomobile.models.Folders.Folder;
import com.geospatialcorporation.android.geomobile.models.Layers.Layer;
import com.geospatialcorporation.android.geomobile.models.Layers.LegendLayer;
import com.geospatialcorporation.android.geomobile.ui.adapters.recycler.LegendLayerAdapter;
import com.geospatialcorporation.android.geomobile.ui.adapters.SimpleSectionedRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class LegendLayerSectionBuilder extends SectionBuilderBase<Folder> implements ISectionBuilder<Folder> {

    public LegendLayerSectionBuilder(Context context) {
        super(context);
    }

    @Override
    public ISectionBuilder<Folder> BuildAdapter(List<Folder> data, int folderCount) {
        mData = data;

        List<LegendLayer> llayers = getLayersFromFolders(data);

        LegendLayerAdapter adapter = new LegendLayerAdapter(mContext, llayers);

        List<SimpleSectionedRecyclerViewAdapter.Section> sections = new ArrayList<>();

        if(data != null){
            int skip = 0;
            List<Folder> emptyFolders = new ArrayList<>();

            for(Folder folder : data){
                //if(folder.getLayers() != null && folder.getLayers().size() > 0) {
                    if(isRoot(folder)) {
                        sections.add(new SimpleSectionedRecyclerViewAdapter.Section(skip, "ROOT"));
                    } else {
                        sections.add(new SimpleSectionedRecyclerViewAdapter.Section(skip, folder.getName()));
                    }
                    skip = getSkipValue(folder, skip);
                //} else {
                //   emptyFolders.add(folder);
                //}
            }

            //sections.add(new SimpleSectionedRecyclerViewAdapter.Section(skip, "- Empty Folders -"));

            //skip++;

            //for(Folder folder : emptyFolders){
            //    if(isRoot(folder)) {
            //        sections.add(new SimpleSectionedRecyclerViewAdapter.Section(skip, "ROOT"));
            //    } else {
            //        sections.add(new SimpleSectionedRecyclerViewAdapter.Section(skip, folder.getName()));
            //    }
            //    skip = getSkipValue(folder, skip);
            //}
        }

        SimpleSectionedRecyclerViewAdapter.Section[] sectionArray = new SimpleSectionedRecyclerViewAdapter.Section[sections.size()];

        mSectionedAdapter = new SimpleSectionedRecyclerViewAdapter(mContext, R.layout.section_legend_layer, R.id.section_text, adapter);

        mSectionedAdapter.setSections(sections.toArray(sectionArray));

        return this;
    }

    private List<LegendLayer> getLayersFromFolders(List<Folder> layerFolders) {
        List<LegendLayer> result = new ArrayList<>();
        HashMap<Integer, Layer> layerHashMap = new HashMap<>();


        for(Folder folder : layerFolders){
            if(folder.getLayers() != null && !folder.getLayers().isEmpty()){
                List<Layer> layers = new ArrayList<>();

                layers.addAll(folder.getLayers());

                for(Layer layer : layers){
                    layerHashMap.put(layer.getId(), layer);
                    result.add(new LegendLayer(layer));
                }

                result.add(new LegendLayer(folder));
            } else {
                result.add(new LegendLayer(folder));
            }
        }

        application.setLayerHashMap(layerHashMap);

        return result;
    }

    private boolean isRoot(Folder folder) {
        return folder.getName().equals("/");
    }

    protected int getSkipValue(Folder folder, int skip) {
        skip += 1;

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
