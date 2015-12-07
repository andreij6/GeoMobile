package com.geospatialcorporation.android.geomobile.ui.fragments.detail_fragment.layer_tabs;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
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
import com.geospatialcorporation.android.geomobile.models.Layers.Layer;
import com.geospatialcorporation.android.geomobile.ui.Interfaces.IContentRefresher;
import com.geospatialcorporation.android.geomobile.ui.Interfaces.IPostExecuter;
import com.geospatialcorporation.android.geomobile.ui.fragments.detail_fragment.GeoDetailsTabBase;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class SublayersTab extends GeoDetailsTabBase<Layer> implements IContentRefresher, IPostExecuter<List<Layer>> {

    List<Layer> mData;
    IGetSublayersTask mTask;
    ILayoutRefresher mRefresher;
    @Bind(R.id.sublayerTableLayout) TableLayout mSublayerDataView;
    @Bind(R.id.swipe_refresh_layout) SwipeRefreshLayout mSwipeRefreshLayout;
    @Bind(R.id.addSublayers) Button mAddSublayerBtn;
    LayoutInflater mInflater;
    ISublayerDialog mDialog;

    @OnClick(R.id.addSublayers)
    public void addSublayer(){
        mDialog.create(mEntity, getActivity(), getFragmentManager());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.fragment_layer_sublayers_tab, container, false);
        mInflater = inflater;
        ButterKnife.bind(this, v);

        mRefresher = application.getUIHelperComponent().provideLayoutRefresher();
        mDialog = application.getUIHelperComponent().provideSublayerDialog();

        mSwipeRefreshLayout.setOnRefreshListener(mRefresher.build(mSwipeRefreshLayout, this));
        mSwipeRefreshLayout.setProgressBackgroundColorSchemeColor(ContextCompat.getColor(getActivity(), R.color.white));

        mAnalytics.trackScreen(new GoogleAnalyticEvent().SublayersTab());

        mAddSublayerBtn.setVisibility(View.INVISIBLE);

        setIntentString(Layer.LAYER_INTENT);
        handleArgs();

        refresh();



        return v;
    }

    @Override
    public void refresh() {
        mTask = application.getTasksComponent().provideSublayersTask();
        mTask.getSublayers(new GetSublayersTaskParams(mData, mEntity, getActivity(), this));
    }

    @Override
    public void onPostExecute(List<Layer> model) {
        try {
            mSublayerDataView.removeAllViews();

            TableFactory factory = new TableFactory(getActivity(), mSublayerDataView, mInflater);

            //factory.addHeaders(R.layout.template_table_header, "Visible", "Name", "Edit");
            factory.addHeaders(R.layout.template_table_header, "", "Name");

            mSublayerDataView = factory.build();

            AddAllFeatures();

            if (model != null) {
                for (final Layer layer : model) {
                    TableRow row = new TableRow(getActivity());

                    CheckBox visible = (CheckBox) mInflater.inflate(R.layout.template_table_checkbox, null);
                    visible.setChecked(layer.getIsShowing());
                    visible.setEnabled(false);

                    TextView columnName = (TextView) mInflater.inflate(R.layout.template_table_column, null);
                    columnName.setText(layer.getName());

                    //ImageView edit = (ImageView) mInflater.inflate(R.layout.template_table_column_image, null);
                    //edit.setImageDrawable(getActivity().getDrawable(R.drawable.ic_rename_box_black_18dp));

                    //edit.setOnClickListener(new View.OnClickListener() {
                    //    @Override
                    //    public void onClick(View v) {
                    //        mDialog.modify(layer, getActivity(), getFragmentManager());
                    //    }
                    //});

                    row.addView(visible);
                    row.addView(columnName);
                    //row.addView(edit);

                    mSublayerDataView.addView(row);
                }
            }
        } catch (Exception e){
            mAnalytics.sendException(e);
        }
    }

    protected void AddAllFeatures() {
        TableRow row = new TableRow(getActivity());

        CheckBox visible = (CheckBox)mInflater.inflate(R.layout.template_table_checkbox, null);
        visible.setChecked(true);
        visible.setEnabled(false);

        TextView columnName = (TextView)mInflater.inflate(R.layout.template_table_column, null);
        columnName.setText("All Features");

        row.addView(visible);
        row.addView(columnName);

        mSublayerDataView.addView(row);
    }
}
