package com.geospatialcorporation.android.geomobile.ui.fragments.panel_fragments.tree_fragment_panels;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.geospatialcorporation.android.geomobile.R;
import com.geospatialcorporation.android.geomobile.application;
import com.geospatialcorporation.android.geomobile.models.Folders.Folder;
import com.geospatialcorporation.android.geomobile.ui.fragments.GeoViewFragmentBase;

import butterknife.Bind;
import butterknife.ButterKnife;

public abstract class TreeFolderPanelFragmentBase<T> extends GeoViewFragmentBase {

    protected Folder mFolder;
    T mContentFragment;
    @Bind(R.id.nameTV) TextView mPath;
    @Bind(R.id.folderNameTV) TextView mFolderName;

    public TreeFolderPanelFragmentBase(){
        mContentFragment = (T) application.getMainActivity().getContentFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance) {
        View view = inflater.inflate(getViewResource(), container, false);
        ButterKnife.bind(this, view);

        handleArgs();

        //TODO: condense - same as TreeFolderPanelFragmentBase
        if(mFolder.getParent() != null) {
            if(mFolder.getParent().getName().equals("/")){
                mPath.setText("ROOT");
            } else{
                mPath.setText(TextUtils.join(" / ", mFolder.getPath()));
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
