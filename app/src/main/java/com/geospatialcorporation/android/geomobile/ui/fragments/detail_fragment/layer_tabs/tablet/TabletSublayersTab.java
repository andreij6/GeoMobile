package com.geospatialcorporation.android.geomobile.ui.fragments.detail_fragment.layer_tabs.tablet;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableLayout;

import com.geospatialcorporation.android.geomobile.R;
import com.geospatialcorporation.android.geomobile.application;
import com.geospatialcorporation.android.geomobile.library.DI.Tasks.models.GetSublayersTaskParams;
import com.geospatialcorporation.android.geomobile.library.DI.UIHelpers.Interfaces.DialogHelpers.ISublayerDialog;
import com.geospatialcorporation.android.geomobile.library.helpers.ProgressDialogHelper;
import com.geospatialcorporation.android.geomobile.library.services.SublayerTabCommons.ISublayerTabCommon;
import com.geospatialcorporation.android.geomobile.library.services.SublayerTabCommons.SublayerTabCommon;
import com.geospatialcorporation.android.geomobile.models.Layers.Layer;
import com.geospatialcorporation.android.geomobile.ui.Interfaces.IContentRefresher;
import com.geospatialcorporation.android.geomobile.ui.Interfaces.IPostExecuter;
import com.geospatialcorporation.android.geomobile.ui.fragments.TabGeoViewFragmentBase;

import java.util.List;

import butterknife.Bind;

public class TabletSublayersTab extends TabGeoViewFragmentBase implements IContentRefresher, IPostExecuter<List<Layer>> {

    ISublayerDialog mDialog;

    @Bind(R.id.sublayerTableLayout) TableLayout mSublayerDataView;
    @Bind(R.id.swipe_refresh_layout) SwipeRefreshLayout mSwipeRefreshLayout;
    @Bind(R.id.addSublayers) Button mAddSublayerBtn;

    ProgressDialogHelper mProgressDialogHelper;
    ISublayerTabCommon mCommons;
    LayoutInflater mInflater;
    Layer mEntity;
    List<Layer> mData;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mDialog = application.getUIHelperComponent().provideSublayerDialog();
        mCommons = new SublayerTabCommon();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = super.onCreateView(inflater, container, savedInstanceState);

        mInflater = inflater;

        mCommons.swipe(mSwipeRefreshLayout, this, getResources());

        mAddSublayerBtn.setVisibility(View.GONE);

        mEntity = mCommons.handleArgugments(getArguments());

        refresh();

        return v;
    }

    @Override
    public void onPostExecute(List<Layer> model) {
        try {
            mCommons.onPostExecute(model, mSublayerDataView, mInflater, getActivity());

            mProgressDialogHelper.hideProgressDialog();
        } catch (Exception e){
            mAnalytics.sendException(e);
        }
    }

    @Override
    public void refresh() {
        if(mProgressDialogHelper == null) {
            mProgressDialogHelper = new ProgressDialogHelper(getActivity());
        }

        mProgressDialogHelper.showProgressDialog();

        mCommons.getSublayersAsync(new GetSublayersTaskParams(mData, mEntity, getActivity(), this));
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_layer_sublayers_tab;
    }
}
