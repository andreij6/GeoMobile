package com.geospatialcorporation.android.geomobile.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.geospatialcorporation.android.geomobile.R;
import com.geospatialcorporation.android.geomobile.application;
import com.geospatialcorporation.android.geomobile.library.DI.UIHelpers.Interfaces.DialogHelpers.IGeneralDialog;
import com.geospatialcorporation.android.geomobile.ui.Interfaces.IGeoMainActivity;
import com.geospatialcorporation.android.geomobile.ui.fragments.GoogleMapFragment;
import com.geospatialcorporation.android.geomobile.ui.fragments.MapFragments.TabletMapFragment;
import com.geospatialcorporation.android.geomobile.ui.fragments.panel_fragments.map_fragment_panels.TabletFeatureWindowPanelFragment;
import com.geospatialcorporation.android.geomobile.ui.fragments.tree_fragments.tablet.*;

import butterknife.Bind;
import butterknife.OnClick;

public class MainTabletActivity extends GeoUndergroundMainActivity implements IGeoMainActivity{

    int mInfoFrameCode;

    @Bind(R.id.subscriptionsIV) ImageView mSubscriptions;

    @OnClick(R.id.showLayersIV)
    public void showLayersClick(){
        if(application.getMapStateLoaded()){
            showLayers();
        }
    }

    @OnClick(R.id.libraryIV)
    public void showLibraryClick(){
        if(application.getMapStateLoaded()){
            showLibrary();
        }
    }

    @OnClick(R.id.accountIV)
    public void showAccountClick(){
        if(application.getMapStateLoaded()){
            showAccount();
        }
    }

    @OnClick(R.id.subscriptionsIV)
    public void showSubcriptionsIV(){
        if(application.getMapStateLoaded()){
            startActivity(new Intent(this, SubscriptionSelectorActivity.class));
            finish();
        }
    }

    @OnClick(R.id.logoutIV)
    public void logoutClick(){
        if(application.getMapStateLoaded()){
            application.Logout();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        application.setIsTablet(true);
        application.setGeoMainActivity(this);
        application.setShouldSetAppState(true);
        application.setMainTabletActivity(this);

        setMapFragment();

        mInfoFrameCode = 0;

        getSupportActionBar().hide();

        if(!application.getIsAdminUser()) {
            mSubscriptions.setVisibility(View.GONE);
        }

    }

   //@Override
   //public boolean onCreateOptionsMenu(Menu menu) {
   //    MenuInflater inflater = getMenuInflater();
   //    if(application.getIsAdminUser()) {
   //        inflater.inflate(R.menu.map_menu_admin, menu);
   //    } else {
   //        inflater.inflate(R.menu.map_menu, menu);
   //    }
   //    return true;
   //}
    /*
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(application.getMapStateLoaded()) {
            switch (item.getItemId()) {
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
        } else {
            return true;
        }
    }
    */

    @Override
    public void onBackPressed() {
        if(mInfoFrame.getVisibility() == View.GONE){
            IGeneralDialog dialog = application.getUIHelperComponent().provideGeneralDialog();

            if(mIsAdmin) {
                dialog.Subscriptions(this, getSupportFragmentManager());
            } else {
                dialog.Logout(this, getSupportFragmentManager());
            }
        } else {

            super.onBackPressed();

            Fragment frag = getContentFragment();

            if(frag instanceof TabletFeatureWindowPanelFragment){
                closeInfoFragment();
            }

            if(frag == null){
                closeInfoFragment();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.info_frame);
        fragment.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mAnalytics.onStop(this);
    }

    public void closeInfoFragment(){
        mInfoFrameCode = 0;
        mInfoFrame.setVisibility(View.GONE);

        TabletMapFragment mapFragment = getMapFragment();

        mapFragment.clearHighlights();
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
        boolean isShowingNegative = shouldShowInfoFrame(frameCode);

        if(isShowingNegative){
            mInfoFrameCode = frameCode;
            mInfoFrame.setVisibility(View.VISIBLE);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.info_frame, fragment)
                    .addToBackStack(null).commit();
        } else {
            closeInfoFragment();
        }
    }
    //endregion

    //region Helpers
    protected void setMapFragment() {
        application.setMapStateLoaded(false);

        Fragment contentFragment = new TabletMapFragment();

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.content_frame, contentFragment)
                .addToBackStack(null).commit();
    }

    protected boolean shouldShowInfoFrame(int infoFrame){
        if(mInfoFrame.getVisibility() == View.VISIBLE && infoFrame == mInfoFrameCode) {
            return false;
        } else {
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

    public TabletMapFragment getMapFragment(){
        return (TabletMapFragment)getSupportFragmentManager().findFragmentById(R.id.content_frame);
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
