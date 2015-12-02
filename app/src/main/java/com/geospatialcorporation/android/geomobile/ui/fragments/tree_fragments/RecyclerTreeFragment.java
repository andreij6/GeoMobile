package com.geospatialcorporation.android.geomobile.ui.fragments.tree_fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.geospatialcorporation.android.geomobile.R;
import com.geospatialcorporation.android.geomobile.application;
import com.geospatialcorporation.android.geomobile.library.constants.AccessLevelCodes;
import com.geospatialcorporation.android.geomobile.library.helpers.DataHelper;
import com.geospatialcorporation.android.geomobile.library.helpers.ProgressDialogHelper;
import com.geospatialcorporation.android.geomobile.models.Folders.Folder;
import com.geospatialcorporation.android.geomobile.ui.Interfaces.OnFragmentInteractionListener;
import com.geospatialcorporation.android.geomobile.ui.MainActivity;
import com.geospatialcorporation.android.geomobile.ui.fragments.GeoViewFragmentBase;
import com.geospatialcorporation.android.geomobile.ui.fragments.GoogleMapFragment;
import com.melnykov.fab.FloatingActionButton;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import butterknife.Bind;
import butterknife.OnClick;

public abstract class RecyclerTreeFragment extends GeoViewFragmentBase {

    private static final String TAG = RecyclerTreeFragment.class.getSimpleName();

    protected DataHelper mHelper;
    protected ProgressDialogHelper mProgressHelper;
    protected Boolean mIsLandscape;
    protected Folder mCurrentFolder;
    protected Context mContext;

    @Bind(R.id.recyclerView) RecyclerView mRecyclerView;
    @Bind(R.id.swipe_refresh_layout) SwipeRefreshLayout mSwipeRefreshLayout;
    @Bind(R.id.sliding_layout) SlidingUpPanelLayout mPanel;
    @Nullable
    @Bind(R.id.OptionsFab) FloatingActionButton mOptionsSlider;
    @Nullable @Bind(R.id.landOptionsIV) ImageView mLandscapeOptions;
    @Nullable @Bind(R.id.showNavIV1) ImageView mNavBars;
    @Nullable @Bind(R.id.showNavIV2) ImageView mNavLogo;
    @Bind(R.id.title) TextView mTitle;
    @Bind(R.id.folderInformation) ImageView mInfo;
    @Nullable @Bind(R.id.backFolder) TextView mParentFolder;

    @Override
    public void onCreate(Bundle savedInstance) {
        setHasOptionsMenu(false);
        mIsLandscape = application.getIsLandscape();
        super.onCreate(savedInstance);
    }


    public void onPostExecute(Folder model) {
        mCurrentFolder = model;
        try {
            mPanelManager.hide();

            if(mIsLandscape) {
                landscapeOnPostExecute();
            } else {
                portraitOnPostExecute();

            }
        } catch (NullPointerException nullpointer){
            Log.d(TAG, nullpointer.getMessage());
        } catch (Exception e){
            Log.d(TAG, e.getMessage());
        } finally {
            mProgressHelper.hideProgressDialog();
        }
    }

    private void landscapeOnPostExecute() {
        canEdit(mLandscapeOptions);

        mTitle.setText(mCurrentFolder.getProperName());

        buildRecycler();
    }

    private void canEdit(View options) {
        if (!mCurrentFolder.getAccessLevel().equals(AccessLevelCodes.ReadOnly)) {
            options.setVisibility(View.VISIBLE);
        }
    }

    private void portraitOnPostExecute() {
        canEdit(mOptionsSlider);

        if (mCurrentFolder.getParent() != null) {
            mNavBars.setVisibility(View.GONE);
            mNavLogo.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_chevron_left_grey600_18dp));
            mNavLogo.setPadding(-12, 0, 0, 0);

            mNavBars.setOnClickListener(navigateUpTree);
            mNavLogo.setOnClickListener(navigateUpTree);
            mParentFolder.setOnClickListener(navigateUpTree);

            mParentFolder.setVisibility(View.VISIBLE);
            mParentFolder.setText(mCurrentFolder.getParent().getProperName());

        } else {
            mNavBars.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_nav_orange));

            mNavBars.setOnClickListener(showNavigation);
            mNavLogo.setOnClickListener(showNavigation);

            mNavLogo.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_g_logo));
            mNavBars.setVisibility(View.VISIBLE);
        }

        mTitle.setText(mCurrentFolder.getProperName());

        mInfo.setVisibility(View.VISIBLE);
        mNavLogo.setVisibility(View.VISIBLE);
        mTitle.setVisibility(View.VISIBLE);

        buildRecycler();
    }

    @Nullable
    @OnClick(R.id.goToMapIV)
    public void goToMapIV(){
        if(mPanelManager.getIsOpen()){
            closePanel();
        } else {
            Fragment pageFragment = new GoogleMapFragment();

            FragmentManager fragmentManager = getFragmentManager();

            fragmentManager.beginTransaction()
                    .replace(R.id.content_frame, pageFragment)
                    .addToBackStack(null).commit();
        }
    }

    @Nullable
    @OnClick(R.id.closeIV)
    public void closeFragment(){
        ((OnFragmentInteractionListener)getActivity()).closeDetailFragment();
    }

    @Nullable
    @OnClick(R.id.showNavIV1)
    public void showNavigation(){
        ((MainActivity)getActivity()).openNavigationDrawer();
    }

    @Nullable
    @OnClick(R.id.showNavIV2)
    public void showNavigation2(){
        ((MainActivity)getActivity()).openNavigationDrawer();
    }

    public void closePanel() {
        mPanelManager.hide();
    }

    public void refresh() {
        handleArguments();
    }

    protected View.OnClickListener showNavigation = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(mPanelManager.getIsOpen()){
                closePanel();
            } else {
                ((MainActivity) getActivity()).openNavigationDrawer();
            }
        }
    };

    protected View.OnClickListener navigateUpTree = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(mPanelManager.getIsOpen()){
                closePanel();
            } else {
                getFragmentManager().popBackStack();
            }
        }
    };

    public abstract void handleArguments();

    protected abstract void buildRecycler();
}
