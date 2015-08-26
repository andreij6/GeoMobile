package com.geospatialcorporation.android.geomobile.library.DI.UIHelpers;

import com.geospatialcorporation.android.geomobile.library.DI.UIHelpers.Interfaces.DialogHelpers.IAttributeDialog;
import com.geospatialcorporation.android.geomobile.library.DI.UIHelpers.Interfaces.DialogHelpers.IDocumentDialog;
import com.geospatialcorporation.android.geomobile.library.DI.UIHelpers.Interfaces.DialogHelpers.IFolderDialog;
import com.geospatialcorporation.android.geomobile.library.DI.UIHelpers.Interfaces.DialogHelpers.IGeneralDialog;
import com.geospatialcorporation.android.geomobile.library.DI.UIHelpers.Interfaces.DialogHelpers.ILayerDialog;
import com.geospatialcorporation.android.geomobile.library.DI.UIHelpers.Interfaces.DialogHelpers.ISublayerDialog;
import com.geospatialcorporation.android.geomobile.library.DI.UIHelpers.Interfaces.ILayoutRefresher;
import com.geospatialcorporation.android.geomobile.library.DI.UIHelpers.Interfaces.IMainMenuHelper;
import com.geospatialcorporation.android.geomobile.library.DI.UIHelpers.Interfaces.IMapStatusBarManager;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = UIHelperModule.class)
public interface UIHelperComponent {

    ILayoutRefresher provideLayoutRefresher();

    IFolderDialog provideFolderDialog();

    ILayerDialog provideLayerDialog();

    IDocumentDialog provideDocumentDialog();

    ISublayerDialog provideSublayerDialog();

    IGeneralDialog provideGeneralDialog();

    IAttributeDialog provideAttributeDialog();

    IMainMenuHelper provideMainMenuHelper();

    IMapStatusBarManager provideMapStatusBarManager();
}
