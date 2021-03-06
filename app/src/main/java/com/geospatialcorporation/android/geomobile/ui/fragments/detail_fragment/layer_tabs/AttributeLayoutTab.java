package com.geospatialcorporation.android.geomobile.ui.fragments.detail_fragment.layer_tabs;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
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
import com.geospatialcorporation.android.geomobile.library.DI.Analytics.Models.GoogleAnalyticEvent;
import com.geospatialcorporation.android.geomobile.library.DI.Tasks.Interfaces.IGetLayerAttributeColumnsTask;
import com.geospatialcorporation.android.geomobile.library.DI.Tasks.models.GetLayerAttributesTaskParams;
import com.geospatialcorporation.android.geomobile.library.DI.UIHelpers.Interfaces.DialogHelpers.ILayerDialog;
import com.geospatialcorporation.android.geomobile.library.DI.UIHelpers.Interfaces.ILayoutRefresher;
import com.geospatialcorporation.android.geomobile.library.DI.UIHelpers.UIHelperComponent;
import com.geospatialcorporation.android.geomobile.library.helpers.TableFactory;
import com.geospatialcorporation.android.geomobile.models.Layers.Layer;
import com.geospatialcorporation.android.geomobile.models.Layers.LayerAttributeColumn;
import com.geospatialcorporation.android.geomobile.ui.Interfaces.IContentRefresher;
import com.geospatialcorporation.android.geomobile.ui.Interfaces.IPostExecuter;
import com.geospatialcorporation.android.geomobile.ui.fragments.detail_fragment.GeoDetailsTabBase;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class AttributeLayoutTab extends GeoDetailsTabBase<Layer> implements IContentRefresher, IPostExecuter<List<LayerAttributeColumn>> {

    private static final String TAG = AttributeLayoutTab.class.getSimpleName();

    List<LayerAttributeColumn> mAttributeColumns;
    IGetLayerAttributeColumnsTask mTask;
    ILayerDialog mLayerDialog;
    ILayoutRefresher mRefresher;

    @Bind(R.id.attributesTableLayout) TableLayout mTableLayout;
    @Bind(R.id.swipe_refresh_layout) SwipeRefreshLayout mSwipeRefreshLayout;
    LayoutInflater mInflater;
    //@InjectView(R.id.sliding_layout) SlidingUpPanelLayout mPanel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.fragment_layer_attributes_tab, container, false);
        ButterKnife.bind(this, v);
        mInflater = inflater;

        UIHelperComponent component = application.getUIHelperComponent();
        mRefresher = component.provideLayoutRefresher();
        mLayerDialog = component.provideLayerDialog();

        mSwipeRefreshLayout.setOnRefreshListener(mRefresher.build(mSwipeRefreshLayout, this));
        mSwipeRefreshLayout.setProgressBackgroundColorSchemeColor(ContextCompat.getColor(getActivity(), R.color.white));

        //application.setLayerAttributePanel(mPanel);
        //mPanelManager = new PanelManager(GeoPanel.LAYER_ATTRIBUTE);
        //mPanelManager.setup();

        mAnalytics.trackScreen(new GoogleAnalyticEvent().AttributeLayoutTab());

        setIntentString(Layer.LAYER_INTENT);
        handleArgs();

        refresh();

        return v;
    }

    @Override
    public void refresh() {
        mTask = application.getTasksComponent().provideAttributeColumnsTask();

        mTask.getColumns(new GetLayerAttributesTaskParams(mEntity, getActivity(), mAttributeColumns, this));
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
        } catch (NullPointerException e){
            mAnalytics.sendException(e);
        } catch (Exception e){
            mAnalytics.sendException(e);
        }
    }
}
