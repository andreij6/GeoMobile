package com.geospatialcorporation.android.geomobile.ui.fragments.dialogs.action_dialogs.layer;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.geospatialcorporation.android.geomobile.R;
import com.geospatialcorporation.android.geomobile.application;
import com.geospatialcorporation.android.geomobile.library.constants.GeometryTypeCodes;
import com.geospatialcorporation.android.geomobile.models.Layers.LegendLayer;
import com.geospatialcorporation.android.geomobile.ui.adapters.SimpleSectionedRecyclerViewAdapter;
import com.geospatialcorporation.android.geomobile.ui.adapters.recycler.EditLayerAdapter;
import com.geospatialcorporation.android.geomobile.ui.fragments.dialogs.base.GeoDialogFragmentBase;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class EditLayersDialogFragment extends GeoDialogFragmentBase {

    @Bind(R.id.edit_layers_recycler) RecyclerView mRecyclerView;
    protected SimpleSectionedRecyclerViewAdapter mSectionedAdapter;

    public void init(Context context) {
        setContext(context);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        View v = getDialogView(R.layout.dialog_edit_layer);
        ButterKnife.bind(this, v);

        List<LegendLayer> layers = application.getLegendLayerQueue();

        Collections.sort(layers);
        Collections.reverse(layers);

        EditLayerAdapter adapter = new EditLayerAdapter(mContext, layers, this);

        List<SimpleSectionedRecyclerViewAdapter.Section> sections = new ArrayList<>();

        List<String> sectionNames = Arrays.asList(getString(R.string.point), getString(R.string.lines), getString(R.string.polygon));

        int skip = 0;

        HashMap<String, Integer> geomSection = setGeomSection(layers);

        for(String name : sectionNames){
            sections.add(new SimpleSectionedRecyclerViewAdapter.Section(skip, name));
            skip += geomSection.get(name);
        }

        SimpleSectionedRecyclerViewAdapter.Section[] sectionArray = new SimpleSectionedRecyclerViewAdapter.Section[sections.size()];

        mSectionedAdapter = new SimpleSectionedRecyclerViewAdapter(mContext, R.layout.section_tree, R.id.section_text, adapter);

        mSectionedAdapter.setSections(sections.toArray(sectionArray));

        mRecyclerView.setAdapter(mSectionedAdapter);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(mContext);

        mRecyclerView.setLayoutManager(layoutManager);

        return getDialogBuilder()
                .setTitle(R.string.edit_layer)
                .setView(v)
                .setNegativeButton(android.R.string.cancel,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        }).create();
    }

    public HashMap<String, Integer> setGeomSection(List<LegendLayer> layers) {
        HashMap<String, Integer> result = new HashMap<>();

        int pointCount = getCount(layers, GeometryTypeCodes.Point);
        int lineCount = getCount(layers, GeometryTypeCodes.Line);
        int polygonCount = getCount(layers, GeometryTypeCodes.Polygon);

        result.put(getString(R.string.point), pointCount);
        result.put(getString(R.string.lines), lineCount);
        result.put(getString(R.string.polygon), polygonCount);

        return result;
    }

    protected int getCount(List<LegendLayer> layers, int geomCode) {
        int count = 0;

        if(geomCode == GeometryTypeCodes.Point){
            for (LegendLayer layer : layers) {
                int code = layer.getLayer().getGeometryTypeCodeId();
                if( code == GeometryTypeCodes.Point ||
                        code == GeometryTypeCodes.MultiPoint)
                {
                    count++;
                }
            }
        }

        if(geomCode == GeometryTypeCodes.Line){
            for (LegendLayer layer : layers) {
                int code = layer.getLayer().getGeometryTypeCodeId();

                if( code == GeometryTypeCodes.Line ||
                        code == GeometryTypeCodes.MultiLine)
                {
                    count++;
                }
            }
        }

        if(geomCode == GeometryTypeCodes.Polygon){
            for (LegendLayer layer : layers) {
                int code = layer.getLayer().getGeometryTypeCodeId();

                if( code == GeometryTypeCodes.Polygon ||
                        code == GeometryTypeCodes.MultiPolygon)
                {
                    count++;
                }

            }
        }

        return count;
    }


}
