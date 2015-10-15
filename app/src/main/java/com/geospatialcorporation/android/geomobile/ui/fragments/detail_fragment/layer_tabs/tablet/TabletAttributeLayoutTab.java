package com.geospatialcorporation.android.geomobile.ui.fragments.detail_fragment.layer_tabs.tablet;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.geospatialcorporation.android.geomobile.R;
import com.geospatialcorporation.android.geomobile.application;
import com.geospatialcorporation.android.geomobile.library.DI.Tasks.Interfaces.IGetLayerAttributeColumnsTask;
import com.geospatialcorporation.android.geomobile.library.DI.Tasks.models.GetLayerAttributesTaskParams;
import com.geospatialcorporation.android.geomobile.library.helpers.TableFactory;
import com.geospatialcorporation.android.geomobile.library.services.AttributeLayoutTabCommons.AttributeLayoutTabCommon;
import com.geospatialcorporation.android.geomobile.library.services.AttributeLayoutTabCommons.IAttributeLayoutTabCommon;
import com.geospatialcorporation.android.geomobile.models.Layers.Layer;
import com.geospatialcorporation.android.geomobile.models.Layers.LayerAttributeColumn;
import com.geospatialcorporation.android.geomobile.ui.Interfaces.IContentRefresher;
import com.geospatialcorporation.android.geomobile.ui.Interfaces.IPostExecuter;
import com.geospatialcorporation.android.geomobile.ui.fragments.TabGeoViewFragmentBase;

import java.util.List;

import butterknife.Bind;

public class TabletAttributeLayoutTab extends TabGeoViewFragmentBase implements IContentRefresher, IPostExecuter<List<LayerAttributeColumn>> {

    IAttributeLayoutTabCommon mCommons;
    IGetLayerAttributeColumnsTask mTask;

    List<LayerAttributeColumn> mAttributeColumns;
    LayoutInflater mInflater;
    Layer mEntity;

    @Bind(R.id.attributesTableLayout) TableLayout mTableLayout;
    @Bind(R.id.swipe_refresh_layout) SwipeRefreshLayout mSwipeRefreshLayout;



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCommons = new AttributeLayoutTabCommon();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = super.onCreateView(inflater, container, savedInstanceState);

        mInflater = inflater;

        mCommons.swipe(mSwipeRefreshLayout, this, getResources());

        mEntity = mCommons.handleArguments(getArguments());

        refresh();

        return v;
    }

    @Override
    public void onPostExecute(List<LayerAttributeColumn> model) {
        try {
            mTableLayout.removeAllViews();

            TableFactory factory = new TableFactory(getActivity(), mTableLayout, mInflater);

            factory.addHeaders(R.layout.template_table_header, "Name", "Type", "Default", "Hidden");

            mTableLayout = factory.build();

            if (model != null) {
                for (LayerAttributeColumn layer : model) {
                    TableRow row = new TableRow(getActivity());

                    TextView name = (TextView) mInflater.inflate(R.layout.template_table_column, null);
                    name.setText(layer.getName());

                    TextView type = (TextView) mInflater.inflate(R.layout.template_table_column, null);
                    type.setText(layer.getDataTypeViewName());

                    TextView defaultValue = (TextView) mInflater.inflate(R.layout.template_table_column, null);
                    defaultValue.setText(layer.getDefaultValue());

                    CheckBox hidden = (CheckBox) mInflater.inflate(R.layout.template_table_checkbox, null);
                    hidden.setChecked(layer.getIsHidden());
                    hidden.setEnabled(false);

                    row.addView(name);
                    row.addView(type);
                    row.addView(defaultValue);
                    row.addView(hidden);

                    mTableLayout.addView(row);
                }
            }
        } catch (Exception e){
            mAnalytics.sendException(e);
        }
    }

    @Override
    public void refresh() {
        mTask = application.getTasksComponent().provideAttributeColumnsTask();

        mTask.getColumns(new GetLayerAttributesTaskParams(mEntity, getActivity(), mAttributeColumns, this));
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_layer_attributes_tab;
    }
}
