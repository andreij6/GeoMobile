package com.geospatialcorporation.android.geomobile.library.services.SublayerTabCommons;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.widget.CheckBox;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.geospatialcorporation.android.geomobile.R;
import com.geospatialcorporation.android.geomobile.application;
import com.geospatialcorporation.android.geomobile.library.DI.Tasks.Interfaces.IGetSublayersTask;
import com.geospatialcorporation.android.geomobile.library.DI.Tasks.models.GetSublayersTaskParams;
import com.geospatialcorporation.android.geomobile.library.DI.UIHelpers.Interfaces.ILayoutRefresher;
import com.geospatialcorporation.android.geomobile.library.helpers.TableFactory;
import com.geospatialcorporation.android.geomobile.models.Layers.Layer;
import com.geospatialcorporation.android.geomobile.ui.Interfaces.IContentRefresher;

import java.util.List;

public class SublayerTabCommon implements ISublayerTabCommon {

    IGetSublayersTask mTask;

    public SublayerTabCommon(){
        mTask = application.getTasksComponent().provideSublayersTask();
    }

    @Override
    public void swipe(SwipeRefreshLayout swipeRefreshLayout, IContentRefresher contentRefresher, Resources resources) {
        ILayoutRefresher refresher = application.getUIHelperComponent().provideLayoutRefresher();

        swipeRefreshLayout.setOnRefreshListener(refresher.build(swipeRefreshLayout, contentRefresher));
        swipeRefreshLayout.setProgressBackgroundColorSchemeColor(resources.getColor(R.color.white));

    }

    @Override
    public void onPostExecute(List<Layer> model, TableLayout sublayerDataView, LayoutInflater inflater, Context context) {
        sublayerDataView.removeAllViews();

        TableFactory factory = new TableFactory(context, sublayerDataView, inflater);

        //factory.addHeaders(R.layout.template_table_header, "Visible", "Name", "Edit");
        factory.addHeaders(R.layout.template_table_header, "Visible", "Name");

        sublayerDataView = factory.build();

        AddAllFeatures(context, inflater, sublayerDataView);

        if(model != null) {
            for (final Layer layer : model) {
                TableRow row = new TableRow(context);

                CheckBox visible = (CheckBox) inflater.inflate(R.layout.template_table_checkbox, null);
                visible.setChecked(layer.getIsShowing());
                visible.setEnabled(false);

                TextView columnName = (TextView) inflater.inflate(R.layout.template_table_column, null);
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

                sublayerDataView.addView(row);
            }
        }
    }

    @Override
    public void getSublayersAsync(GetSublayersTaskParams params) {
        mTask.getSublayers(params);
    }

    @Override
    public Layer handleArgugments(Bundle args) {
        return args.getParcelable(Layer.LAYER_INTENT);
    }

    protected void AddAllFeatures(Context context, LayoutInflater inflater, TableLayout sublayerDataView) {
        TableRow row = new TableRow(context);

        CheckBox visible = (CheckBox)inflater.inflate(R.layout.template_table_checkbox, null);
        visible.setChecked(true);
        visible.setEnabled(false);

        TextView columnName = (TextView)inflater.inflate(R.layout.template_table_column, null);
        columnName.setText("All Features");

        row.addView(visible);
        row.addView(columnName);

        sublayerDataView.addView(row);
    }
}
