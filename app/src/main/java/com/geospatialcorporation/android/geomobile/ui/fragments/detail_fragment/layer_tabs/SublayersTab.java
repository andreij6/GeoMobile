package com.geospatialcorporation.android.geomobile.ui.fragments.detail_fragment.layer_tabs;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.geospatialcorporation.android.geomobile.R;
import com.geospatialcorporation.android.geomobile.application;
import com.geospatialcorporation.android.geomobile.library.DI.Analytics.Models.GoogleAnalyticEvent;
import com.geospatialcorporation.android.geomobile.library.DI.Tasks.Interfaces.IGetSublayersTask;
import com.geospatialcorporation.android.geomobile.library.DI.Tasks.models.GetSublayersTaskParams;
import com.geospatialcorporation.android.geomobile.library.DI.UIHelpers.Interfaces.DialogHelpers.ISublayerDialog;
import com.geospatialcorporation.android.geomobile.library.DI.UIHelpers.Interfaces.ILayoutRefresher;
import com.geospatialcorporation.android.geomobile.library.helpers.TableFactory;
import com.geospatialcorporation.android.geomobile.ui.Interfaces.IContentRefresher;
import com.geospatialcorporation.android.geomobile.models.Layers.Layer;
import com.geospatialcorporation.android.geomobile.ui.Interfaces.IPostExecuter;
import com.geospatialcorporation.android.geomobile.ui.fragments.detail_fragment.GeoDetailsTabBase;

import org.w3c.dom.Text;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;


public class SublayersTab extends GeoDetailsTabBase<Layer> implements IContentRefresher, IPostExecuter<List<Layer>> {

    List<Layer> mData;
    IGetSublayersTask mTask;
    ILayoutRefresher mRefresher;
    @InjectView(R.id.sublayerTableLayout) TableLayout mSublayerDataView;
    @InjectView(R.id.swipe_refresh_layout) SwipeRefreshLayout mSwipeRefreshLayout;
    LayoutInflater mInflater;
    ISublayerDialog mDialog;
    //@InjectView(R.id.sliding_layout) SlidingUpPanelLayout mPanel;

    @OnClick(R.id.addSublayers)
    public void addSublayer(){
        mDialog.create(mEntity, getActivity(), getFragmentManager());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.fragment_layer_sublayers_tab, container, false);
        mInflater = inflater;
        ButterKnife.inject(this, v);

        mRefresher = application.getUIHelperComponent().provideLayoutRefresher();
        mDialog = application.getUIHelperComponent().provideSublayerDialog();

        mSwipeRefreshLayout.setOnRefreshListener(mRefresher.build(mSwipeRefreshLayout, this));
        mSwipeRefreshLayout.setProgressBackgroundColorSchemeColor(getActivity().getResources().getColor(R.color.accent));

        mAnalytics.trackScreen(new GoogleAnalyticEvent().SublayersTab());

        //application.setSublayerFragmentPanel(mPanel);
        //mPanel.setBackgroundColor(getActivity().getResources().getColor(R.color.primary_light));
        //ISlidingPanelManager manager = new PanelManager(GeoPanel.SUBLAYER);
        //manager.setup();

        setIntentString(Layer.LAYER_INTENT);
        handleArgs();

        refresh();



        return v;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        //if(mPanelManager != null) {
        //    mPanelManager.collapse();
        //}
    }

    @Override
    public void refresh() {
        mTask = application.getTasksComponent().provideSublayersTask();
        mTask.getSublayers(new GetSublayersTaskParams(mData, mEntity, getActivity(), this));
    }

    @Override
    public void onPostExecute(List<Layer> model) {
        mSublayerDataView.removeAllViews();

        TableFactory factory = new TableFactory(getActivity(), mSublayerDataView, mInflater);

        factory.addHeaders(R.layout.template_table_header, "Visible", "Name", "Edit");

        mSublayerDataView = factory.build();

        AddAllFeatures();

        if(model != null) {
            for (final Layer layer : model) {
                TableRow row = new TableRow(getActivity());

                CheckBox visible = (CheckBox) mInflater.inflate(R.layout.template_table_checkbox, null);
                visible.setChecked(layer.getIsShowing());

                TextView columnName = (TextView) mInflater.inflate(R.layout.template_table_column, null);
                columnName.setText(layer.getName());

                ImageView edit = (ImageView) mInflater.inflate(R.layout.template_table_column_image, null);
                edit.setImageDrawable(getActivity().getDrawable(R.drawable.ic_rename_box_black_18dp));

                edit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mDialog.modify(layer, getActivity(), getFragmentManager());
                    }
                });

                row.addView(visible);
                row.addView(columnName);
                row.addView(edit);

                mSublayerDataView.addView(row);
            }
        }
    }

    protected void AddAllFeatures() {
        TableRow row = new TableRow(getActivity());

        CheckBox visible = (CheckBox)mInflater.inflate(R.layout.template_table_checkbox, null);
        visible.setChecked(true);

        TextView columnName = (TextView)mInflater.inflate(R.layout.template_table_column, null);
        columnName.setText("All Features");

        row.addView(visible);
        row.addView(columnName);

        mSublayerDataView.addView(row);
    }
}
