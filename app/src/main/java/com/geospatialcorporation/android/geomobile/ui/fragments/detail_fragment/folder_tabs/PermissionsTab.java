package com.geospatialcorporation.android.geomobile.ui.fragments.detail_fragment.folder_tabs;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.geospatialcorporation.android.geomobile.R;
import com.geospatialcorporation.android.geomobile.library.helpers.GeoDialogHelper;
import com.geospatialcorporation.android.geomobile.library.services.FolderTreeService;
import com.geospatialcorporation.android.geomobile.models.Folders.Folder;
import com.geospatialcorporation.android.geomobile.models.Folders.FolderPermissionsResponse;
import com.geospatialcorporation.android.geomobile.ui.fragments.GeoViewFragmentBase;
import com.geospatialcorporation.android.geomobile.ui.fragments.detail_fragment.GeoDetailsTabBase;
import com.melnykov.fab.FloatingActionButton;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by andre on 6/8/2015.
 */
public class PermissionsTab extends GeoDetailsTabBase<Folder> {

    private static final String TAG = PermissionsTab.class.getSimpleName();

    List<FolderPermissionsResponse> mPermission;
    @InjectView(R.id.fab) FloatingActionButton mFab;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.fragment_folder_permissions_tab, container, false);
        ButterKnife.inject(this, v);

        setIntentString(Folder.FOLDER_INTENT);
        handleArgs();

        mFab.setOnClickListener(showActions);

        refresh();

        return v;
    }

    private View.OnClickListener showActions = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Toaster("Allow user to edit Permissions");
        }
    };

    @Override
    public void refresh() {
        new GetFolderPermissionsTask().execute();
    }

    private class GetFolderPermissionsTask extends AsyncTask<Void, Void, List<FolderPermissionsResponse>>{


        @Override
        protected List<FolderPermissionsResponse> doInBackground(Void... params) {
            mService = new FolderTreeService();

            if(mEntity != null){
                mPermission = ((FolderTreeService)mService).permissions(mEntity.getId());
            }

            return mPermission;
        }

        @Override
        protected void onPostExecute(List<FolderPermissionsResponse> permissions){
            if(permissions != null) {
                Toaster(permissions.get(0).getRoleName());
                Toaster(permissions.get(0).getIsFixed() + "");
            } else {

                Toaster("Permissions Null");
            }
        }
    }

}
