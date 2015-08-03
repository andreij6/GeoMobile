package com.geospatialcorporation.android.geomobile.library.DI.UIHelpers;

import com.geospatialcorporation.android.geomobile.library.DI.UIHelpers.Implementations.DialogHelpers.AttributeDialog;
import com.geospatialcorporation.android.geomobile.library.DI.UIHelpers.Implementations.DialogHelpers.DocumentDialog;
import com.geospatialcorporation.android.geomobile.library.DI.UIHelpers.Implementations.DialogHelpers.FolderDialog;
import com.geospatialcorporation.android.geomobile.library.DI.UIHelpers.Implementations.DialogHelpers.GeneralDialog;
import com.geospatialcorporation.android.geomobile.library.DI.UIHelpers.Implementations.DialogHelpers.LayerDialog;
import com.geospatialcorporation.android.geomobile.library.DI.UIHelpers.Implementations.DialogHelpers.SublayerDialog;
import com.geospatialcorporation.android.geomobile.library.DI.UIHelpers.Implementations.GeoRefreshLayout;
import com.geospatialcorporation.android.geomobile.library.DI.UIHelpers.Interfaces.DialogHelpers.IAttributeDialog;
import com.geospatialcorporation.android.geomobile.library.DI.UIHelpers.Interfaces.DialogHelpers.IDocumentDialog;
import com.geospatialcorporation.android.geomobile.library.DI.UIHelpers.Interfaces.DialogHelpers.IFolderDialog;
import com.geospatialcorporation.android.geomobile.library.DI.UIHelpers.Interfaces.DialogHelpers.IGeneralDialog;
import com.geospatialcorporation.android.geomobile.library.DI.UIHelpers.Interfaces.DialogHelpers.ILayerDialog;
import com.geospatialcorporation.android.geomobile.library.DI.UIHelpers.Interfaces.DialogHelpers.ISublayerDialog;
import com.geospatialcorporation.android.geomobile.library.DI.UIHelpers.Interfaces.ILayoutRefresher;

import dagger.Module;
import dagger.Provides;

@Module
public class UIHelperModule {

    @Provides
    ILayoutRefresher provideLayoutRefresher(){ return new GeoRefreshLayout(); }

    @Provides
    IFolderDialog provideFolderDialog() { return new FolderDialog(); }

    @Provides
    ILayerDialog provideLayerDialog() { return new LayerDialog(); }

    @Provides
    IDocumentDialog provideDocumentDialog() { return new DocumentDialog(); }

    @Provides
    ISublayerDialog provideSublayerDialog(){ return new SublayerDialog(); }

    @Provides
    IGeneralDialog provideGeneralDialog(){ return new GeneralDialog(); }

    @Provides
    IAttributeDialog provideAttributeDialog(){ return new AttributeDialog(); }
}
