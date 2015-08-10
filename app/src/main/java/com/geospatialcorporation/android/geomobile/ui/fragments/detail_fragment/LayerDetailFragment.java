package com.geospatialcorporation.android.geomobile.ui.fragments.detail_fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabHost;

import com.geospatialcorporation.android.geomobile.R;
import com.geospatialcorporation.android.geomobile.models.Layers.Layer;
import com.geospatialcorporation.android.geomobile.ui.fragments.detail_fragment.layer_tabs.AttributeLayoutTab;
import com.geospatialcorporation.android.geomobile.ui.fragments.detail_fragment.layer_tabs.DetailsTab;
import com.geospatialcorporation.android.geomobile.ui.fragments.detail_fragment.layer_tabs.SublayersTab;
import com.geospatialcorporation.android.geomobile.ui.fragments.dialogs.ItemDetailFragment;

import butterknife.ButterKnife;

/**
 * Created by andre on 6/2/2015.
 */
public class LayerDetailFragment extends ItemDetailFragment<Layer> implements TabHost.OnTabChangeListener {
    private static final String TAG = LayerDetailFragment.class.getSimpleName();

    private static final String SUBLAYERS = "Sublayers";
    private static final String ATTRIBUTES = "Attributes";
    private static final String DETAILS = "Details";

    FragmentTabHost mTabHost;
    View mView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        mView = inflater.inflate(R.layout.fragment_tree_detail, null);

        ButterKnife.inject(this, mView);

        handleArguments();

        mTabHost = (FragmentTabHost)mView.findViewById(R.id.tabHost);

        mTabHost.setup(getActivity(), getChildFragmentManager(), android.R.id.tabcontent);

        mTabHost.addTab(mTabHost.newTabSpec(SUBLAYERS).setIndicator(SUBLAYERS), SublayersTab.class, getArguments());
        mTabHost.addTab(mTabHost.newTabSpec(ATTRIBUTES).setIndicator(ATTRIBUTES), AttributeLayoutTab.class, getArguments());
        mTabHost.addTab(mTabHost.newTabSpec(DETAILS).setIndicator(DETAILS), DetailsTab.class, getArguments());

        mTabHost.setCurrentTab(0);

        return mView;
    }

    @Override
    protected void handleArguments() {
        Bundle args = getArguments();

        mEntity = args.getParcelable(Layer.LAYER_INTENT);

        SetTitle(mEntity != null ? mEntity.getName() : null);
    }

    @Override
    public void onTabChanged(String tabId) {

    }

    public View.OnClickListener DeleteonClickListner = new View.OnClickListener(){
        @Override
        public void onClick(View v){
           //GeoDialogHelper.deleteLayer(getActivity(), mEntity, getFragmentManager());
        }
    };

    public Fragment getCurrentTab() {
        return getChildFragmentManager().findFragmentById(android.R.id.tabcontent);
    }
}
