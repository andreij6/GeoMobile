package com.geospatialcorporation.android.geomobile.ui.fragments.dialogs;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

/**
 * Created by andre on 6/2/2015.
 */
public class ItemDetailFragment<Parcelable> extends Fragment {

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
}
