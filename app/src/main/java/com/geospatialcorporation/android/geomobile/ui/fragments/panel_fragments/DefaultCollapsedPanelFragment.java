package com.geospatialcorporation.android.geomobile.ui.fragments.panel_fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.geospatialcorporation.android.geomobile.R;
import com.geospatialcorporation.android.geomobile.library.constants.ViewModes;
import com.geospatialcorporation.android.geomobile.library.helpers.panelmanager.SlidingPanelManager;
import com.geospatialcorporation.android.geomobile.ui.MainActivity;
import com.geospatialcorporation.android.geomobile.ui.fragments.GeoViewFragmentBase;
import com.geospatialcorporation.android.geomobile.ui.fragments.GoogleMapFragment;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by andre on 6/5/2015.
 */
public class DefaultCollapsedPanelFragment extends GeoViewFragmentBase {
    private static final String TAG = DefaultCollapsedPanelFragment.class.getSimpleName();

    View mView;
    SlidingPanelManager mPanelManager;

    @InjectView(R.id.title) TextView Title;
    @InjectView(R.id.bookmarkBtn) Button mBookmark;
    @InjectView(R.id.quickSearchBtn) Button mSearch;
    @InjectView(R.id.queryBtn) Button mQuery;
    @InjectView(R.id.panelAnchor) ImageView mAnchor;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        mView = inflater.inflate(R.layout.fragment_panel_collapsed, container, false);
        ButterKnife.inject(this, mView);

        mPanelManager = new SlidingPanelManager(getActivity());

        mBookmark.setOnClickListener(setBookmarkMode);
        mSearch.setOnClickListener(performQuickSearch);
        mQuery.setOnClickListener(setQueryMode);
        mAnchor.setOnClickListener(anchorPanel);
        Title.setOnClickListener(anchorPanel);

        return mView;
    }

    protected View.OnClickListener setBookmarkMode = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            setViewMode(ViewModes.BOOKMARK);
        }
    };

    protected View.OnClickListener performQuickSearch = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            setViewMode(ViewModes.QUICKSEARCH);
        }
    };

    protected View.OnClickListener setQueryMode = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            setViewMode(ViewModes.QUERY);
        }
    };

    protected View.OnClickListener anchorPanel = new View.OnClickListener(){

        @Override
        public void onClick(View v) {
            if(mPanelManager.getPanelState() == SlidingUpPanelLayout.PanelState.ANCHORED){
                mPanelManager.collapse();
                mAnchor.setImageDrawable(null);
            } else {
                mPanelManager.anchor();
                mAnchor.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.ic_close_circle_white_24dp));
            }
        }
    };

    private void setViewMode(String viewMode) {
        GoogleMapFragment mapFragment = (GoogleMapFragment)(((MainActivity) getActivity()).getContentFragment());
        mapFragment.setViewMode(viewMode);
    }


}