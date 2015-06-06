package com.geospatialcorporation.android.geomobile.library.viewmode.implementations;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.geospatialcorporation.android.geomobile.R;
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
    public void Disable() {
        mBuilder.mPanel.setTouchEnabled(true);
        mBuilder.mPanel.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
    }

    public static class Builder {
        SlidingUpPanelLayout mPanel;

        public Builder init(SlidingUpPanelLayout panel, FragmentManager supportFragmentManager) {
            mPanel = panel;

            panel.setTouchEnabled(false);

            Fragment quickSearchFragment = new QuickSearchFragment();

            supportFragmentManager.beginTransaction()
                    .replace(R.id.slider_content, quickSearchFragment)
                    .commit();

            anchorSlider();

            return this;
        }

        public IViewMode create(){
            return new SearchMode(this);
        }

        //region Slider Helpers
        protected void anchorSlider(){
            if (mPanel != null) {
                if (mPanel.getAnchorPoint() == 1.0f || mPanel.getPanelState() == SlidingUpPanelLayout.PanelState.COLLAPSED) {
                    mPanel.setAnchorPoint(0.7f);
                    mPanel.setPanelState(SlidingUpPanelLayout.PanelState.ANCHORED);
                } else {
                    mPanel.setAnchorPoint(1.0f);
                    mPanel.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                }
            }
        }

        //endregion
    }

}
