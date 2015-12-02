/*
 * Copyright 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.geospatialcorporation.android.geomobile.ui;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.geospatialcorporation.android.geomobile.R;
import com.geospatialcorporation.android.geomobile.application;
import com.geospatialcorporation.android.geomobile.library.DI.Analytics.Interfaces.IGeoAnalytics;
import com.geospatialcorporation.android.geomobile.library.DI.Analytics.Models.GoogleAnalyticEvent;
import com.geospatialcorporation.android.geomobile.library.DI.ErrorHandler.Interfaces.IGeoErrorHandler;
import com.geospatialcorporation.android.geomobile.library.DI.MainNavigationController.DaggerMainNavCtrlComponent;
import com.geospatialcorporation.android.geomobile.library.DI.MainNavigationController.Implementations.MainNavCtrl;
import com.geospatialcorporation.android.geomobile.library.DI.MainNavigationController.MainNavCtrlComponent;
import com.geospatialcorporation.android.geomobile.library.DI.UIHelpers.Interfaces.DialogHelpers.IGeneralDialog;
import com.geospatialcorporation.android.geomobile.models.Subscription;
import com.geospatialcorporation.android.geomobile.ui.Interfaces.IFragmentView;
import com.geospatialcorporation.android.geomobile.ui.Interfaces.IGeoMainActivity;
import com.geospatialcorporation.android.geomobile.ui.Interfaces.OnFragmentInteractionListener;
import com.geospatialcorporation.android.geomobile.ui.fragments.GoogleMapFragment;
import com.geospatialcorporation.android.geomobile.ui.fragments.drawer.LayerSelectorDrawerFragment;
import com.geospatialcorporation.android.geomobile.ui.fragments.drawer.MainNavigationDrawerFragment;
import com.geospatialcorporation.android.geomobile.ui.fragments.tree_fragments.AccountFragment;
import com.geospatialcorporation.android.geomobile.ui.fragments.tree_fragments.LayerFragment;
import com.geospatialcorporation.android.geomobile.ui.fragments.tree_fragments.LibraryFragment;
import com.google.android.gms.maps.GoogleMap;

import butterknife.ButterKnife;

/**
 * This example illustrates a common usage of the DrawerLayout widget
 * in the Android support library.
 * <p/>
 * <p>When a navigation (left) drawer is present, the host activity should detect presses of
 * the action bar's Up affordance as a signal to open and close the navigation drawer. The
 * ActionBarDrawerToggle facilitates this behavior.
 * Items within the drawer should fall into one of two categories:</p>
 * <p/>
 * <ul>
 * <li><strong>View switches</strong>. A view switch follows the same basic policies as
 * list or tab navigation in that a view switch does not create navigation history.
 * This pattern should only be used at the root activity of a task, leaving some form
 * of Up navigation active for activities further down the navigation hierarchy.</li>
 * <li><strong>Selective Up</strong>. The drawer allows the user to choose an alternate
 * parent for Up navigation. This allows a user to jump across an app's navigation
 * hierarchy at will. The application should treat this as it treats Up navigation from
 * a different task, replacing the current task stack using TaskStackBuilder or similar.
 * This is the only form of navigation drawer that should be used outside of the root
 * activity of a task.</li>
 * </ul>
 * <p/>
 * <p>Right side drawers should be used for actions, not navigation. This follows the pattern
 * established by the Action Bar that navigation should be to the left and actions to the right.
 * An action should be an operation performed on the current contents of the window,
 * for example enabling or disabling a data overlay on top of the current content.</p>
 */
public class MainActivity extends ActionBarActivity
        implements MainNavigationDrawerFragment.NavigationDrawerCallbacks,
        OnFragmentInteractionListener, IGeoMainActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final String CURRENT_FRAGMENT = "current_fragment";

    //region Properties
    private GoogleMapFragment mMapFragment;
    GoogleMap mMap;
    private boolean mIsAdmin;
    MainNavigationDrawerFragment mMainMainNavigationDrawerFragment;
    LayerSelectorDrawerFragment mLayerDrawerFragment;
    MainNavCtrl mMainNavCtrl;
    DrawerLayout mDrawerLayout;
    IGeoErrorHandler mErrorHandler;
    IGeoAnalytics mAnalytics;
    int mBackPressedFromGoogleMapFragmentCount;
    Boolean mIsLandscape;
    FrameLayout mDetailFrame;
    String mCurrentFragment;
    //region Landscape Views
    ImageView mMapIV;
    ImageView mLayersIV;
    ImageView mLibrayIV;
    ImageView mAccountIV;
    ImageView mSubscriptionsIV;
    ImageView mLogoutIV;
    //endregion
    //endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        determineOrientation();

        application.setIsTablet(false);
        application.setIsLandscape(mIsLandscape);
        application.setGeoMainActivity(this);
        Subscription subscription = application.getGeoSubscription();

        //region Common Btw Tablet & Phone
        ButterKnife.bind(this);
        application.setMainActivity(this);
        ActionBar ab = getSupportActionBar();

        if(ab != null){
            ab.hide();
        }

        mBackPressedFromGoogleMapFragmentCount = 0;

        mMapFragment = application.getMapFragment();
        mAnalytics = application.getAnalyticsComponent().provideGeoAnalytics();
        application.getStatusBarManager().reset();

        mErrorHandler = application.getErrorsComponent().provideErrorHandler();
        Thread.setDefaultUncaughtExceptionHandler(mErrorHandler.UncaughtExceptionHandler(this));

        mIsAdmin = application.getIsAdminUser();
        //endregion

        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);

        if (!mIsLandscape) {
            mMainMainNavigationDrawerFragment = (MainNavigationDrawerFragment) getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
            mMainMainNavigationDrawerFragment.setUp(R.id.navigation_drawer, mDrawerLayout, getSupportActionBar());
        }

        mLayerDrawerFragment = (LayerSelectorDrawerFragment)getSupportFragmentManager().findFragmentById(R.id.layer_drawer);
        mLayerDrawerFragment.setUp(R.id.layer_drawer, mDrawerLayout, new Toolbar(this), mMainMainNavigationDrawerFragment);

        if(mIsLandscape) {
            setupLandscapeUI();
        }
        //determineScreenSize();
    }

    private void setupLandscapeUI() {
        mDetailFrame = (FrameLayout)findViewById(R.id.detail_frame);

        mMapIV = (ImageView)findViewById(R.id.mapIV);
        mLayersIV = (ImageView)findViewById(R.id.layersIV);
        mLibrayIV = (ImageView)findViewById(R.id.libraryIV);
        mAccountIV = (ImageView)findViewById(R.id.accountIV);
        mSubscriptionsIV = (ImageView)findViewById(R.id.SubscriptionsIV);
        mLogoutIV = (ImageView)findViewById(R.id.logoutIV);

        if(mIsAdmin){
            mSubscriptionsIV.setVisibility(View.VISIBLE);
        }

        FragmentManager fragmentManager = getSupportFragmentManager();

        fragmentManager.beginTransaction()
                .replace(R.id.content_frame, mMapFragment)
                .addToBackStack(null).commit();

        mMapIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDetailFrame.setVisibility(View.GONE);
            }
        });

        mLayersIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openLayersDrawer();
            }
        });

        mLogoutIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                application.Logout();
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                finish();
            }
        });

        mSubscriptionsIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, SubscriptionSelectorActivity.class));
                finish();
            }
        });

        showDetailFrame(mLibrayIV, new LibraryFragment());
        showDetailFrame(mAccountIV, new AccountFragment());

    }

    public void setFragmentFrame(IFragmentView fragment, View controller){
        if (mCurrentFragment.equals(fragment.getClass().getSimpleName())) {
            if(mIsLandscape) {
                showDetailFragment();

                fragment.setDetailFrame(controller, getSupportFragmentManager());
            } else {
                fragment.setContentFragment(getSupportFragmentManager());
            }
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        if(savedInstanceState != null){
            mCurrentFragment = savedInstanceState.getString(CURRENT_FRAGMENT);

            if(mCurrentFragment != null){
                setFragmentFrame(new AccountFragment(), mAccountIV);
                setFragmentFrame(new LibraryFragment(), mLibrayIV);
                setFragmentFrame(new LayerFragment(), null);
            }
        }

        mCurrentFragment = null;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if(mIsLandscape){
            if(mDetailFrame.getVisibility() == View.VISIBLE) {
                Fragment detailFragment = getDetailFragment();

                if(detailFragment != null){
                    mCurrentFragment = detailFragment.getClass().getSimpleName();

                    outState.putString(CURRENT_FRAGMENT, mCurrentFragment);
                }
            }
        } else {
            outState.putString(CURRENT_FRAGMENT , getContentFragment().getClass().getSimpleName());
        }
        super.onSaveInstanceState(outState);
    }

    private void showDetailFrame(ImageView control, final Fragment fragment) {
        control.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment currentFragment = getDetailFragment();

                if (currentFragment != null) {
                    if (fragment.getClass().getSimpleName().equals(currentFragment.getClass().getSimpleName()) && mDetailFrame.getVisibility() == View.VISIBLE) {
                        //If the fragment is Visible and already the target fragment just hide the frame
                        mDetailFrame.setVisibility(View.GONE);
                    } else {
                        setDetailFragment();
                    }
                } else {
                    setDetailFragment();
                }
            }

            protected void setDetailFragment() {
                mDetailFrame.setVisibility(View.VISIBLE);

                FragmentManager fragmentManager = getSupportFragmentManager();

                fragmentManager.beginTransaction()
                        .replace(R.id.detail_frame, fragment)
                        .addToBackStack(null).commit();
            }
        });
    }

    public void determineOrientation(){
        int display_mode = getResources().getConfiguration().orientation;

        if(display_mode == Configuration.ORIENTATION_LANDSCAPE){
            mIsLandscape = true;
        } else {
            mIsLandscape = false;
        }

    }

    public void determineScreenSize() {
        float density = getResources().getDisplayMetrics().density;

        Toast.makeText(this, "Density: " + density, Toast.LENGTH_LONG).show();

        int screenSize = getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK;
        switch (screenSize) {
            case Configuration.SCREENLAYOUT_SIZE_XLARGE:
                Toast.makeText(this, "XLarge screen", Toast.LENGTH_LONG).show();
                break;
            case Configuration.SCREENLAYOUT_SIZE_LARGE:
                Toast.makeText(this, "Large screen", Toast.LENGTH_LONG).show();
                break;
            case Configuration.SCREENLAYOUT_SIZE_NORMAL:
                Toast.makeText(this, "Normal screen", Toast.LENGTH_LONG).show();
                break;
            case Configuration.SCREENLAYOUT_SIZE_SMALL:
                Toast.makeText(this, "Small screen", Toast.LENGTH_LONG).show();
                break;
            default:
                Toast.makeText(this,
                        "Screen size is neither large, normal or small",
                        Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mAnalytics.onStop(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.content_frame);
        fragment.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            View layerView = getLayerListView();

            if(mDrawerLayout.isDrawerOpen(layerView)){
                mDrawerLayout.closeDrawer(layerView);
            }
        }

        return super.onOptionsItemSelected(item);
    }

    /* Called whenever we call invalidateOptionsMenu() */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void onBackPressed() {

        if(getContentFragment().getClass().getSimpleName().equals(GoogleMapFragment.class.getSimpleName())){
            IGeneralDialog dialog = application.getUIHelperComponent().provideGeneralDialog();

            if(mIsAdmin) {
                dialog.Subscriptions(this, getSupportFragmentManager());
            } else {
                dialog.Logout(this, getSupportFragmentManager());
            }
        } else {
            super.onBackPressed();
        }

    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        Fragment pageFragment = setContentFragment(position);

        if (pageFragment == null) return;

        FragmentManager fragmentManager = getSupportFragmentManager();

        fragmentManager.beginTransaction()
                .replace(R.id.content_frame, pageFragment)
                .addToBackStack(null).commit();
    }

    protected Fragment setContentFragment(int position) {
        mIsAdmin = application.getIsAdminUser();
        application.setMainActivity(this);


        MainNavCtrlComponent component = DaggerMainNavCtrlComponent.builder().build();
        mMainNavCtrl = component.provideMainNavCtrl();

        if(mIsAdmin) {
            return mMainNavCtrl.setAdminView(this, mMap, mMapFragment, position);
        } else {
            return mMainNavCtrl.setStandardView(mMap, mMapFragment, position);
        }
    }

    public Fragment getContentFragment(){
        return getSupportFragmentManager().findFragmentById(R.id.content_frame);
    }

    public void openLayersDrawer(){
        mAnalytics.trackClick(new GoogleAnalyticEvent().OpenLayerDrawer());

        View layerView = getLayerListView();
        View mainView = getMainNavView();

        showDrawer(layerView, mainView);
    }

    public void openNavigationDrawer(){

        View layerView = getLayerListView();
        View mainView = getMainNavView();

        mAnalytics.trackClick(new GoogleAnalyticEvent().OpenMainNav());

        showDrawer(mainView, layerView);
    }

    protected void showDrawer(View toShow, View toClose){
        DrawerLayout drawerLayout = getDrawerLayout();

        if(drawerLayout.isDrawerOpen(toShow)){
            drawerLayout.closeDrawer(toShow);
        } else {
            drawerLayout.openDrawer(toShow);
        }

        if(toClose != null) {  //null in landscape
            if (drawerLayout.isDrawerOpen(toClose)) {
                drawerLayout.closeDrawer(toClose);
            }
        }

    }

    @Override
    public void onFragmentInteraction(String title) {
        getSupportActionBar().setTitle(title);
    }

    @Override
    public void closeDetailFragment() {
        FrameLayout frameLayout = (FrameLayout)findViewById(R.id.detail_frame);

        if(frameLayout != null){
            frameLayout.setVisibility(View.GONE);
        }
    }

    @Override
    public void showDetailFragment() {
        mDetailFrame.setVisibility(View.VISIBLE);
    }

    public DrawerLayout getDrawerLayout() {
        return (DrawerLayout)findViewById(R.id.drawer_layout);
    }

    public LayerSelectorDrawerFragment getLayerDrawerFragment(){
        return (LayerSelectorDrawerFragment)getSupportFragmentManager().findFragmentById(R.id.layer_drawer);
    }

    public void closeLayerDrawer() {
        getDrawerLayout().closeDrawer(Gravity.RIGHT);
    }

    public void closeNavDrawer() {
        getDrawerLayout().closeDrawer(Gravity.LEFT);
    }

    public View getLayerListView() {
        return findViewById(R.id.layer_drawer);
    }

    public View getMainNavView(){
        return findViewById(R.id.navigation_drawer);
    }

    public Fragment getDetailFragment() {
        return getSupportFragmentManager().findFragmentById(R.id.detail_frame);
    }

    public static class MediaConstants {
        public static final int PICK_FILE_REQUEST = 0;
        public static final int PICK_IMAGE_REQUEST = 1;
        public static final int TAKE_IMAGE_REQUEST = 2;
        public static final int MEDIA_TYPE_IMAGE = 4;
        public static final int MEDIA_TYPE_VIDEO = 5;

        public static final int PICK_FILE_REQUEST_FEATUREWINDOW = 7;
        public static final int TAKE_IMAGE_REQUEST_FEATUREWINDOW = 8;
        public static final int PICK_IMAGE_REQUEST_FEATUREWINDOW = 9;
    }
}