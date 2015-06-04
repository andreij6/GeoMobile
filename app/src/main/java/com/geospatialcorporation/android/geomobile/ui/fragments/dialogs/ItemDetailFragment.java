package com.geospatialcorporation.android.geomobile.ui.fragments.dialogs;

import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.geospatialcorporation.android.geomobile.library.services.DocumentTreeService;
import com.geospatialcorporation.android.geomobile.library.services.FolderTreeService;
import com.geospatialcorporation.android.geomobile.library.helpers.Interfaces.ITreeService;
import com.geospatialcorporation.android.geomobile.library.services.LayerTreeService;
import com.geospatialcorporation.android.geomobile.models.Folders.Folder;
import com.geospatialcorporation.android.geomobile.models.Interfaces.INamedEntity;
import com.geospatialcorporation.android.geomobile.models.Interfaces.IdModel;
import com.geospatialcorporation.android.geomobile.models.Layers.Layer;
import com.geospatialcorporation.android.geomobile.models.Library.Document;

/**
 * Created by andre on 6/2/2015.
 */
public class ItemDetailFragment<ITreeEntity> extends Fragment {

    protected ITreeEntity mEntity;

    //region Getters & Setters
    public Button getButton() {
        return mButton;
    }

    public void setButton(Button button) {
        mButton = button;
    }

    public EditText getEditText() {
        return mEditText;
    }

    public void setEditText(EditText editText) {
        mEditText = editText;
    }
    //endregion

    Button mButton;
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
            mButton.setVisibility(View.VISIBLE);
        }
    };

    protected void SetupRename(){
        mButton.setVisibility(View.GONE);
        mEditText.setVisibility(View.GONE);

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ITreeService service = null;

                if(mEntity instanceof Document){
                    service = new DocumentTreeService();
                } else if( mEntity instanceof Layer){
                    service = new LayerTreeService();
                } else if(mEntity instanceof Folder){
                    service = new FolderTreeService();
                } else {}

                if(service != null) {

                    Boolean success = service.rename(((IdModel)mEntity).getId(), mEditText.getText().toString());

                    if(!success){
                        Toast.makeText(getActivity(), "Not Authorized to Rename Doc", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getActivity(), "Success!", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }




}
