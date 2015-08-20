package com.geospatialcorporation.android.geomobile.library.DI.Tasks;

import com.geospatialcorporation.android.geomobile.library.DI.Tasks.Implementations.DocumentsTask;
import com.geospatialcorporation.android.geomobile.library.DI.Tasks.Implementations.GeoMapper;
import com.geospatialcorporation.android.geomobile.library.DI.Tasks.Implementations.GeoUserLoginTask;
import com.geospatialcorporation.android.geomobile.library.DI.Tasks.Implementations.GetClientsTask;
import com.geospatialcorporation.android.geomobile.library.DI.Tasks.Implementations.GetFolderDetailsTask;
import com.geospatialcorporation.android.geomobile.library.DI.Tasks.Implementations.GetFolderPermissionsTask;
import com.geospatialcorporation.android.geomobile.library.DI.Tasks.Implementations.GetLayerAttributeColumnsTask;
import com.geospatialcorporation.android.geomobile.library.DI.Tasks.Implementations.GetLayerDetailsTask;
import com.geospatialcorporation.android.geomobile.library.DI.Tasks.Implementations.GetLayersTask;
import com.geospatialcorporation.android.geomobile.library.DI.Tasks.Implementations.GetOrderNumberTask;
import com.geospatialcorporation.android.geomobile.library.DI.Tasks.Implementations.GetSublayersTask;
import com.geospatialcorporation.android.geomobile.library.DI.Tasks.Implementations.LayerStyleTask;
import com.geospatialcorporation.android.geomobile.library.DI.Tasks.Implementations.ProfileTask;
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
import com.geospatialcorporation.android.geomobile.library.DI.Tasks.Interfaces.ILayerStyleTask;
import com.geospatialcorporation.android.geomobile.library.DI.Tasks.Interfaces.IMapFeaturesTask;
import com.geospatialcorporation.android.geomobile.library.DI.Tasks.Interfaces.IUserLoginTask;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class TasksModule {

    @Provides @Singleton
    IUserLoginTask provideUserLoginTask(){ return new GeoUserLoginTask();}

    @Provides @Singleton
    IGetProfileTask provideProfileTask() { return new ProfileTask(); }

    @Provides @Singleton
    IGetDocumentsTask provideGetDocumentsTask() { return new DocumentsTask();}

    @Provides @Singleton
    IGetLayersTask provideGetLayersTask() { return new GetLayersTask(); }

    @Provides @Singleton
    IGetClientsTask provideGetClientsTask() { return new GetClientsTask(); }

    //TODO: Check in AttributeDefaultCollapsedPanelFragment -- Done
    @Provides @Singleton
    IGetOrderNumberTask provideGetOrderNumberTask(){ return new GetOrderNumberTask(); }

    @Provides @Singleton
    IGetLayerAttributeColumnsTask provideAttributeColumnsTask(){ return new GetLayerAttributeColumnsTask();}

    //TODO: Check DetailsTab : layers  -- Done
    @Provides @Singleton
    IGetLayerDetailsTask provideLayerDetailsTask(){ return new GetLayerDetailsTask(); }

    @Provides @Singleton
    IGetSublayersTask provideSublayersTask() { return new GetSublayersTask(); }

    @Provides @Singleton
    IGetFolderDetailsTask provideFolderDetailsTask() { return new GetFolderDetailsTask(); }

    //TODO: FolderPermissionsTab --- Done
    @Provides @Singleton
    IGetFolderPermissionTask provideGetFolderPermissions(){ return new GetFolderPermissionsTask(); }

    @Provides @Singleton
    IMapFeaturesTask provideMapFeaturesTask(){ return new GeoMapper();}

    @Provides @Singleton
    ILayerStyleTask provideLayerStyleTask(){ return new LayerStyleTask(); }
}
