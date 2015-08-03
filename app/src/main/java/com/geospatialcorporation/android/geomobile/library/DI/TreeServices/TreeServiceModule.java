package com.geospatialcorporation.android.geomobile.library.DI.TreeServices;

import com.geospatialcorporation.android.geomobile.database.DataRepository.Implementations.Documents.DocumentsAppSource;
import com.geospatialcorporation.android.geomobile.database.DataRepository.Implementations.Folders.FolderAppSource;
import com.geospatialcorporation.android.geomobile.database.DataRepository.Implementations.Layers.LayersAppSource;
import com.geospatialcorporation.android.geomobile.library.DI.TreeServices.Implementations.DocumentTreeService;
import com.geospatialcorporation.android.geomobile.library.DI.TreeServices.Implementations.FolderTreeService;
import com.geospatialcorporation.android.geomobile.library.DI.TreeServices.Implementations.LayerTreeService;
import com.geospatialcorporation.android.geomobile.library.DI.TreeServices.Implementations.SublayerTreeService;
import com.geospatialcorporation.android.geomobile.library.DI.TreeServices.Interfaces.IDocumentTreeService;
import com.geospatialcorporation.android.geomobile.library.DI.TreeServices.Interfaces.IFolderTreeService;
import com.geospatialcorporation.android.geomobile.library.DI.TreeServices.Interfaces.ILayerTreeService;
import com.geospatialcorporation.android.geomobile.library.DI.TreeServices.Interfaces.ISublayerTreeService;

import dagger.Module;
import dagger.Provides;

@Module
public class TreeServiceModule {
    @Provides
    IFolderTreeService provideFolderTreeService(){ return new FolderTreeService(new LayersAppSource(), new FolderAppSource()); }

    @Provides
    ILayerTreeService provideLayerTreeService(){ return new LayerTreeService(new LayersAppSource()); }

    @Provides
    ISublayerTreeService provideSublayerTreeService(){ return new SublayerTreeService();}

    @Provides
    IDocumentTreeService provideDocumentTreeService(){ return new DocumentTreeService(new DocumentsAppSource()); }

}
