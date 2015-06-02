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

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.geospatialcorporation.android.geomobile.R;
import com.geospatialcorporation.android.geomobile.application;
import com.geospatialcorporation.android.geomobile.library.constants.ViewConstants;
import com.geospatialcorporation.android.geomobile.library.util.Dialogs;
import com.geospatialcorporation.android.geomobile.models.Layers.Layer;
import com.geospatialcorporation.android.geomobile.ui.fragments.AccountFragment;
import com.geospatialcorporation.android.geomobile.ui.fragments.DocumentFragment;
import com.geospatialcorporation.android.geomobile.ui.fragments.GoogleMapFragment;
import com.geospatialcorporation.android.geomobile.ui.fragments.LayerFragment;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

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
public class MainActivity extends Activity {
    private static final String TAG = MainActivity.class.getSimpleName();

    //region Properties
    @InjectView(R.id.drawer_layout)DrawerLayout mDrawerLayout;
    @InjectView(R.id.left_drawer)ListView mLeftDrawerList;
    @InjectView(R.id.right_drawer)ListView mRightDrawerList;
    View mHeaderView;
    View mFooterView;
    private ActionBarDrawerToggle mDrawerToggle;
    private ActionBar mActionBar;
    private GoogleMapFragment mMap;
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    private List<String> mViewTitles;
    private Dialogs dialog;
    private boolean mIsAdmin;
    //endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);

        mActionBar = getActionBar();

        dialog = new Dialogs();

        mMap = application.getMapFragment();
        mIsAdmin = application.getIsAdminUser();

        mTitle = mDrawerTitle = getTitle();

        mHeaderView = View.inflate(this, R.layout.drawer_listview_header, null);
        mFooterView = View.inflate(this, R.layout.drawer_listview_footer, null);

        TextView mClientName = (TextView) mHeaderView.findViewById(R.id.headerClientName);
        mClientName.setText(application.getGeoClient().getName());

        mLeftDrawerList.addHeaderView(mHeaderView);
        mLeftDrawerList.addFooterView(mFooterView);

        if(mIsAdmin){
            mViewTitles = Arrays.asList(new String[]{MenuConstants.MAP, MenuConstants.LAYERS, MenuConstants.LIBRARY, MenuConstants.ADMINCLITENTS});
        } else {
            mViewTitles = Arrays.asList(new String[]{MenuConstants.MAP, MenuConstants.LAYERS, MenuConstants.LIBRARY});
        }

        // set a custom shadow that overlays the main content when the drawer opens
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        // set up the drawer's list view with items and click listener
        mLeftDrawerList.setAdapter(new MainNavigationAdapter(this, mViewTitles));

        List<Layer> fakeLayers = new ArrayList<>();
        fakeLayers.add(new Layer("First Layer"));
        //TODO: Get a List of Layers
        mRightDrawerList.setAdapter(new LayerAdapter(this,
                R.layout.drawer_right_list_item, fakeLayers));

        mLeftDrawerList.setOnItemClickListener(new LeftDrawerItemClickListener());
        mRightDrawerList.setOnItemClickListener(new RightDrawerItemClickListener());

        // enable ActionBar app icon to behave as action to toggle nav drawer
        mActionBar.setDisplayHomeAsUpEnabled(true);
        mActionBar.setHomeButtonEnabled(true);

        // ActionBarDrawerToggle ties together the the proper interactions
        // between the sliding drawer and the action bar app icon
        mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                mDrawerLayout,         /* DrawerLayout object */
                R.string.drawer_open,  /* "open drawer" description for accessibility */
                R.string.drawer_close  /* "close drawer" description for accessibility */
        ) {
            public void onDrawerClosed(View view) {
                mActionBar.setTitle(mTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
                mDrawerToggle.syncState();
            }

            public void onDrawerOpened(View drawerView) {
                mActionBar.setTitle(mDrawerTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
                toggleDrawers((ListView) drawerView);
                mDrawerToggle.syncState();
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        if (savedInstanceState == null) {
            selectItem(1);
        }
    }

    private void toggleDrawers(ListView drawer) {
        if (drawer == mLeftDrawerList) {
            if (mDrawerLayout.isDrawerOpen(mRightDrawerList)) {
                mDrawerLayout.closeDrawer(mRightDrawerList);
            }
        } else {
            if (mDrawerLayout.isDrawerOpen(mLeftDrawerList)) {
                mDrawerLayout.closeDrawer(mLeftDrawerList);
            }
        }
    }

    public AbstractMap.SimpleEntry<DrawerLayout, ListView> getRightDrawer() {
        return new AbstractMap.SimpleEntry<>(mDrawerLayout, mRightDrawerList);
    }

    protected void onResume() {
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /* Called whenever we call invalidateOptionsMenu() */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // If the nav drawer is open, hide action items related to the content view
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mLeftDrawerList);
        menu.findItem(R.id.action_websearch).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // The action bar home/up action should open or close the drawer.
        // ActionBarDrawerToggle will take care of this.
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle action buttons
        switch (item.getItemId()) {
            case R.id.action_websearch:
                // create intent to perform web search for this planet
                Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
                intent.putExtra(SearchManager.QUERY, mActionBar.getTitle());
                // catch event that there's no activity to handle intent
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                } else {
                    Toast.makeText(this, R.string.app_not_available, Toast.LENGTH_LONG).show();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /* The click listener for ListView in the navigation drawer */
    private class LeftDrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }
    }

    private class RightDrawerItemClickListener implements ListView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectLayerItem(position);
        }

        private void selectLayerItem(int position) {
        }
    }

    private class LayerAdapter extends ArrayAdapter<Layer> {

        private List<Layer> mLayers;

        public LayerAdapter(Context context, int resource, List<Layer> layers) {
            super(context, resource, layers);
            mLayers = layers;
        }

        private class ViewHolder {
            TextView layerName;
            CheckBox isVisible;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder holder = null;

            if (convertView == null) {
                LayoutInflater vi = (LayoutInflater) getSystemService(
                        Context.LAYOUT_INFLATER_SERVICE);
                convertView = vi.inflate(R.layout.drawer_right_list_item, null);

                holder = new ViewHolder();
                holder.layerName = (TextView) convertView.findViewById(R.id.layerNameTV);
                holder.isVisible = (CheckBox) convertView.findViewById(R.id.isVisible);
                convertView.setTag(holder);

                convertView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CheckBox cb = (CheckBox) v.findViewById(R.id.isVisible);
                        if(cb.isChecked()){
                            cb.setChecked(false);
                        } else {
                            cb.setChecked(true);
                        }
                    }
                });
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            Layer layer = mLayers.get(position);
            holder.layerName.setText(layer.getName());
            holder.isVisible.setChecked(layer.getIsShowing());

            return convertView;

        }

    }

    private class MainNavigationAdapter extends ArrayAdapter<String>{
        List<String> mMenuItems;

        public MainNavigationAdapter(Context context, List<String> menuItems) {
            super(context, R.layout.drawer_left_list_item, menuItems);
            mMenuItems = menuItems;
        }

        @Override
        public View getView(int position, View coverView, ViewGroup parent) {
            // TODO Auto-generated method stub

            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View v = inflater.inflate(R.layout.drawer_left_list_item, null);

            TextView name = (TextView)v.findViewById(R.id.menuItemTV);
            ImageView icon = (ImageView)v.findViewById(R.id.menuItemIcon);

            name.setText(mMenuItems.get(position));

            switch (mMenuItems.get(position)){
                case MenuConstants.MAP:
                    icon.setImageDrawable(getDrawable(R.drawable.ic_map_marker_white_18dp));
                    break;
                case MenuConstants.LAYERS:
                    icon.setImageDrawable(getDrawable(R.drawable.ic_layers_white_18dp));
                    break;
                case MenuConstants.LIBRARY:
                    icon.setImageDrawable(getDrawable(R.drawable.ic_book_white_18dp));
                    break;
                case MenuConstants.ADMINCLITENTS:
                    icon.setImageDrawable(getDrawable(R.drawable.ic_account_search_white_18dp));
                    break;
                default:
                    break;
            }

            return v;

        }

    }

    private void selectItem(int position) {
        Fragment fragment = setPageFragment(position);

        FragmentManager fragmentManager = getFragmentManager();

        fragmentManager.beginTransaction()
                .replace(R.id.content_frame, fragment)
                .commit();

        // update selected item and title, then close the drawer
        mLeftDrawerList.setItemChecked(position, true);
        mDrawerLayout.closeDrawer(mLeftDrawerList);
    }

    protected Fragment setPageFragment(int position) {
        if(mIsAdmin) {
            switch (position) {
                case ViewConstants.MAP:
                    return mMap;
                case ViewConstants.LAYER:
                    return new LayerFragment();
                case ViewConstants.LIBRARY:
                    return new DocumentFragment();
                case ViewConstants.ACCOUNT:
                    return new AccountFragment();
                case ViewConstants.LOGOUT_ADMIN:
                    application.Logout();
                    startActivity(new Intent(this, LoginActivity.class));
                    break;
                case ViewConstants.ADMINCLIENTS:
                    startActivity(new Intent(this, ClientSelectorActivity.class));
                    finish();
                    break;
                default:
                    Toast.makeText(application.getAppContext(), "Drawer view not yet implemented.", Toast.LENGTH_LONG).show();
                    break;
            }
        } else {
            switch (position) {
                case ViewConstants.MAP:
                    return mMap;
                case ViewConstants.LAYER:
                    return new LayerFragment();
                case ViewConstants.LIBRARY:
                    return new DocumentFragment();
                case ViewConstants.ACCOUNT:
                    return new AccountFragment();
                case ViewConstants.LOGOUT_REGULAR:
                    application.Logout();
                    startActivity(new Intent(this, LoginActivity.class));
                    break;
                default:
                    Toast.makeText(application.getAppContext(), "Drawer view not yet implemented.", Toast.LENGTH_LONG).show();
                    break;
            }
        }

        return mMap;
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        mActionBar.setTitle(mTitle);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    public void treeItem(View view) {
        dialog.message("tree item clicked", this);
    }

    private class MenuConstants {
        public static final String MAP = "Map";
        public static final String LAYERS = "Layers";
        public static final String LIBRARY = "Library";
        public static final String ADMINCLITENTS = "Admin Clients";

    }
}