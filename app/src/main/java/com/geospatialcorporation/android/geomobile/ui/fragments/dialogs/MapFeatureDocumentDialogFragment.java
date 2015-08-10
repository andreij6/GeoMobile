package com.geospatialcorporation.android.geomobile.ui.fragments.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.geospatialcorporation.android.geomobile.R;
import com.geospatialcorporation.android.geomobile.application;
import com.geospatialcorporation.android.geomobile.library.DI.Analytics.Models.GoogleAnalyticEvent;
import com.geospatialcorporation.android.geomobile.library.DI.Tasks.Interfaces.IGetDocumentsTask;
import com.geospatialcorporation.android.geomobile.library.DI.Tasks.models.GetAllDocumentsParam;
import com.geospatialcorporation.android.geomobile.library.DI.TreeServices.Implementations.LayerTreeService;
import com.geospatialcorporation.android.geomobile.library.DI.TreeServices.Interfaces.ILayerTreeService;
import com.geospatialcorporation.android.geomobile.library.helpers.ItemSelectedListener;
import com.geospatialcorporation.android.geomobile.models.Document.Document;
import com.geospatialcorporation.android.geomobile.ui.Interfaces.ISpinnerListener;
import com.geospatialcorporation.android.geomobile.ui.fragments.dialogs.base.GeoDialogFragmentBase;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class MapFeatureDocumentDialogFragment extends GeoDialogFragmentBase implements ISpinnerListener<Document> {

    IGetDocumentsTask mGetDocumentsTask;
    ILayerTreeService mLayerTreeService;
    List<Document> mDocuments;
    Document mSelected;
    int mLayerId;
    String mFeatureId;
    @InjectView(R.id.document_spinner) Spinner mDocumentSpinner;

    public void init(Context context, int layerId, String featureId) {
        mContext = context;
        mLayerId = layerId;
        mFeatureId = featureId;
        mLayerTreeService = application.getTreeServiceComponent().provideLayerTreeService();
        mAnalytics = application.getAnalyticsComponent().provideGeoAnalytics();
        mGetDocumentsTask = application.getTasksComponent().provideGetDocumentsTask();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        mGetDocumentsTask.getAllDocuments(new GetAllDocumentsParam(this, mDocuments));

        AlertDialog.Builder builder = getDialogBuilder();

        View v = getDialogView(R.layout.dialog_mapfeature_document);
        ButterKnife.inject(this, v);

        builder.setTitle(R.string.add_mapfeature_document)
                .setView(v)
                .setPositiveButton(R.string.add, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (mSelected != null) {
                            mAnalytics.trackClick(new GoogleAnalyticEvent().MapfeatureDocument());
                            mLayerTreeService.addMapFeatureDocument(mLayerId, mFeatureId, mSelected.getId());
                        }
                        dialog.cancel();
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

    public void addListenerOnSpinnerItemSelection() {
        mDocumentSpinner.setOnItemSelectedListener(new ItemSelectedListener<>(this));
    }

    public void addItemsOnSpinner(List<Document> documents) {

        mDocuments = documents;

        List<String> list = new ArrayList<>();

        list.add("Choose A Document");

        if(mDocuments != null) {
            for (Document doc : mDocuments) {
                list.add(doc.getName());
            }
        }

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(mContext, android.R.layout.simple_spinner_item, list);

        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        mDocumentSpinner.setAdapter(dataAdapter);
    }

    @Override
    public void setSelected(Document selected) {
        mSelected = selected;
    }

    @Override
    public List<Document> getData() {
        return mDocuments;
    }
}
