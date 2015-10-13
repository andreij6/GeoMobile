package com.geospatialcorporation.android.geomobile.ui.fragments.detail_fragment.tablet;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.geospatialcorporation.android.geomobile.R;
import com.geospatialcorporation.android.geomobile.library.services.DocumentDetailCommons.DocumentDetailCommon;
import com.geospatialcorporation.android.geomobile.library.services.DocumentDetailCommons.IDocumentDetailCommon;
import com.geospatialcorporation.android.geomobile.ui.Interfaces.IPanelFragmentCtrl;
import com.geospatialcorporation.android.geomobile.ui.MainTabletActivity;
import com.geospatialcorporation.android.geomobile.ui.fragments.TabGeoViewFragmentBase;
import com.geospatialcorporation.android.geomobile.ui.fragments.panel_fragments.tree_fragment_panels.tablet.TabletDocumentDetailPanelFragment;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import butterknife.Bind;
import butterknife.OnClick;

public class TabDocumentDetailFragment extends TabGeoViewFragmentBase implements IPanelFragmentCtrl {

    @Bind(R.id.title) TextView mTitle;
    @Bind(R.id.uploadTimeLabel) TextView mUploadLabel;
    @Bind(R.id.uploadTimeValue) TextView mUploadValue;
    @Bind(R.id.fileSizeLabel) TextView mFileSizeLabel;
    @Bind(R.id.fileSizeValue) TextView mFileSizeValue;
    @Bind(R.id.sliding_layout) SlidingUpPanelLayout mPanel;

    IDocumentDetailCommon mCommon;

    @SuppressWarnings("unused")
    @OnClick(R.id.close)
    public void close(){
        ((MainTabletActivity)getActivity()).closeInfoFragment();
    }

    @SuppressWarnings("unused")
    @OnClick(R.id.optionsIV)
    public void openOptions(){
        mCommon.onOptionsButtonPressed(getFragmentManager(), new TabletDocumentDetailPanelFragment());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = super.onCreateView(inflater, container, savedInstanceState);

        mCommon = new DocumentDetailCommon(getResources());

        mCommon.panel(mPanel)
                .handleArguments(getArguments())
                .setViews(mTitle, mUploadValue, mFileSizeValue);

        return v;
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_detail_document_tablet;
    }

    @Override
    public void closePanel() {
        mCommon.closePanel();
    }
}
