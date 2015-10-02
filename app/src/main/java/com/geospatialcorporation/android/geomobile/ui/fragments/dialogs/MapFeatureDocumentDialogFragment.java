package com.geospatialcorporation.android.geomobile.ui.fragments.dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.geospatialcorporation.android.geomobile.R;
import com.geospatialcorporation.android.geomobile.application;
import com.geospatialcorporation.android.geomobile.library.DI.Analytics.Models.GoogleAnalyticEvent;
import com.geospatialcorporation.android.geomobile.library.DI.Tasks.Interfaces.IGetDocumentsTask;
import com.geospatialcorporation.android.geomobile.library.DI.Tasks.models.GetAllDocumentsParam;
import com.geospatialcorporation.android.geomobile.library.DI.TreeServices.Implementations.LayerTreeService;
import com.geospatialcorporation.android.geomobile.library.DI.TreeServices.Interfaces.IDocumentTreeService;
import com.geospatialcorporation.android.geomobile.library.DI.TreeServices.Interfaces.ILayerTreeService;
import com.geospatialcorporation.android.geomobile.library.DocumentSentCallback;
import com.geospatialcorporation.android.geomobile.library.ISendFileCallback;
import com.geospatialcorporation.android.geomobile.library.constants.MediaConstants;
import com.geospatialcorporation.android.geomobile.library.helpers.DataHelper;
import com.geospatialcorporation.android.geomobile.library.helpers.ItemSelectedListener;
import com.geospatialcorporation.android.geomobile.library.helpers.MediaHelper;
import com.geospatialcorporation.android.geomobile.library.rest.TreeService;
import com.geospatialcorporation.android.geomobile.models.Document.Document;
import com.geospatialcorporation.android.geomobile.models.Folders.Folder;
import com.geospatialcorporation.android.geomobile.ui.Interfaces.ISpinnerListener;
import com.geospatialcorporation.android.geomobile.ui.MainActivity;
import com.geospatialcorporation.android.geomobile.ui.fragments.dialogs.base.GeoDialogFragmentBase;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MapFeatureDocumentDialogFragment extends GeoDialogFragmentBase implements ISpinnerListener<Document> {

    IGetDocumentsTask mGetDocumentsTask;
    ILayerTreeService mLayerTreeService;
    List<Document> mDocuments;

    Document mSelected;
    int mLayerId;
    String mFeatureId;
    @Bind(R.id.document_spinner) Spinner mDocumentSpinner;

    @OnClick(R.id.addDocumentSection)
    public void addDocument(){
        Intent chooseFileIntent = new Intent(Intent.ACTION_GET_CONTENT);
        chooseFileIntent.setType("file/*");

        application.setFeatureWindowDocumentIds(mLayerId, mFeatureId);

        MapFeatureDocumentDialogFragment.this.getDialog().cancel();

        getActivity().startActivityForResult(chooseFileIntent, MainActivity.MediaConstants.PICK_FILE_REQUEST_FEATUREWINDOW);
    }

    @OnClick(R.id.takePhotoSection)
    public void takePhoto(){
        Intent takePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        MediaHelper mediaHelper = new MediaHelper(getActivity());

        application.mMediaUri = mediaHelper.getOutputMediaFileUri(MediaConstants.MEDIA_TYPE_IMAGE);

        if (application.mMediaUri == null) {
            // display an error
            Toast.makeText(getActivity(), R.string.error_external_storage, Toast.LENGTH_LONG).show();
        }
        else {
            mAnalytics.trackClick(new GoogleAnalyticEvent().UploadImage());
            takePhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, application.mMediaUri);

            application.setFeatureWindowDocumentIds(mLayerId, mFeatureId);


            MapFeatureDocumentDialogFragment.this.getDialog().cancel();

            getActivity().startActivityForResult(takePhotoIntent, MediaConstants.TAKE_IMAGE_REQUEST_FEATUREWINDOW);
        }
    }

    @OnClick(R.id.choosePhotoSection)
    public void choosePhotoSection(){
        Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
        getIntent.setType("image/*");

        Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickIntent.setType("image/*");

        Intent chooserIntent = Intent.createChooser(getIntent, "Select Image");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{pickIntent});

        MapFeatureDocumentDialogFragment.this.getDialog().cancel();

        application.setFeatureWindowDocumentIds(mLayerId, mFeatureId);


        getActivity().startActivityForResult(chooserIntent, MediaConstants.PICK_IMAGE_REQUEST_FEATUREWINDOW);
    }

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
        ButterKnife.bind(this, v);

        preloadSpinner();

        builder.setTitle(R.string.add_mapfeature_document)
                .setView(v)
                .setPositiveButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        return builder.create();
    }

    private void preloadSpinner() {
        ArrayList<String> list = new ArrayList<>();
        list.add("Loading Documents");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(mContext, android.R.layout.simple_spinner_item, list);

        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        mDocumentSpinner.setAdapter(dataAdapter);
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

        if (mSelected != null) {
            mAnalytics.trackClick(new GoogleAnalyticEvent().MapfeatureDocument());
            mLayerTreeService.addMapFeatureDocument(mLayerId, mFeatureId, mSelected.getId());
        }

        getDialog().cancel();

    }

    @Override
    public List<Document> getData() {
        return mDocuments;
    }




}
