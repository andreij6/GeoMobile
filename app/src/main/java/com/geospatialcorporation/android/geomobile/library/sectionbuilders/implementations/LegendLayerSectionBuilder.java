package com.geospatialcorporation.android.geomobile.library.sectionbuilders.implementations;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.geospatialcorporation.android.geomobile.R;
import com.geospatialcorporation.android.geomobile.application;
import com.geospatialcorporation.android.geomobile.database.DataRepository.IAddDataRepository;
import com.geospatialcorporation.android.geomobile.database.DataRepository.Implementations.Folders.FolderAppSource;
import com.geospatialcorporation.android.geomobile.library.DI.Map.Interfaces.ILayerManager;
import com.geospatialcorporation.android.geomobile.library.DI.SharedPreferences.Implementations.AppStateSharedPrefs;
import com.geospatialcorporation.android.geomobile.library.DI.Tasks.Implementations.LayerStyleTask;
import com.geospatialcorporation.android.geomobile.library.DI.Tasks.Interfaces.ILayerStyleTask;
import com.geospatialcorporation.android.geomobile.library.DI.UIHelpers.Interfaces.IMapStatusBarManager;
import com.geospatialcorporation.android.geomobile.library.constants.PluginCodes;
import com.geospatialcorporation.android.geomobile.library.map.AppStateMapQueryRequestCallback;
import com.geospatialcorporation.android.geomobile.library.sectionbuilders.ISectionBuilder;
import com.geospatialcorporation.android.geomobile.library.sectionbuilders.SectionBuilderBase;
import com.geospatialcorporation.android.geomobile.models.Folders.Folder;
import com.geospatialcorporation.android.geomobile.models.Layers.Layer;
import com.geospatialcorporation.android.geomobile.models.Layers.LegendLayer;
import com.geospatialcorporation.android.geomobile.models.Query.map.Layers;
import com.geospatialcorporation.android.geomobile.models.Query.map.MapDefaultQueryRequest;
import com.geospatialcorporation.android.geomobile.models.Query.map.Options;
import com.geospatialcorporation.android.geomobile.models.Subscription;
import com.geospatialcorporation.android.geomobile.ui.adapters.recycler.LegendLayerAdapter;
import com.geospatialcorporation.android.geomobile.ui.adapters.SimpleSectionedRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class LegendLayerSectionBuilder extends SectionBuilderBase<Folder> implements ISectionBuilder<Folder> {


    public LegendLayerSectionBuilder(Context context) {
        super(context);
        mAppStateLayers = new ArrayList<>();
        mStateSharedPrefs = application.getGeoSharedPrefsComponent().provideAppStateSharedPrefs();
        mMapStatusBarManager = application.getStatusBarManager();
        mLayerManager = application.getMapComponent().provideLayerManager();
        mSubscription = application.getGeoSubscription();
        mFolderRepo = new FolderAppSource();

    }

    protected ArrayList<LegendLayer> mAppStateLayers;
    protected AppStateSharedPrefs mStateSharedPrefs;
    protected IMapStatusBarManager mMapStatusBarManager;
    protected ILayerManager mLayerManager;
    protected Subscription mSubscription;
    protected IAddDataRepository<Folder> mFolderRepo;


    @Override
    public ISectionBuilder<Folder> BuildAdapter(List<Folder> data, int folderCount) {
        mData = data;

        List<LegendLayer> llayers = getLayersFromFolders(data);

        ILayerManager layerManager = application.getMapComponent().provideLayerManager();

        //-- Add All Layers Extent
        getAllLayerExtents(llayers, layerManager);
        //--

        for(LegendLayer llayer : llayers){
            ILayerStyleTask layerStyleTask = new LayerStyleTask();

            layerStyleTask.getActiveStyle(llayer);
        }

        //-- Show AppState Layers
        showAppStateLayers();
        //--

        LegendLayerAdapter adapter = new LegendLayerAdapter(mContext, llayers);

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

    @Override
    public ISectionBuilder<Folder> setRecycler(RecyclerView recycler) {
        recycler.setAdapter(mSectionedAdapter);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(mContext);

        recycler.setLayoutManager(layoutManager);

        return this;
    }

    //region Helpers
    protected void showAppStateLayers(){

        //for(LegendLayer legendLayer : mAppStateLayers){
        //    mMapStatusBarManager.StartLoading(legendLayer.getLayer().getGeometryTypeCodeId());
        //}

        for(LegendLayer llayer : mAppStateLayers) {

                Layer layer = llayer.getLayer();
                layer.setIsShowing(true);

                addLayerToMap(llayer);

                application.getMapLayerState().addLayer(layer);

                mLayerManager.addVisibleLayerExtent(layer.getId(), layer.getExtent());

                llayer.setMapped(true);

        }
    }

    protected void getAllLayerExtents(List<LegendLayer> llayers, ILayerManager layerManager) {
        for(LegendLayer llayer : llayers){
            Layer toLayerAdd = llayer.getLayer();

            if (toLayerAdd != null) {
                application.addLegendLayerToQueue(llayer);

                layerManager.addAllLayersExtent(toLayerAdd.getId(), toLayerAdd.getExtent());

                if(IsSetInAppState(toLayerAdd)){
                    mAppStateLayers.add(llayer);
                }
            }

        }
    }

    protected void addLayerToMap(LegendLayer llayer) {
        Layers single = new Layers(llayer.getLayer());
        List<Layers> layers = new ArrayList<>();
        layers.add(single);

        MapDefaultQueryRequest request = new MapDefaultQueryRequest(layers, Options.MAP_QUERY);

        ILayerStyleTask layerStyleTask = application.getTasksComponent().provideLayerStyleTask();

        layerStyleTask.getStyle(llayer, new AppStateMapQueryRequestCallback(request, llayer));
    }

    protected boolean IsSetInAppState(Layer layer) {

        return mStateSharedPrefs.getInt(layer.getName() + layer.getId() + "_" + mSubscription.getId(), 0) != 0;
    }

    protected List<LegendLayer> getLayersFromFolders(List<Folder> layerFolders) {
        List<LegendLayer> result = new ArrayList<>();
        HashMap<Integer, Layer> layerHashMap = new HashMap<>();

        addFoldersToRepo(layerFolders);

        for(Folder folder : layerFolders){

            result.add(new LegendLayer(folder));

            if(folder.getLayers() != null && !folder.getLayers().isEmpty()){
                List<Layer> layers = new ArrayList<>();

                layers.addAll(folder.getLayers());

                for(Layer layer : layers){
                    layerHashMap.put(layer.getId(), layer);

                    if(layer.isMobileCompatible()) {
                        result.add(new LegendLayer(layer));
                    }
                }
            }
        }

        application.setLayerHashMap(layerHashMap);

        return result;
    }

    private void addFoldersToRepo(List<Folder> folders) {

        if (folders.size() > 0) {
            mFolderRepo.Add(folders);
        }
    }


    protected int getSkipValue(Folder folder, int skip) {
        //skip += 1;

        if(folder != null && folder.getLayers() != null){
            skip += folder.getLayers().size();
        }

        return skip;
    }
    //endregion




}
