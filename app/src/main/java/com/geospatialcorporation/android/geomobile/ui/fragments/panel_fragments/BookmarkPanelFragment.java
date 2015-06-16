package com.geospatialcorporation.android.geomobile.ui.fragments.panel_fragments;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.geospatialcorporation.android.geomobile.R;
import com.geospatialcorporation.android.geomobile.library.helpers.MapStateManager;
import com.geospatialcorporation.android.geomobile.library.helpers.panelmanager.SlidingPanelManager;
import com.geospatialcorporation.android.geomobile.ui.fragments.GeoViewFragmentBase;
import com.google.android.gms.maps.GoogleMap;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by andre on 6/6/2015.
 */
public class BookmarkPanelFragment extends GeoViewFragmentBase {
    View mView;
    @InjectView(R.id.bookMarkNameInput)EditText mName;

    //region Getters && Setters
    public GoogleMap getMap() {
        return mMap;
    }

    public void setMap(GoogleMap map) {
        mMap = map;
    }

    public SlidingPanelManager getPanelManager() {
        return mPanelManager;
    }

    public void setPanelManager(SlidingPanelManager panelManager) {
        mPanelManager = panelManager;
    }
    //endregion

    GoogleMap mMap;
    SlidingPanelManager mPanelManager;

    @OnClick(R.id.close)
    public void close(){
        mPanelManager.hide();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        mView = inflater.inflate(R.layout.fragment_panel_bookmarkmode, container, false);
        ButterKnife.inject(this, mView);
        mPanelManager = new SlidingPanelManager(getActivity());

        SetTitle(R.string.add_bookmark);

        mName.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    MapStateManager msm = new MapStateManager(getActivity());

                    msm.saveMapStateForBookMark(mMap, mName.getText().toString());

                    mPanelManager.hide();

                    Toaster("Bookmark saved");
                }

                return false;
            }
        });

        return mView;
    }

    @Override
    public void onDestroyView(){
        super.onDestroyView();
        SetTitle(R.string.app_name);
    }

    public static class Builder {
        GoogleMap mGoogleMap;

        public Builder(GoogleMap map){
            mGoogleMap = map;
        }

        public BookmarkPanelFragment create(){
            BookmarkPanelFragment result = new BookmarkPanelFragment();
            result.mMap = mGoogleMap;
            return result;
        }

    }
}
