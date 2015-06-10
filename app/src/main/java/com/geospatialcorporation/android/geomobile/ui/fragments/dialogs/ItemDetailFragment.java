package com.geospatialcorporation.android.geomobile.ui.fragments.dialogs;

import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.geospatialcorporation.android.geomobile.library.services.DocumentTreeService;
import com.geospatialcorporation.android.geomobile.library.services.FolderTreeService;
import com.geospatialcorporation.android.geomobile.library.helpers.Interfaces.ITreeService;
import com.geospatialcorporation.android.geomobile.library.services.LayerTreeService;
import com.geospatialcorporation.android.geomobile.models.Folders.Folder;
import com.geospatialcorporation.android.geomobile.models.Interfaces.INamedEntity;
import com.geospatialcorporation.android.geomobile.models.Interfaces.IdModel;
import com.geospatialcorporation.android.geomobile.models.Layers.Layer;
import com.geospatialcorporation.android.geomobile.models.Document.Document;
import com.geospatialcorporation.android.geomobile.ui.fragments.GeoViewFragmentBase;

/**
 * Created by andre on 6/2/2015.
 */
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



    protected void HandleArguments(){
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
                    ITreeService service = null;

                    if (mEntity instanceof Document) {
                        service = new DocumentTreeService();
                    } else if (mEntity instanceof Layer) {
                        service = new LayerTreeService();
                    } else if (mEntity instanceof Folder) {
                        service = new FolderTreeService();
                    } else {
                    }

                    if (service != null) {

                        Boolean success = service.rename(((IdModel) mEntity).getId(), mEditText.getText().toString());

                        if (!success) {
                            Toast.makeText(getActivity(), "Not Authorized to rename " + mEntity.getClass().getSimpleName(), Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(getActivity(), "Success!", Toast.LENGTH_LONG).show();
                        }
                    }

                    mEditText.setVisibility(View.GONE);
                }
                return false;
            }
        });

    }




}
