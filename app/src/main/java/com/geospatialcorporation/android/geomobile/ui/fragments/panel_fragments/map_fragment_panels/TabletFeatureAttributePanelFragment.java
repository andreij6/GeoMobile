package com.geospatialcorporation.android.geomobile.ui.fragments.panel_fragments.map_fragment_panels;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.geospatialcorporation.android.geomobile.R;
import com.geospatialcorporation.android.geomobile.application;
import com.geospatialcorporation.android.geomobile.library.DI.Analytics.Models.GoogleAnalyticEvent;
import com.geospatialcorporation.android.geomobile.library.DI.TreeServices.Interfaces.ILayerTreeService;
import com.geospatialcorporation.android.geomobile.library.constants.ColumnDataTypes;
import com.geospatialcorporation.android.geomobile.models.AttributeValueVM;
import com.geospatialcorporation.android.geomobile.models.Layers.EditLayerAttributesRequest;
import com.geospatialcorporation.android.geomobile.ui.MainTabletActivity;
import com.geospatialcorporation.android.geomobile.ui.fragments.TabGeoViewFragmentBase;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

public class TabletFeatureAttributePanelFragment extends TabGeoViewFragmentBase {

    AttributeValueVM mData;
    LayoutInflater mInflater;
    HashMap<Integer, PreviousCurrent> mStartingValues;
    HashMap<Integer, String> mRequestValues;
    ILayerTreeService mService;

    @Bind(R.id.editAttributes)
    TableLayout mTableLayout;

    @OnClick(R.id.close)
    public void closeFeatureWindow(){
        MainTabletActivity activity = ((MainTabletActivity)getActivity());

        activity.closeInfoFragment();
    }

    @OnClick(R.id.saveButton)
    public void save(){
        getFragmentManager().popBackStack();

        mAnalytics.trackClick(new GoogleAnalyticEvent().EditAttributes());
        mRequestValues = compareValues();

        if (mRequestValues != null && !mRequestValues.isEmpty()) {
            //hideSoftKeyboard();

            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    EditLayerAttributesRequest.RequestFeatures features =
                            new EditLayerAttributesRequest.RequestFeatures(mData.getColumns().get(0).getFeatureId(), mRequestValues);

                    EditLayerAttributesRequest request = new EditLayerAttributesRequest(Arrays.asList(features));

                    application.setFeatureWindowDocumentIds(mData.getLayerId(), mData.getColumns().get(0).getFeatureId());

                    mService.editAttributeValue(mData.getLayerId(), request);

                    application.getMainTabletActivity().getFragmentManager().popBackStack();
                }
            }, 250);

        } else {

            Toaster("No Changes Made");
        }


    }

    @Override
    protected int getLayoutResId() {
        return R.layout.feature_attribute_panel_fragment_tablet;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = super.onCreateView(inflater, container, savedInstanceState);

        mService = application.getTreeServiceComponent().provideLayerTreeService();

        handleArgs();

        setAttributeTable();

        return v;
    }

    protected void handleArgs() {
        Bundle args = getArguments();

        mData = args.getParcelable(AttributeValueVM.ATTRIBUTE_VALUE);
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

            TableRow row = new TableRow(getActivity());

            TextView columnName = (TextView)mInflater.inflate(R.layout.template_feature_window_column_tv, null);
            columnName.setText(keyValue.getKey() + "  ");

            row.addView(columnName);

            mTableLayout.addView(row);

            row = new TableRow(getActivity());

            EditText columnValue = (EditText)mInflater.inflate(R.layout.template_feature_window_column_et, null);
            columnValue.setText(keyValue.getValue());
            mStartingValues.put(keyValue.getColumnId(), new PreviousCurrent(keyValue.getValue(), columnValue));
            columnValue.setEnabled(keyValue.isEditable());


            setEditTextType(keyValue, columnValue);

            row.addView(columnValue);

            mTableLayout.addView(row);
            mTableLayout.setStretchAllColumns(true);

        }
    }

    protected void setEditTextType(AttributeValueVM.Columns keyValue, final EditText columnValue) {
        Integer type = keyValue.getDataType();

        if(type.equals(ColumnDataTypes.INTEGER) || type.equals(ColumnDataTypes.DECIMAL)){

            columnValue.setInputType(InputType.TYPE_CLASS_NUMBER);

        } else if(type.equals(ColumnDataTypes.DATE)){
            //columnValue.setInputType(InputType.TYPE_DATETIME_VARIATION_DATE);
            columnValue.setInputType(InputType.TYPE_CLASS_TEXT);

            columnValue.setHint(R.string.date_hint);

            //columnValue.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            //    @Override
            //    public void onFocusChange(View v, boolean hasFocus) {
            //        Toaster("DatePicker");
            //    }
            //});

        } else if(type.equals(ColumnDataTypes.DATE_TIME)){
            //columnValue.setInputType(InputType.TYPE_CLASS_DATETIME);
            columnValue.setInputType(InputType.TYPE_CLASS_TEXT);

            columnValue.setHint(R.string.datetime_hint);

            //columnValue.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            //    @Override
            //    public void onFocusChange(View v, boolean hasFocus) {
            //        if (hasFocus) {
            //            Toaster("Open DateTime Picker");
            //        }
            //    }
            //});
        } else {
            columnValue.setInputType(InputType.TYPE_CLASS_TEXT);
        }

        if(type.equals(ColumnDataTypes.BOOLEAN)){
            columnValue.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (s.toString().toLowerCase().equals("true") || s.toString().toLowerCase().equals("false")) {
                        columnValue.setError(null);
                    } else {
                        columnValue.setError(getActivity().getString(R.string.boolean_validation));
                    }
                }
            });

            columnValue.setHint(R.string.boolean_hint);
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
