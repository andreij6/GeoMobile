package com.geospatialcorporation.android.geomobile.library.DI.TreeServices;

import com.geospatialcorporation.android.geomobile.library.DI.TreeServices.Interfaces.IDocumentTreeService;
import com.geospatialcorporation.android.geomobile.library.DI.TreeServices.Interfaces.IFolderTreeService;
import com.geospatialcorporation.android.geomobile.library.DI.TreeServices.Interfaces.ILayerTreeService;
import com.geospatialcorporation.android.geomobile.library.DI.TreeServices.Interfaces.ISublayerTreeService;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = TreeServiceModule.class)
public interface TreeServiceComponent {
    IFolderTreeService provideFolderTreeService();
    ILayerTreeService provideLayerTreeService();
    ISublayerTreeService provideSublayerTreeService();
    IDocumentTreeService provideDocumentTreeService();
}
