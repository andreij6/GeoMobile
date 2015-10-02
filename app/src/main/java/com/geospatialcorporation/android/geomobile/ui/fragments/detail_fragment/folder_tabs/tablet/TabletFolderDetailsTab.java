package com.geospatialcorporation.android.geomobile.ui.fragments.detail_fragment.folder_tabs.tablet;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.geospatialcorporation.android.geomobile.R;
import com.geospatialcorporation.android.geomobile.library.DI.Analytics.Models.GoogleAnalyticEvent;
import com.geospatialcorporation.android.geomobile.library.services.FolderDetailsCommon.FolderDetailsCommons;
import com.geospatialcorporation.android.geomobile.library.services.FolderDetailsCommon.IFolderDetailsCommons;
import com.geospatialcorporation.android.geomobile.ui.fragments.TabGeoViewFragmentBase;

import butterknife.Bind;

public class TabletFolderDetailsTab extends TabGeoViewFragmentBase {

    IFolderDetailsCommons mCommons;

    @Bind(R.id.createdByValue) TextView mCreatedBy;
    @Bind(R.id.createdValue) TextView mDateCreated;
    @Bind(R.id.lastUpdatedValue) TextView mUpdated;
    @Bind(R.id.userUpdateValue) TextView mUpdateUser;
    @Bind(R.id.folderCountValue) TextView mFolderCount;
    @Bind(R.id.entityCountValue) TextView mEntityCount;
    @Bind(R.id.entityCountLabel) TextView mEntityCountLabel;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v =  super.onCreateView(inflater, container, savedInstanceState);

        mCommons = new FolderDetailsCommons();

        FolderDetailsCommons.ViewSetups viewSetups =
                new FolderDetailsCommons.ViewSetups(mCreatedBy, mDateCreated,
                                                    mUpdated, mUpdateUser,
                                                    mFolderCount, mEntityCount,
                                                    mEntityCountLabel);

        mCommons.handleArguments(getArguments())
                .setViews(viewSetups)
                .getDataAsync();

        mAnalytics.trackScreen(new GoogleAnalyticEvent().FolderDetailTab());

        return v;
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_folder_details_tab;
    }
}
