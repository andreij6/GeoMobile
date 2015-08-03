package com.geospatialcorporation.android.geomobile.ui.fragments.detail_fragment.folder_tabs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.geospatialcorporation.android.geomobile.R;
import com.geospatialcorporation.android.geomobile.application;
import com.geospatialcorporation.android.geomobile.library.DI.Analytics.Models.GoogleAnalyticEvent;
import com.geospatialcorporation.android.geomobile.library.DI.Tasks.Interfaces.IGetFolderPermissionTask;
import com.geospatialcorporation.android.geomobile.library.DI.Tasks.models.FolderPermissionsParams;
import com.geospatialcorporation.android.geomobile.models.Folders.Folder;
import com.geospatialcorporation.android.geomobile.models.Folders.FolderPermissionsResponse;
import com.geospatialcorporation.android.geomobile.ui.Interfaces.IPostExecuter;
import com.geospatialcorporation.android.geomobile.ui.fragments.detail_fragment.GeoDetailsTabBase;
import com.melnykov.fab.FloatingActionButton;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class PermissionsTab extends GeoDetailsTabBase<Folder> implements IPostExecuter<List<FolderPermissionsResponse>> {

    private static final String TAG = PermissionsTab.class.getSimpleName();

    List<FolderPermissionsResponse> mPermission;
    @InjectView(R.id.fab) FloatingActionButton mFab;
    IGetFolderPermissionTask mTask;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.fragment_folder_permissions_tab, container, false);
        ButterKnife.inject(this, v);

        setIntentString(Folder.FOLDER_INTENT);
        handleArgs();

        mAnalytics.trackScreen(new GoogleAnalyticEvent().FolderPermissionsScreen());

        mFab.setOnClickListener(showActions);

        refresh();

        return v;
    }

    protected View.OnClickListener showActions = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Toaster("Allow user to edit Permissions");
            mAnalytics.trackClick(new GoogleAnalyticEvent().FolderPermissionsClick());
        }
    };

    @Override
    public void refresh() {
        mTask = application.getTasksComponent().provideGetFolderPermissions();
        mTask.getPermissions(new FolderPermissionsParams(mEntity, mPermission, this));
    }

    @Override
    public void onPostExecute(List<FolderPermissionsResponse> permissions){

    }
}
