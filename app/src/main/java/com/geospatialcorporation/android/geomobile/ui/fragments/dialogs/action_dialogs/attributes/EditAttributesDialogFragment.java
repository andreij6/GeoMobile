package com.geospatialcorporation.android.geomobile.ui.fragments.dialogs.action_dialogs.attributes;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.geospatialcorporation.android.geomobile.R;
import com.geospatialcorporation.android.geomobile.application;
import com.geospatialcorporation.android.geomobile.library.DI.Analytics.Models.GoogleAnalyticEvent;
import com.geospatialcorporation.android.geomobile.library.DI.TreeServices.Interfaces.ILayerTreeService;
import com.geospatialcorporation.android.geomobile.models.AttributeValueVM;
import com.geospatialcorporation.android.geomobile.models.Layers.EditLayerAttributesRequest;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class EditAttributesDialogFragment extends AttributesActionDialogBase {

    LayoutInflater mInflater;
    TableLayout mTableLayout;
    HashMap<Integer, PreviousCurrent> mStartingValues;
    HashMap<Integer, String> mRequestValues;
    ILayerTreeService mService;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        AlertDialog.Builder builder = getDialogBuilder();
        View v = getDialogView(R.layout.dialog_edit_attributes);

        mService = application.getTreeServiceComponent().provideLayerTreeService();
        mAnalytics = application.getAnalyticsComponent().provideGeoAnalytics();

        mTableLayout = (TableLayout)v.findViewById(R.id.editAttributes);

        setAttributeTable();

        builder.setTitle(R.string.edit_attributes)
                .setView(v)
                .setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //TODO: Validation
                        mAnalytics.trackClick(new GoogleAnalyticEvent().EditAttributes());
                        mRequestValues = compareValues();

                        if (mRequestValues != null && !mRequestValues.isEmpty()) {
                            EditLayerAttributesRequest.RequestFeatures features =
                                    new EditLayerAttributesRequest.RequestFeatures(mData.getColumns().get(0).getFeatureId(), mRequestValues);

                            EditLayerAttributesRequest request = new EditLayerAttributesRequest(Arrays.asList(features));

                            mService.editAttributeValue(mData.getLayerId(), request);

                            Toaster("Request Sent");
                        } else {

                            Toaster("Invalid Request");
                        }

                    }
                }).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        return builder.create();
    }

    protected HashMap<Integer, String> compareValues() {
        HashMap<Integer, String> values = new HashMap<>();

        for (Integer columnId : mStartingValues.keySet()) {
            PreviousCurrent attrs = mStartingValues.get(columnId);

            if (attrs.areChanged()) {
                values.put(columnId, attrs.getInput());
            }

        }

        return values;
    }

    protected void setAttributeTable() {

        mStartingValues = new HashMap<>();

        mInflater = getActivity().getLayoutInflater();

        List<AttributeValueVM.Columns> columnsList = mData.getColumns();

        for(AttributeValueVM.Columns keyValue : columnsList) {


            TableRow row = new TableRow(mContext);

            TextView columnName = (TextView)mInflater.inflate(R.layout.template_feature_window_column_tv, null);
            columnName.setText(keyValue.getKey() + "  ");

            EditText columnValue = (EditText)mInflater.inflate(R.layout.template_feature_window_column_et, null);
            columnValue.setText(keyValue.getValue());

            mStartingValues.put(keyValue.getColumnId(), new PreviousCurrent(keyValue.getValue(), columnValue));

            row.addView(columnName);
            row.addView(columnValue);

            mTableLayout.addView(row);
        }
    }

    public static class PreviousCurrent{

        EditText mInput;
        String mValue;

        public PreviousCurrent(String value, EditText columnValue) {
            mValue = value;
            mInput = columnValue;
        }

        public String getInput() {
            return mInput.getText().toString();
        }

        public String getValue() {
            return mValue;
        }

        public boolean areChanged() {
            if(mValue != null) {
                return !mValue.equals(mInput.getText().toString());
            } else {
                return !mInput.getText().toString().isEmpty();
            }
        }
    }

}
