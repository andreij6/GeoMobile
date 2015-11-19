package com.geospatialcorporation.android.geomobile.ui.fragments.detail_fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabHost;
import android.widget.TextView;

import com.geospatialcorporation.android.geomobile.R;
import com.geospatialcorporation.android.geomobile.application;
import com.geospatialcorporation.android.geomobile.library.DI.UIHelpers.Interfaces.DialogHelpers.IFolderDialog;
import com.geospatialcorporation.android.geomobile.library.constants.GeoPanel;
import com.geospatialcorporation.android.geomobile.library.helpers.DataHelper;
import com.geospatialcorporation.android.geomobile.library.helpers.GeoDialogHelper;
import com.geospatialcorporation.android.geomobile.library.panelmanager.PanelManager;
import com.geospatialcorporation.android.geomobile.models.Folders.Folder;
import com.geospatialcorporation.android.geomobile.ui.fragments.GoogleMapFragment;
import com.geospatialcorporation.android.geomobile.ui.fragments.detail_fragment.folder_tabs.FolderDetailsTab;
import com.geospatialcorporation.android.geomobile.ui.fragments.detail_fragment.folder_tabs.PermissionsTab;
import com.geospatialcorporation.android.geomobile.ui.fragments.dialogs.ItemDetailFragment;
import com.geospatialcorporation.android.geomobile.ui.fragments.panel_fragments.tree_fragment_panels.DocumentFolderPanelFragment;
import com.melnykov.fab.FloatingActionButton;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DocumentFolderDetailFragment extends ItemDetailFragment<Folder> implements TabHost.OnTabChangeListener {
    private static final String TAG = DocumentFolderDetailFragment.class.getSimpleName();

    private static final String DETAILS = "Details";
    private static final String PERMISSIONS = "Permissions";

    @Bind(R.id.sliding_layout) SlidingUpPanelLayout mPanel;
    @Bind(R.id.title) TextView mTitle;
    @Bind(R.id.optionsIV) FloatingActionButton mOptionsSlider;

    IFolderDialog mFolderDialog;

    @OnClick(R.id.goBackIV)
    public void goBack(){
        getFragmentManager().popBackStack();
    }

    @OnClick(R.id.backFolder)
    public void goUp(){ getFragmentManager().popBackStack(); }

    @OnClick(R.id.goToMapIV)
    public void goToMapIV(){
        Fragment pageFragment = new GoogleMapFragment();

        FragmentManager fragmentManager = getFragmentManager();

        fragmentManager.beginTransaction()
                .replace(R.id.content_frame, pageFragment)
                .addToBackStack(null).commit();
    }

    @OnClick(R.id.optionsIV)
    public void openOptions(){
        if(mPanelManager.getIsOpen()){
            closePanel();
        } else {
            Fragment f = new DocumentFolderPanelFragment();

            f.setArguments(mEntity.toBundle());

            getFragmentManager().beginTransaction()
                    .replace(R.id.slider_content, f)
                    .commit();

            mPanelManager.halfAnchor();
            mPanelManager.touch(false);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        View view = inflater.inflate(R.layout.fragment_tree_detail, null);

        ButterKnife.bind(this, view);

        mFolderDialog = application.getUIHelperComponent().provideFolderDialog();

        application.setDocumentFolderFragmentPanel(mPanel);
        mPanelManager = new PanelManager.Builder().type(GeoPanel.DOCUMENT_FOLDER_DETAIL).hide().build();

        handleArguments();
        Bundle args = getArguments();
        args.putString("Folder Type", "Document");

        mOptionsSlider.bringToFront();

        FragmentTabHost tabHost = (FragmentTabHost)view.findViewById(R.id.tabHost);

        tabHost.setup(getActivity(), getChildFragmentManager(), android.R.id.tabcontent);

        tabHost.addTab(tabHost.newTabSpec(DETAILS).setIndicator(createTabView(tabHost.getContext(), R.drawable.details_selector)), FolderDetailsTab.class, args);
        tabHost.addTab(tabHost.newTabSpec(PERMISSIONS).setIndicator(createTabView(tabHost.getContext(), R.drawable.permissions_selector)), PermissionsTab.class, args);

        tabHost.setCurrentTab(0);

        return view;
    }

    @Override
    protected void handleArguments() {
        Bundle args = getArguments();

        mEntity = args.getParcelable(Folder.FOLDER_INTENT);

        if(mEntity != null){
            mTitle.setText(DataHelper.trimString(mEntity.getProperName(), 15));
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

    public void closePanel() {
        mPanelManager.hide();
    }
}
