package com.geospatialcorporation.android.geomobile.ui.fragments.panel_fragments;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.geospatialcorporation.android.geomobile.R;
import com.geospatialcorporation.android.geomobile.database.DataRepository.IDataRepository;
import com.geospatialcorporation.android.geomobile.database.DataRepository.Implementations.Bookmark.BookmarkAppSource;
import com.geospatialcorporation.android.geomobile.library.helpers.MapStateManager;
import com.geospatialcorporation.android.geomobile.models.Bookmarks.Bookmark;
import com.geospatialcorporation.android.geomobile.ui.fragments.GeoViewFragmentBase;
import com.google.android.gms.maps.GoogleMap;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by andre on 6/6/2015.
 */
public class BookmarkFragment extends GeoViewFragmentBase {
    View mView;
    @InjectView(R.id.bookMarkNameInput)EditText mName;

    //region Getters && Setters
    public SlidingUpPanelLayout getPanel() {
        return mPanel;
    }

    public void setPanel(SlidingUpPanelLayout panel) {
        mPanel = panel;
    }

    public GoogleMap getMap() {
        return mMap;
    }

    public void setMap(GoogleMap map) {
        mMap = map;
    }
    //endregion

    SlidingUpPanelLayout mPanel;
    GoogleMap mMap;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        mView = inflater.inflate(R.layout.fragment_panel_bookmarkmode, container, false);
        ButterKnife.inject(this, mView);

        SetTitle(R.string.add_bookmark);

        mName.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_DONE){
                    MapStateManager msm = new MapStateManager(getActivity());
                    msm.saveMapStateForBookMark(mMap, mName.getText().toString());

                    mPanel.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);
                    Toaster("Bookmark saved");

                    IDataRepository<Bookmark> repo = new BookmarkAppSource();

                    Toaster(repo.getAll().size() + " bookmarks in cache");
                }

                return false;
            }
        });

        return mView;
    }


    @Override
    public void onDetach(){
        super.onDetach();
        SetTitle(R.string.app_name);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        SetTitle(R.string.app_name);
    }

    public static class Builder {
        GoogleMap mGoogleMap;
        SlidingUpPanelLayout mSlidingUpPanelLayout;

        public Builder(GoogleMap map, SlidingUpPanelLayout panel){
            mGoogleMap = map;
            mSlidingUpPanelLayout = panel;
        }

        public BookmarkFragment create(){
            BookmarkFragment result = new BookmarkFragment();
            result.mPanel = mSlidingUpPanelLayout;
            result.mMap = mGoogleMap;
            return result;
        }

    }
}
