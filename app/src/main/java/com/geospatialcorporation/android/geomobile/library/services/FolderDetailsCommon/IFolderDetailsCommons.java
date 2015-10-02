package com.geospatialcorporation.android.geomobile.library.services.FolderDetailsCommon;

import android.os.Bundle;
import android.widget.TextView;


public interface IFolderDetailsCommons {
    IFolderDetailsCommons handleArguments(Bundle arguments);

    void getDataAsync();

    IFolderDetailsCommons setViews(FolderDetailsCommons.ViewSetups views);
}
