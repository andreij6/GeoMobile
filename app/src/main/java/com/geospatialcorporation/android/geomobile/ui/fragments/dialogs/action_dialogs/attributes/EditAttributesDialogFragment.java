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
import com.geospatialcorporation.android.geomobile.models.AttributeValueVM;

import java.util.HashMap;
import java.util.List;

public class EditAttributesDialogFragment extends AttributesActionDialogBase {

    LayoutInflater mInflater;
    TableLayout mTableLayout;
    HashMap<Integer, PreviousCurrent> mStartingValues;
    HashMap<Integer, String> mRequestValues;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        AlertDialog.Builder builder = getDialogBuilder();
        View v = getDialogView(R.layout.dialog_edit_attributes);

        mTableLayout = (TableLayout)v.findViewById(R.id.editAttributes);

        setAttributeTable();

        builder.setTitle(R.string.edit_attributes)
                .setView(v)
                .setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //TODO: Validation
                        //mAnalytics.trackClick(new GoogleAnalyticEvent().CreateFolder());
                        mRequestValues = compareValues();

                        if (mRequestValues != null && !mRequestValues.isEmpty()) {
                            SendAttributeValuesRequest request = new SendAttributeValuesRequest();
                            request.setValues(mRequestValues);
                            request.setId(mData.getColumns().get(0).getFeatureId());

                            Toaster(request.toString());
                        }

                        Toaster(mRequestValues.isEmpty() + "");

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
            return !mValue.equals(mInput.getText().toString());
        }
    }

    public static class SendAttributeValuesRequest{

        private String mId;
        private HashMap<Integer, String> mValues;

        public void setId(String id) {
            mId = id;
        }

        public void setValues(HashMap<Integer,String> values) {
            mValues = values;
        }

        @Override
        public String toString() {
            String result = " Id: " + mId + " Values {";

            for(Integer key : mValues.keySet()){
                result += " " + key + " : " + mValues.get(key) + " ";
            }

            result += " } ";

            return result;
        }
    }

}
