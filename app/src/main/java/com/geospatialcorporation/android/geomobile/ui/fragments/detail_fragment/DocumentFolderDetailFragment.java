package com.geospatialcorporation.android.geomobile.ui.fragments.detail_fragment;

import android.os.Bundle;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabHost;

import com.geospatialcorporation.android.geomobile.R;
import com.geospatialcorporation.android.geomobile.library.helpers.GeoDialogHelper;
import com.geospatialcorporation.android.geomobile.models.Folders.Folder;
import com.geospatialcorporation.android.geomobile.ui.fragments.detail_fragment.folder_tabs.FolderDetailsTab;
import com.geospatialcorporation.android.geomobile.ui.fragments.detail_fragment.folder_tabs.PermissionsTab;
import com.geospatialcorporation.android.geomobile.ui.fragments.dialogs.ItemDetailFragment;

import butterknife.ButterKnife;

public class DocumentFolderDetailFragment extends ItemDetailFragment<Folder> implements TabHost.OnTabChangeListener {
    private static final String TAG = DocumentFolderDetailFragment.class.getSimpleName();

    private static final String DETAILS = "Details";
    private static final String PERMISSIONS = "Permissions";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        View view = inflater.inflate(R.layout.fragment_tree_detail, null);

        ButterKnife.inject(this, view);

        HandleArguments();
        Bundle args = getArguments();
        args.putString("Folder Type", "Document");

        FragmentTabHost tabHost = (FragmentTabHost)view.findViewById(R.id.tabHost);

        tabHost.setup(getActivity(), getChildFragmentManager(), android.R.id.tabcontent);

        tabHost.addTab(tabHost.newTabSpec(DETAILS).setIndicator(DETAILS), FolderDetailsTab.class, args);
        tabHost.addTab(tabHost.newTabSpec(PERMISSIONS).setIndicator(PERMISSIONS), PermissionsTab.class, getArguments());

        tabHost.setCurrentTab(0);

        return view;
    }

    @Override
    protected void HandleArguments() {
        Bundle args = getArguments();

        mEntity = args.getParcelable(Folder.FOLDER_INTENT);

        SetTitle(mEntity.getName());
    }

    public View.OnClickListener DeleteonClickListner = new View.OnClickListener(){
        @Override
        public void onClick(View v){
            GeoDialogHelper.deleteFolder(getActivity(), mEntity, getFragmentManager());
        }
    };

    @Override
    public void onTabChanged(String tabId) {

    }
}
