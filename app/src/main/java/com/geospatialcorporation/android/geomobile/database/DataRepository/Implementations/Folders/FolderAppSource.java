package com.geospatialcorporation.android.geomobile.database.DataRepository.Implementations.Folders;

import com.geospatialcorporation.android.geomobile.application;
import com.geospatialcorporation.android.geomobile.database.DataRepository.Implementations.AppSourceBase;
import com.geospatialcorporation.android.geomobile.models.Folders.Folder;

public class FolderAppSource extends AppSourceBase<Folder> {

    public FolderAppSource(){
        super(application.getFolderHashMap());
    }
}
