package com.geospatialcorporation.android.geomobile.ui.fragments.dialogs.action_dialogs.attributes;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;

import com.geospatialcorporation.android.geomobile.R;
import com.geospatialcorporation.android.geomobile.application;
import com.geospatialcorporation.android.geomobile.library.DI.Analytics.Interfaces.IGeoAnalytics;
import com.geospatialcorporation.android.geomobile.library.DI.Analytics.Models.GoogleAnalyticEvent;
import com.geospatialcorporation.android.geomobile.library.DI.Tasks.Interfaces.IGetOrderNumberTask;
import com.geospatialcorporation.android.geomobile.library.DI.Tasks.models.GetOrderNumberParams;
import com.geospatialcorporation.android.geomobile.library.DI.TreeServices.Interfaces.ILayerTreeService;
import com.geospatialcorporation.android.geomobile.library.validation.requests.IValidateRequest;
import com.geospatialcorporation.android.geomobile.library.validation.requests.LayerAttributeValidateRequest;
import com.geospatialcorporation.android.geomobile.models.AddAttributeRequest;
import com.geospatialcorporation.android.geomobile.models.Layers.Columns;
import com.geospatialcorporation.android.geomobile.models.Layers.Layer;
import com.geospatialcorporation.android.geomobile.models.Layers.LayerAttributeColumn;
import com.geospatialcorporation.android.geomobile.ui.Interfaces.IPostExecuter;

import butterknife.Bind;
import butterknife.ButterKnife;

public class CreateAttributeDialogFragment extends AttributesActionDialogBase<Layer> implements IPostExecuter<Integer> {

    @Bind(R.id.attrNameET) EditText mName;
    @Bind(R.id.columnTypes) Spinner mColumnTypes;
    @Bind(R.id.defaultValueET) EditText mDefaultValue;
    @Bind(R.id.attrHidden) CheckBox mHidden;

    IGetOrderNumberTask mTask;
    ILayerTreeService mService;
    IGeoAnalytics mAnalytics;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = getDialogBuilder();

        View v = getDialogView(R.layout.dialog_add_attributes);

        ButterKnife.bind(this, v);

        setupSpinner();

        mTask = application.getTasksComponent().provideOrderNumberTask();
        mService = application.getTreeServiceComponent().provideLayerTreeService();
        mAnalytics = application.getAnalyticsComponent().provideGeoAnalytics();

        builder.setTitle(R.string.add_attribute)
                .setView(v)
                .setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(Valid()){
                            mAnalytics.trackClick(new GoogleAnalyticEvent().CreateAttribute());
                            mTask.getOrderNumber(new GetOrderNumberParams(mData, mHidden, mColumnTypes, mName, mDefaultValue, CreateAttributeDialogFragment.this));
                            dialog.cancel();
                        }
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        return builder.create();
    }

    protected boolean Valid() {
        IValidateRequest request = new LayerAttributeValidateRequest(getValue(mName), getValue(mDefaultValue), getValue(mColumnTypes));

        return request.isValid();
    }

    @Override
    public void onPostExecute(Integer orderNum) {
        LayerAttributeColumn newColumn = new LayerAttributeColumn();

        //TODO: 400 Bad Request Error
        newColumn.setId(-1);
        newColumn.setDataType(getValue(mColumnTypes));
        newColumn.setName(getValue(mName));
        newColumn.setIsHidden(mHidden.isChecked());
        newColumn.setDefaultValue(getValue(mDefaultValue));
        newColumn.setOrderNum(orderNum + 1);

        Columns vm = new Columns(orderNum, newColumn);

        AddAttributeRequest request = new AddAttributeRequest(vm);

        mService.addColumn(mData.getId(), request);
    }

    protected void setupSpinner() {

        String[] choices = getResources().getStringArray(R.array.layer_attribute_column_types);

        ArrayAdapter<String> a = new ArrayAdapter<>(getActivity(),android.R.layout.simple_spinner_item, choices);

        a.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        mColumnTypes.setAdapter(a);

    }

    public String getValue(EditText et){
        return et.getText().toString();
    }

    public String getValue(Spinner spinner){
        return spinner.getSelectedItem().toString();
    }
}
