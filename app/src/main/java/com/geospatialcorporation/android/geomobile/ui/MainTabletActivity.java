package com.geospatialcorporation.android.geomobile.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.geospatialcorporation.android.geomobile.R;
import com.geospatialcorporation.android.geomobile.application;
import com.geospatialcorporation.android.geomobile.ui.Interfaces.IGeoMainActivity;
import com.geospatialcorporation.android.geomobile.ui.fragments.MapFragments.TabletMapFragment;
import com.geospatialcorporation.android.geomobile.ui.fragments.tree_fragments.tablet.*;

public class MainTabletActivity extends GeoUndergroundMainActivity implements IGeoMainActivity{

    int mInfoFrameCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        application.setIsTablet(true);
        application.setGeoMainActivity(this);
        application.setShouldSetAppState(true);

        setMapFragment();

        mInfoFrameCode = 0;

        getSupportActionBar().setTitle(getResources().getString(R.string.geounderground));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        if(application.getIsAdminUser()) {
            inflater.inflate(R.menu.map_menu_admin, menu);
        } else {
            inflater.inflate(R.menu.map_menu, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_show_layers:
                showLayers();
                return true;
            case R.id.action_account:
                showAccount();
                return true;
            case R.id.action_library:
                showLibrary();
                return true;
            case R.id.action_subscriptions:
                startActivity(new Intent(this, SubscriptionSelectorActivity.class));
                finish();
                return true;
            case R.id.action_logout:
                application.Logout();
                startActivity(new Intent(this, LoginActivity.class));
                finish();
                return true;
            default:
                return true;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.info_frame);
        fragment.onActivityResult(requestCode, resultCode, data);
    }

    public void closeInfoFragment(){
        mInfoFrameCode = 0;
        mInfoFrame.setVisibility(View.GONE);
    }

    //region Actions
    protected void showLibrary() {
        showFragment(InfoFrame.Library, new TabLibraryFragment());
    }

    protected void showAccount() {
        showFragment(InfoFrame.Account, new TabAccountFragment());
    }

    public void showLayers() {
        showFragment(InfoFrame.Layer, new TabLayerFragment());
    }

    protected void showFragment(int frameCode, Fragment fragment){
        boolean isShowingNegative = showHideInfoFrame(frameCode);

        if(isShowingNegative){
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.info_frame, fragment)
                    .addToBackStack(null).commit();
        }
    }
    //endregion

    //region Helpers
    protected void setMapFragment() {
        Fragment contentFragment = new TabletMapFragment();

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.content_frame, contentFragment)
                .addToBackStack(null).commit();
    }

    protected boolean showHideInfoFrame(int infoFrame) {

        if(mInfoFrame.getVisibility() == View.VISIBLE && infoFrame == mInfoFrameCode) {
            closeInfoFragment();
            return false;
        } else {
            mInfoFrameCode = infoFrame;
            mInfoFrame.setVisibility(View.VISIBLE);
            return true;
        }
    }

    @Override
    public int getLayoutResource() {
        return R.layout.activity_main_tablet;
    }

    public Fragment getContentFragment() {
        return getSupportFragmentManager().findFragmentById(R.id.info_frame);
    }

    public void featureWindow() {
        mInfoFrame.setVisibility(View.VISIBLE);
        mInfoFrameCode = InfoFrame.FeatureWindow;
    }

    //endregion

    protected class InfoFrame {
        public static final int Library = 1;
        public static final int Layer = 2;
        public static final int Account = 3;
        public static final int FeatureWindow = 4;
    }
}
