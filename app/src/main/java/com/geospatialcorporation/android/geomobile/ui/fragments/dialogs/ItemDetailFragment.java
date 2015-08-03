package com.geospatialcorporation.android.geomobile.ui.fragments.dialogs;

import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.geospatialcorporation.android.geomobile.application;
import com.geospatialcorporation.android.geomobile.library.DI.TreeServices.Interfaces.IDocumentTreeService;
import com.geospatialcorporation.android.geomobile.library.DI.TreeServices.Interfaces.IFolderTreeService;
import com.geospatialcorporation.android.geomobile.library.DI.TreeServices.Interfaces.ILayerTreeService;
import com.geospatialcorporation.android.geomobile.library.DI.TreeServices.TreeServiceComponent;
import com.geospatialcorporation.android.geomobile.models.Folders.Folder;
import com.geospatialcorporation.android.geomobile.models.Interfaces.INamedEntity;
import com.geospatialcorporation.android.geomobile.models.Interfaces.IdModel;
import com.geospatialcorporation.android.geomobile.models.Layers.Layer;
import com.geospatialcorporation.android.geomobile.models.Document.Document;
import com.geospatialcorporation.android.geomobile.ui.fragments.GeoViewFragmentBase;

public class ItemDetailFragment<ITreeEntity> extends GeoViewFragmentBase {

    protected ITreeEntity mEntity;

    //region Getters & Setters
    public EditText getEditText() {
        return mEditText;
    }

    public void setEditText(EditText editText) {
        mEditText = editText;
    }
    //endregion

    EditText mEditText;

    protected void handleArguments(){
        Toast.makeText(getActivity(), "Override Me", Toast.LENGTH_LONG).show();
    }

    protected void SetupUI(){
        Toast.makeText(getActivity(), "Override Me", Toast.LENGTH_LONG).show();
    }

    protected View.OnClickListener BackButtonClicked = new View.OnClickListener(){
        @Override
        public void onClick(View v){
            getFragmentManager().popBackStack();
        }
    };

    protected View.OnClickListener EditNameClicked = new View.OnClickListener(){
        @Override
        public void onClick(View v){
            mEditText.setText(((INamedEntity)mEntity).getName());
            mEditText.setVisibility(View.VISIBLE);
        }
    };

    protected void SetupRename(){
        mEditText.setVisibility(View.GONE);

        mEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    TreeServiceComponent component = application.getTreeServiceComponent();

                    int id = ((IdModel)mEntity).getId();
                    String name = mEditText.getText().toString();
                    if (mEntity instanceof Document) {
                        IDocumentTreeService  docService = component.provideDocumentTreeService();
                        docService.rename(id, name );
                    } else if (mEntity instanceof Layer) {
                        ILayerTreeService layerService = component.provideLayerTreeService();
                        layerService.rename(id, name);
                    } else if (mEntity instanceof Folder) {
                        IFolderTreeService folderService = component.provideFolderTreeService();
                        folderService.rename(id, name);
                    } else {
                    }


                    mEditText.setVisibility(View.GONE);
                }
                return false;
            }
        });

    }




}
