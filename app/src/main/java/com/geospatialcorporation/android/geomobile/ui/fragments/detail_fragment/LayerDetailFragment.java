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
import com.geospatialcorporation.android.geomobile.library.DI.TreeServices.Interfaces.IFolderTreeService;
import com.geospatialcorporation.android.geomobile.library.constants.GeoPanel;
import com.geospatialcorporation.android.geomobile.library.helpers.DataHelper;
import com.geospatialcorporation.android.geomobile.library.panelmanager.PanelManager;
import com.geospatialcorporation.android.geomobile.models.Folders.Folder;
import com.geospatialcorporation.android.geomobile.models.Layers.Layer;
import com.geospatialcorporation.android.geomobile.ui.Interfaces.IFragmentView;
import com.geospatialcorporation.android.geomobile.ui.Interfaces.RestoreSettings;
import com.geospatialcorporation.android.geomobile.ui.fragments.detail_fragment.layer_tabs.AttributeLayoutTab;
import com.geospatialcorporation.android.geomobile.ui.fragments.detail_fragment.layer_tabs.DetailsTab;
import com.geospatialcorporation.android.geomobile.ui.fragments.detail_fragment.layer_tabs.SublayersTab;
import com.geospatialcorporation.android.geomobile.ui.fragments.dialogs.ItemDetailFragment;
import com.geospatialcorporation.android.geomobile.ui.fragments.panel_fragments.tree_fragment_panels.LayerDetailPanelFragment;
import com.melnykov.fab.FloatingActionButton;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import butterknife.Bind;
import butterknife.ButterKnife;

public class LayerDetailFragment extends ItemDetailFragment<Layer>
        implements TabHost.OnTabChangeListener, IFragmentView {
    private static final String TAG = LayerDetailFragment.class.getSimpleName();

    private static final String SUBLAYERS = "Sublayers";
    private static final String ATTRIBUTES = "Attributes";
    private static final String DETAILS = "Details";

    @Bind(R.id.title) TextView mTitle;
    @Bind(R.id.sliding_layout) SlidingUpPanelLayout mPanel;
    @Nullable @Bind(R.id.optionsIV) FloatingActionButton mOptions;
    @Nullable @Bind(R.id.parentFolderSplit) ImageView mSplitter;
    @Nullable @Bind(R.id.sectionTitle) TextView mBackFolder;
    @Nullable @Bind(R.id.landOptionsIV) ImageView mLandOptions;

    FragmentTabHost mTabHost;
    View mView;
    IFolderTreeService mFolderService;
    LayerDetailRestoreSettings mRestoreSettings;
    Bundle mBundle;

    static String RESTORE_SETTINGS_KEY = LayerDetailRestoreSettings.class.getSimpleName();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        mView = inflater.inflate(R.layout.fragment_tree_detail, null);
        mFolderService = application.getTreeServiceComponent().provideFolderTreeService();

        ButterKnife.bind(this, mView);

        handleArguments();

        application.setLayerDetailFragmentPanel(mPanel);
        mPanelManager = new PanelManager.Builder().type(GeoPanel.LAYER_DETAIL).hide().build();

        mTabHost = (FragmentTabHost)mView.findViewById(R.id.tabHost);

        mTabHost.setup(getActivity(), getChildFragmentManager(), android.R.id.tabcontent);

        mTabHost.addTab(
                mTabHost.newTabSpec(SUBLAYERS)
                .setIndicator(createTabView(mTabHost.getContext(), R.drawable.sublayers_selector)),
                SublayersTab.class, mBundle);

        mTabHost.addTab(
                mTabHost.newTabSpec(ATTRIBUTES)
                        .setIndicator(createTabView(mTabHost.getContext(), R.drawable.attr_selector)),
                AttributeLayoutTab.class, mBundle);

        mTabHost.addTab(
                mTabHost.newTabSpec(DETAILS)
                        .setIndicator(createTabView(mTabHost.getContext(), R.drawable.details_selector)),
                DetailsTab.class, mBundle);

        mTabHost.setCurrentTab(0);

        if(mOptions != null) {
            mOptions.bringToFront();
        }

        return mView;
    }

    @Override
    protected Fragment getPanelFragment() {
        return new LayerDetailPanelFragment();
    }

    @Override
    protected void handleArguments() {
        mBundle = getArguments();

        if(mBundle == null){
            mRestoreSettings = (LayerDetailRestoreSettings)application.getRestoreSettings(RESTORE_SETTINGS_KEY);

            if(mRestoreSettings != null){
                mEntity = mRestoreSettings.getEntity();
                mBundle = new Bundle();
                mBundle.putParcelable(Layer.LAYER_INTENT, mEntity);
            }
        } else {
            mEntity = mBundle.getParcelable(Layer.LAYER_INTENT);
        }

        if(mEntity != null){
            mTitle.setText(DataHelper.trimString(mEntity.getName(), 15));
        }

        Folder folder = mFolderService.getParentFolderByLayerId(mEntity.getId());

        if(folder != null){
            if(!folder.isEditable()) {
                mOptions.setVisibility(View.GONE);
            }
        }
    }

    private void setLandscapeView(Folder parentFolder) {
        if(application.getIsLandscape()){
            mBackFolder.setVisibility(View.VISIBLE);
            mSplitter.setVisibility(View.VISIBLE);

            mBackFolder.setText(parentFolder.getProperName());
        }
    }

    @Override
    public void onTabChanged(String tabId) {

    }

    public Fragment getCurrentTab() {
        return getChildFragmentManager().findFragmentById(android.R.id.tabcontent);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //outState.putParcelable(Layer.LAYER_INTENT, mEntity);
        if(mRestoreSettings != null){
            mRestoreSettings.onSaveInstance(mEntity, RESTORE_SETTINGS_KEY);
        } else {
            application.setRestoreSettings(new LayerDetailRestoreSettings(mEntity), RESTORE_SETTINGS_KEY);
        }
    }

    @Override
    public void setContentFragment(FragmentManager fm, Bundle bundle) {
        setFrame(R.id.content_frame, fm, bundle);
    }

    private void setFrame(int frame, FragmentManager fm, Bundle bundle){
        Fragment fragment = new LayerDetailFragment();

        fragment.setArguments(bundle);

        fm.beginTransaction()
                .replace(frame, this)
                .addToBackStack(null)
                .commit();

    }

    public static class LayerDetailRestoreSettings extends RestoreSettings<Layer> {
       public LayerDetailRestoreSettings(Layer layer){ super(layer);}
    }
}
