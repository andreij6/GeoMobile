package com.geospatialcorporation.android.geomobile.ui.fragments.dialogs;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.geospatialcorporation.android.geomobile.R;
import com.geospatialcorporation.android.geomobile.models.Interfaces.ITreeEntity;
import com.geospatialcorporation.android.geomobile.ui.Interfaces.OnFragmentInteractionListener;
import com.geospatialcorporation.android.geomobile.ui.fragments.GeoViewFragmentBase;
import com.geospatialcorporation.android.geomobile.ui.fragments.GoogleMapFragment;

import butterknife.OnClick;

public abstract class ItemDetailFragment<Entity extends ITreeEntity> extends GeoViewFragmentBase {

    protected Entity mEntity;

    @Nullable
    @OnClick(R.id.goToMapIV)
    public void goToMapIV(){
        Fragment pageFragment = new GoogleMapFragment();

        FragmentManager fragmentManager = getFragmentManager();

        fragmentManager.beginTransaction()
                .replace(R.id.content_frame, pageFragment)
                .addToBackStack(null).commit();
    }

    @Nullable
    @OnClick(R.id.goBackIV)
    public void previousView(){
        getFragmentManager().popBackStack();
    }

    @Nullable
    @OnClick(R.id.sectionTitle)
    public void goUp(){ getFragmentManager().popBackStack(); }

    @Nullable
    @OnClick(R.id.closeIV)
    public void closeFragment(){
        ((OnFragmentInteractionListener)getActivity()).closeDetailFragment();
    }

    @Nullable
    @OnClick(R.id.optionsIV)
    public void bringUpOptions(){
        options();
    }

    @Nullable
    @OnClick(R.id.landOptionsIV)
    public void bringUpOptionsLand(){
        options();
    }

    protected void options(){
        if(mPanelManager.getIsOpen()){
            closePanel();
        } else {
            Fragment f = getPanelFragment();

            f.setArguments(mEntity.toBundle());

            getFragmentManager().beginTransaction()
                    .replace(R.id.slider_content, f)
                    .commit();

            if(mIsLandscape){
                mPanelManager.expand();
            }else{
                mPanelManager.halfAnchor();
            }

            mPanelManager.touch(false);
        }
    }

    protected abstract Fragment getPanelFragment();

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

    protected View createTabView(Context context, int imageSelector) {

        View view = LayoutInflater.from(context).inflate(R.layout.tabs_bg, null);

        ImageView ti = (ImageView) view.findViewById(R.id.tabImage);

        ti.setImageDrawable(ContextCompat.getDrawable(context, imageSelector));

        return view;
    }

    public void closePanel() {
        mPanelManager.hide();
    }


}
