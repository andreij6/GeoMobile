package com.geospatialcorporation.android.geomobile.library.DI.Tasks;

import com.geospatialcorporation.android.geomobile.library.DI.Tasks.Interfaces.IGetClientsTask;
import com.geospatialcorporation.android.geomobile.library.DI.Tasks.Interfaces.IGetDocumentsTask;
import com.geospatialcorporation.android.geomobile.library.DI.Tasks.Interfaces.IGetFolderDetailsTask;
import com.geospatialcorporation.android.geomobile.library.DI.Tasks.Interfaces.IGetFolderPermissionTask;
import com.geospatialcorporation.android.geomobile.library.DI.Tasks.Interfaces.IGetLayerAttributeColumnsTask;
import com.geospatialcorporation.android.geomobile.library.DI.Tasks.Interfaces.IGetLayerDetailsTask;
import com.geospatialcorporation.android.geomobile.library.DI.Tasks.Interfaces.IGetLayersTask;
import com.geospatialcorporation.android.geomobile.library.DI.Tasks.Interfaces.IGetOrderNumberTask;
import com.geospatialcorporation.android.geomobile.library.DI.Tasks.Interfaces.IGetProfileTask;
import com.geospatialcorporation.android.geomobile.library.DI.Tasks.Interfaces.IGetSublayersTask;
import com.geospatialcorporation.android.geomobile.library.DI.Tasks.Interfaces.IUserLoginTask;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = TasksModule.class)
public interface TasksComponent {
    IUserLoginTask provideUserLoginTask();

    IGetProfileTask provideProfileTask();

    IGetDocumentsTask provideGetDocumentsTask();

    IGetLayersTask provideLayersTask();

    IGetClientsTask provideGetClientsTask();

    IGetOrderNumberTask provideOrderNumberTask();

    IGetLayerAttributeColumnsTask provideAttributeColumnsTask();

    IGetLayerDetailsTask provideLayerDetailsTask();

    IGetSublayersTask provideSublayersTask();

    IGetFolderDetailsTask provideFolderDetailsTask();

    IGetFolderPermissionTask provideGetFolderPermissions();
}
