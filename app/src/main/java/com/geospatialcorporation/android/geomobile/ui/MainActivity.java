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
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;

import com.geospatialcorporation.android.geomobile.R;
import com.geospatialcorporation.android.geomobile.application;
import com.geospatialcorporation.android.geomobile.library.DI.ErrorHandler.Interfaces.IGeoErrorHandler;
import com.geospatialcorporation.android.geomobile.library.DI.MainNavigationController.DaggerMainNavCtrlComponent;
import com.geospatialcorporation.android.geomobile.library.DI.MainNavigationController.Implementations.MainNavCtrl;
import com.geospatialcorporation.android.geomobile.library.DI.MainNavigationController.MainNavCtrlComponent;
import com.geospatialcorporation.android.geomobile.ui.Interfaces.OnFragmentInteractionListener;
import com.geospatialcorporation.android.geomobile.ui.fragments.GoogleMapFragment;
import com.geospatialcorporation.android.geomobile.ui.fragments.drawer.LayerSelectorDrawerFragment;
import com.geospatialcorporation.android.geomobile.ui.fragments.drawer.MainNavigationDrawerFragment;
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
        OnFragmentInteractionListener {
    private static final String TAG = MainActivity.class.getSimpleName();

    //region Properties
    private GoogleMapFragment mMapFragment;
    GoogleMap mMap;
    private boolean mIsAdmin;
    MainNavigationDrawerFragment mMainMainNavigationDrawerFragment;
    LayerSelectorDrawerFragment mLayerDrawerFragement;
    MainNavCtrl mMainNavCtrl;
    IGeoErrorHandler mErrorHandler;
    //endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);
        application.setMainActivity(this);
        getSupportActionBar().setElevation(0);

        mMapFragment = application.getMapFragment();

        //mErrorHandler = application.getErrorsComponent().provideErrorHandler();
        //Thread.setDefaultUncaughtExceptionHandler(mErrorHandler.UncaughtExceptionHandler());

        mIsAdmin = application.getIsAdminUser();

        mMainMainNavigationDrawerFragment = (MainNavigationDrawerFragment)getSupportFragmentManager().findFragmentById(R.id.navigation_left_drawer);
        mMainMainNavigationDrawerFragment.setUp(R.id.navigation_left_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), getSupportActionBar());

        mLayerDrawerFragement = (LayerSelectorDrawerFragment)getSupportFragmentManager().findFragmentById(R.id.layer_drawer);
        mLayerDrawerFragement.setUp(R.id.layer_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), (Toolbar) findViewById(R.id.my_toolbar));


    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.content_frame);
        fragment.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return false;
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.main, menu);
//        return super.onCreateOptionsMenu(menu);
    }

    /* Called whenever we call invalidateOptionsMenu() */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        if(getContentFragment() == null){
            onNavigationDrawerItemSelected(0);
        }
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        Fragment pageFragment = setPageFragment(position);

        if (pageFragment == null) return;

        FragmentManager fragmentManager = getSupportFragmentManager();

        fragmentManager.beginTransaction()
                .replace(R.id.content_frame, pageFragment)
                .addToBackStack(null).commit();
    }

    protected Fragment setPageFragment(int position) {
        mIsAdmin = application.getIsAdminUser();
        application.setMainActivity(this);

        MainNavCtrlComponent component = DaggerMainNavCtrlComponent.builder().build();
        mMainNavCtrl = component.provideMainNavCtrl();

        if(mIsAdmin) {
            return mMainNavCtrl.setAdminView(this, mMap, mMapFragment, position);
        } else {
            return mMainNavCtrl.setStandardView(this, mMap, mMapFragment, position);
        }
    }

    public Fragment getContentFragment(){
        return getSupportFragmentManager().findFragmentById(R.id.content_frame);
    }

    @Override
    public void onFragmentInteraction(String title) {
        getSupportActionBar().setTitle(title);
    }

    public DrawerLayout getRightDrawer() {
        return (DrawerLayout)findViewById(R.id.drawer_layout);
    }

    public LayerSelectorDrawerFragment getLayerDrawerFragment(){
        return (LayerSelectorDrawerFragment)getSupportFragmentManager().findFragmentById(R.id.layer_drawer);
    }

    public View getLayerListView() {
        return findViewById(R.id.layer_drawer);
    }

    public static class MediaConstants {
        public static final int PICK_FILE_REQUEST = 0;
        public static final int PICK_IMAGE_REQUEST = 1;
        public static final int TAKE_IMAGE_REQUEST = 2;
        public static final int MEDIA_TYPE_IMAGE = 4;
        public static final int MEDIA_TYPE_VIDEO = 5;
    }
}