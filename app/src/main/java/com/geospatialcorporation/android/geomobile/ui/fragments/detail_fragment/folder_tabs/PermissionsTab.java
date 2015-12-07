package com.geospatialcorporation.android.geomobile.ui.fragments.detail_fragment.folder_tabs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.geospatialcorporation.android.geomobile.R;
import com.geospatialcorporation.android.geomobile.application;
import com.geospatialcorporation.android.geomobile.library.DI.Analytics.Models.GoogleAnalyticEvent;
import com.geospatialcorporation.android.geomobile.library.DI.Tasks.Interfaces.IGetFolderPermissionTask;
import com.geospatialcorporation.android.geomobile.library.DI.Tasks.models.FolderPermissionsParams;
import com.geospatialcorporation.android.geomobile.library.constants.AccessLevelCodes;
import com.geospatialcorporation.android.geomobile.library.helpers.TableFactory;
import com.geospatialcorporation.android.geomobile.models.Folders.Folder;
import com.geospatialcorporation.android.geomobile.models.Folders.FolderPermissionsResponse;
import com.geospatialcorporation.android.geomobile.ui.Interfaces.IPostExecuter;
import com.geospatialcorporation.android.geomobile.ui.fragments.detail_fragment.GeoDetailsTabBase;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;


public class PermissionsTab extends GeoDetailsTabBase<Folder> implements IPostExecuter<List<FolderPermissionsResponse>> {

    private static final String TAG = PermissionsTab.class.getSimpleName();

    List<FolderPermissionsResponse> mPermission;
    LayoutInflater mInflater;
    @Bind(R.id.permissionsTable) TableLayout mTableLayout;

    IGetFolderPermissionTask mTask;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.fragment_folder_permissions_tab, container, false);
        ButterKnife.bind(this, v);

        mInflater = inflater;
        setIntentString(Folder.FOLDER_INTENT);
        handleArgs();

        mAnalytics.trackScreen(new GoogleAnalyticEvent().FolderPermissionsScreen());

        refresh();

        return v;
    }

    @Override
    public void refresh() {
        mTask = application.getTasksComponent().provideGetFolderPermissions();
        mTask.getPermissions(new FolderPermissionsParams(mEntity, mPermission, this));
    }

    @Override
    public void onPostExecute(List<FolderPermissionsResponse> permissions){
        if(permissions == null || permissions.isEmpty()){
            Toaster("No Access to Permissions");
            return;
        }

        mTableLayout.removeAllViews();

        TableFactory factory = new TableFactory(getActivity(), mTableLayout, mInflater);

        factory.addHeaders(R.layout.template_table_header, "Role", "Permissions");

        mTableLayout = factory.build();

        for(FolderPermissionsResponse permission : permissions){
            TableRow row = new TableRow(getActivity());

            TextView role = (TextView)mInflater.inflate(R.layout.template_table_column, null);
            role.setText(permission.getRoleName());

            TextView permissionTV = (TextView)mInflater.inflate(R.layout.template_feature_window_column_tv, null);
            String permissionName = getPermissionName(permission.getAccessLevel(), permission.getInheritedAccessLevel());
            permissionTV.setText(permissionName);

            row.addView(role);
            row.addView(permissionTV);

            mTableLayout.addView(row);
        }

        mTableLayout.setStretchAllColumns(true);

    }

    protected String getPermissionName(Integer accessLevel, Integer inheritedAccessLevel) {
        if(accessLevel != null){
            return switchPermission(accessLevel);
        } else {
            return switchPermission(inheritedAccessLevel);
        }
    }

    protected String switchPermission(Integer accessLevelCode) {
        String access = "";

        if(AccessLevelCodes.NoAccess.equals(accessLevelCode)){
            access = "No Access";
        }

        if(AccessLevelCodes.Editor.equals(accessLevelCode)){
            access = "Read/Write";
        }

        if(AccessLevelCodes.FullControl.equals(accessLevelCode)){
            access = "Full Control";
        }

        if(AccessLevelCodes.ReadOnly.equals(accessLevelCode)){
            access = "Read Only";
        }

        return access;
    }
}
