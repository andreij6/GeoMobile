package com.geospatialcorporation.android.geomobile.library.sectionbuilders.implementations;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

import com.geospatialcorporation.android.geomobile.R;
import com.geospatialcorporation.android.geomobile.application;
import com.geospatialcorporation.android.geomobile.library.DI.Map.Interfaces.ILayerManager;
import com.geospatialcorporation.android.geomobile.library.DI.SharedPreferences.Implementations.AppStateSharedPrefs;
import com.geospatialcorporation.android.geomobile.library.DI.Tasks.Interfaces.ILayerStyleTask;
import com.geospatialcorporation.android.geomobile.library.DI.UIHelpers.Interfaces.IMapStatusBarManager;
import com.geospatialcorporation.android.geomobile.library.map.AppStateMapQueryRequestCallback;
import com.geospatialcorporation.android.geomobile.library.panelmanager.ISlidingPanelManager;
import com.geospatialcorporation.android.geomobile.library.sectionbuilders.ISectionBuilder;
import com.geospatialcorporation.android.geomobile.library.sectionbuilders.SectionBuilderBase;
import com.geospatialcorporation.android.geomobile.models.Folders.Folder;
import com.geospatialcorporation.android.geomobile.models.Layers.Layer;
import com.geospatialcorporation.android.geomobile.models.Layers.LegendLayer;
import com.geospatialcorporation.android.geomobile.models.Subscription;
import com.geospatialcorporation.android.geomobile.ui.adapters.SimpleSectionedRecyclerViewAdapter;
import com.geospatialcorporation.android.geomobile.ui.adapters.recycler.LegendLayerAdapter;
import com.geospatialcorporation.android.geomobile.ui.adapters.recycler.TabletLegendAdapter;

import java.util.ArrayList;
import java.util.List;

public class TabletLegendSectionBuilder extends LegendLayerSectionBuilder {

    ISlidingPanelManager mPanelManager;

    public TabletLegendSectionBuilder(ISlidingPanelManager manager, Context context) {
        super(context);
        mPanelManager = manager;
    }

    @Override
    public ISectionBuilder<Folder> BuildAdapter(List<Folder> data, int folderCount) {
        mData = data;

        List<LegendLayer> llayers = getLayersFromFolders(data);
        ILayerManager layerManager = application.getLayerManager();

        //-- Add All Layers Extent
        getAllLayerExtents(llayers, layerManager);
        //--

        //-- Show AppState Layers
        if(application.getShouldSetAppState()) {
            showAppStateLayers();
            application.setShouldSetAppState(false);
        } else {
            getLayerIcons();
        }
        //--

        TabletLegendAdapter adapter = new TabletLegendAdapter(mContext, llayers, mPanelManager);

        List<SimpleSectionedRecyclerViewAdapter.Section> sections = new ArrayList<>();

        if(data != null){
            int skip = 0;

            for(Folder folder : data){
                sections.add(new SimpleSectionedRecyclerViewAdapter.Section(skip, folder.getProperName()));

                skip = getSkipValue(folder, skip);
            }
        }

        SimpleSectionedRecyclerViewAdapter.Section[] sectionArray = new SimpleSectionedRecyclerViewAdapter.Section[sections.size()];

        mSectionedAdapter = new SimpleSectionedRecyclerViewAdapter(mContext, R.layout.section_legend_layer_v2, R.id.section_text, adapter);

        mSectionedAdapter.setSections(sections.toArray(sectionArray));

        return this;
    }
    
    protected void getLayerIcons(){

        for(LegendLayer llayer : mAppStateLayers) {

            ILayerStyleTask layerStyleTask = application.getTasksComponent().provideLayerStyleTask();

            layerStyleTask.getActiveStyle(llayer);

        }



    }

}
