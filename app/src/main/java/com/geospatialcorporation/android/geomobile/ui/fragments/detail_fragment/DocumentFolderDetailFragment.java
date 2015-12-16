package com.geospatialcorporation.android.geomobile.ui.fragments.detail_fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;

import com.geospatialcorporation.android.geomobile.R;
import com.geospatialcorporation.android.geomobile.application;
import com.geospatialcorporation.android.geomobile.library.DI.UIHelpers.Interfaces.DialogHelpers.IFolderDialog;
import com.geospatialcorporation.android.geomobile.library.constants.GeoPanel;
import com.geospatialcorporation.android.geomobile.library.helpers.DataHelper;
import com.geospatialcorporation.android.geomobile.library.panelmanager.PanelManager;
import com.geospatialcorporation.android.geomobile.models.Folders.Folder;
import com.geospatialcorporation.android.geomobile.ui.Interfaces.IFragmentView;
import com.geospatialcorporation.android.geomobile.ui.Interfaces.RestoreSettings;
import com.geospatialcorporation.android.geomobile.ui.fragments.detail_fragment.folder_tabs.FolderDetailsTab;
import com.geospatialcorporation.android.geomobile.ui.fragments.detail_fragment.folder_tabs.PermissionsTab;
import com.geospatialcorporation.android.geomobile.ui.fragments.dialogs.ItemDetailFragment;
import com.geospatialcorporation.android.geomobile.ui.fragments.panel_fragments.tree_fragment_panels.DocumentFolderPanelFragment;
import com.melnykov.fab.FloatingActionButton;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import butterknife.Bind;
import butterknife.ButterKnife;

public class DocumentFolderDetailFragment extends ItemDetailFragment<Folder> implements TabHost.OnTabChangeListener, IFragmentView {
    private static final String TAG = DocumentFolderDetailFragment.class.getSimpleName();

    private static final String DETAILS = "Details";
    private static final String PERMISSIONS = "Permissions";

    @Bind(R.id.title) TextView mTitle;
    @Bind(R.id.sliding_layout) SlidingUpPanelLayout mPanel;
    @Nullable @Bind(R.id.optionsIV) FloatingActionButton mOptions;
    @Nullable @Bind(R.id.parentFolderSplit) ImageView mSplitter;
    @Nullable @Bind(R.id.sectionTitle) TextView mBackFolder;
    @Nullable @Bind(R.id.landOptionsIV) ImageView mLandOptions;

    IFolderDialog mFolderDialog;

    DocumentFolderRestoreSettings mRestoreSettings;
    Bundle mBundle;

    static String RESTORE_SETTINGS_KEY = DocumentFolderRestoreSettings.class.getSimpleName();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.fragment_tree_detail, null);

        ButterKnife.bind(this, view);

        mFolderDialog = application.getUIHelperComponent().provideFolderDialog();

        application.setDocumentFolderFragmentPanel(mPanel);
        mPanelManager = new PanelManager.Builder().type(GeoPanel.DOCUMENT_FOLDER_DETAIL).hide().build();

        handleArguments();



        if(mOptions != null) {
            mOptions.bringToFront();
        }

        FragmentTabHost tabHost = (FragmentTabHost)view.findViewById(R.id.tabHost);

        tabHost.setup(getActivity(), getChildFragmentManager(), android.R.id.tabcontent);

        tabHost.addTab(tabHost.newTabSpec(DETAILS).setIndicator(createTabView(tabHost.getContext(), R.drawable.details_selector)), FolderDetailsTab.class, mBundle);
        tabHost.addTab(tabHost.newTabSpec(PERMISSIONS).setIndicator(createTabView(tabHost.getContext(), R.drawable.permissions_selector)), PermissionsTab.class, mBundle);

        tabHost.setCurrentTab(0);

        return view;
    }

    @Override
    protected Fragment getPanelFragment() {
        return new DocumentFolderPanelFragment();
    }

    @Override
    protected void handleArguments() {
        mBundle = getArguments();

        if(mBundle != null) {
            mEntity = mBundle.getParcelable(Folder.FOLDER_INTENT);
        } else {
            mRestoreSettings = (DocumentFolderRestoreSettings)application.getRestoreSettings(RESTORE_SETTINGS_KEY);

            if (mRestoreSettings != null) {
                mEntity = mRestoreSettings.getEntity();
                mBundle = new Bundle();
                mBundle.putParcelable(Folder.FOLDER_INTENT, mEntity);
            }

        }

        if(mEntity != null){
            mTitle.setText(DataHelper.trimString(mEntity.getProperName(), 15));
        }

        mBundle.putString(Folder.FOLDER_TYPE_INTENT, "Document");
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if(mRestoreSettings != null){
            mRestoreSettings.onSaveInstance(mEntity, RESTORE_SETTINGS_KEY);
        } else {
            application.setRestoreSettings(new DocumentFolderRestoreSettings(mEntity), RESTORE_SETTINGS_KEY);
        }
    }

    public View.OnClickListener DeleteonClickListner = new View.OnClickListener(){
        @Override
        public void onClick(View v){
            mFolderDialog.delete(mEntity, getActivity(), getFragmentManager());
        }
    };

    @Override
    public void onTabChanged(String tabId) {

    }

    @Override
    public void setContentFragment(FragmentManager fm, Bundle bundle) {
        setFrame(R.id.content_frame, fm, bundle);
    }

    private void setFrame(int frame, FragmentManager fm, Bundle bundle){
        Fragment fragment = new DocumentFolderDetailFragment();

        if (bundle != null) {
            fragment.setArguments(bundle);

            fm.beginTransaction()
                    .replace(frame, this)
                    .addToBackStack(null)
                    .commit();
        }
    }

    public static class DocumentFolderRestoreSettings extends RestoreSettings<Folder>{
        public DocumentFolderRestoreSettings(Folder folder){
            super(folder);
        }
    }
}
