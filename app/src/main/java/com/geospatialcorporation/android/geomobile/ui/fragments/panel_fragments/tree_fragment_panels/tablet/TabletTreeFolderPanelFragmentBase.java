package com.geospatialcorporation.android.geomobile.ui.fragments.panel_fragments.tree_fragment_panels.tablet;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.geospatialcorporation.android.geomobile.R;
import com.geospatialcorporation.android.geomobile.library.util.DeviceTypeUtil;
import com.geospatialcorporation.android.geomobile.models.Folders.Folder;
import com.geospatialcorporation.android.geomobile.ui.Interfaces.IPanelFragmentCtrl;
import com.geospatialcorporation.android.geomobile.ui.MainTabletActivity;
import com.geospatialcorporation.android.geomobile.ui.fragments.TabGeoViewFragmentBase;

import butterknife.Bind;

public abstract class TabletTreeFolderPanelFragmentBase extends TabletPanelFragmentBase {

    private static final String TAG = TabletTreeFolderPanelFragmentBase.class.getSimpleName();

    Folder mFolder;
    @Bind(R.id.nameTV) TextView mPath;
    @Bind(R.id.folderNameTV) TextView mFolderName;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = super.onCreateView(inflater, container, savedInstanceState);

        handleArgs();

        //TODO: condense - same as TreeFolderPanelFragmentBase
        if(mFolder.getParent() != null) {
            mPath.setText(mFolder.getParent().getProperName());
        } else {
            mPath.setText("");
        }

        mFolderName.setText(mFolder.getProperName());

        return v;
    }

    protected void handleArgs() {
        Bundle args = getArguments();

        mFolder = args.getParcelable(Folder.FOLDER_INTENT);
    }
}
