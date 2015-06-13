package com.geospatialcorporation.android.geomobile.library.viewmode.implementations;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.geospatialcorporation.android.geomobile.R;
import com.geospatialcorporation.android.geomobile.library.helpers.panelmanager.ISlidingPanelManager;
import com.geospatialcorporation.android.geomobile.library.helpers.panelmanager.SlidingPanelManager;
import com.geospatialcorporation.android.geomobile.library.viewmode.IViewMode;
import com.geospatialcorporation.android.geomobile.ui.fragments.panel_fragments.QuickSearchFragment;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

/**
 * Created by andre on 6/6/2015.
 */
public class SearchMode implements IViewMode {
    Builder mBuilder;
    public SearchMode(Builder b){
        mBuilder = b;
    }

    @Override
    public void Disable(Boolean showPanel) {
        mBuilder.mPanelManager.collapse();
    }

    @Override
    public boolean isSame(IViewMode mode) {
        return mode instanceof SearchMode;
    }

    public static class Builder {
        ISlidingPanelManager mPanelManager;

        public Builder init(FragmentManager supportFragmentManager, ISlidingPanelManager panelManager) {
            mPanelManager = panelManager;

            mPanelManager.touch(false);

            Fragment quickSearchFragment = new QuickSearchFragment();

            supportFragmentManager.beginTransaction()
                    .replace(R.id.slider_content, quickSearchFragment)
                    .commit();

            mPanelManager.anchor();

            return this;
        }

        public IViewMode create(){
            return new SearchMode(this);
        }

    }

}
