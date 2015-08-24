package com.geospatialcorporation.android.geomobile.ui.fragments.panel_fragments.tree_fragment_panels;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.geospatialcorporation.android.geomobile.R;
import com.geospatialcorporation.android.geomobile.models.Folders.Folder;
import com.geospatialcorporation.android.geomobile.ui.fragments.GeoViewFragmentBase;

import butterknife.ButterKnife;
import butterknife.InjectView;

public abstract class TreeFolderPanelFragmentBase extends GeoViewFragmentBase {

    protected Folder mFolder;
    @InjectView(R.id.pathTV) TextView mPath;
    @InjectView(R.id.folderNameTV) TextView mFolderName;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance) {
        View view = inflater.inflate(getViewResource(), container, false);
        ButterKnife.inject(this, view);

        handleArgs();

        if(mFolder.getParent() != null) {
            if(mFolder.getParent().getName().equals("/")){
                mPath.setText("ROOT");
            } else {
                mPath.setText(mFolder.getParent().getName());
            }
        } else {
            mPath.setText("");
        }

        if(mFolder.getName().equals("/")){
            mFolderName.setText("Root");
        } else {
            mFolderName.setText(mFolder.getName());
        }

        return view;
    }

    protected void handleArgs() {
        Bundle args = getArguments();

        mFolder = args.getParcelable(Folder.FOLDER_INTENT);
    }

    protected abstract int getViewResource();
}
