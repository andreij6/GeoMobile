package com.geospatialcorporation.android.geomobile.ui.fragments.drawer;

import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.geospatialcorporation.android.geomobile.R;
import com.geospatialcorporation.android.geomobile.application;
import com.geospatialcorporation.android.geomobile.library.DI.Analytics.Interfaces.IGeoAnalytics;
import com.geospatialcorporation.android.geomobile.library.DI.Tasks.Interfaces.IGetLayersTask;
import com.geospatialcorporation.android.geomobile.library.DI.Tasks.models.GetLayersTaskParams;
import com.geospatialcorporation.android.geomobile.library.sectionbuilders.implementations.LegendLayerSectionBuilder;
import com.geospatialcorporation.android.geomobile.models.Folders.Folder;
import com.geospatialcorporation.android.geomobile.ui.Interfaces.IPostExecuter;
import com.geospatialcorporation.android.geomobile.ui.MainActivity;

import java.lang.reflect.Field;
import java.util.List;

import butterknife.Bind;

/**
 * Fragment used for managing interactions for and presentation of a navigation drawer.
 * See the <a href="https://developer.android.com/design/patterns/navigation-drawer.html#Interaction">
 * design guidelines</a> for a complete explanation of the behaviors implemented here.
 */
public class LayerSelectorDrawerFragment extends Fragment implements IPostExecuter<List<Folder>> {

    private static final String STATE_SELECTED_POSITION = "selected_navigation_drawer_position";
    private static final String TAG = LayerSelectorDrawerFragment.class.getSimpleName();

    private static final String PREF_USER_LEARNED_DRAWER = "navigation_drawer_learned";

    private ActionBarDrawerToggle mDrawerToggle;

    private DrawerLayout mDrawerLayout;
    private ListView mDrawerListView;
    private View mFragmentContainerView;
    private View mRootView;
    @Bind(R.id.layerRecyclerView) RecyclerView mRecyclerView;
    IGeoAnalytics mAnalytics;
    IGetLayersTask mGetLayersTask;

    private int mCurrentSelectedPosition = 0;
    private boolean mFromSavedInstanceState;
    private boolean mUserLearnedDrawer;

    public LayerSelectorDrawerFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Read in the flag indicating whether or not the user has demonstrated awareness of the
        // drawer. See PREF_USER_LEARNED_DRAWER for details.
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
        mUserLearnedDrawer = sp.getBoolean(PREF_USER_LEARNED_DRAWER, false);
        mAnalytics = application.getAnalyticsComponent().provideGeoAnalytics();

        if (savedInstanceState != null) {
            mCurrentSelectedPosition = savedInstanceState.getInt(STATE_SELECTED_POSITION);
            mFromSavedInstanceState = true;
        }

        // Select either the default item (0) or the last selected item.
        selectItem(mCurrentSelectedPosition);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // Indicate that this fragment would like to influence the set of actions in the action bar.
        mRecyclerView = (RecyclerView)mRootView.findViewById(R.id.layerRecyclerView);

        refresh();

        setHasOptionsMenu(false);
    }

    protected IGetLayersTask setTask() {
        if(mGetLayersTask == null){
            return application.getTasksComponent().provideLayersTask();
        }

        return mGetLayersTask;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_right_navigation_drawer, container, false);

        return mRootView;
    }

    public boolean isDrawerOpen() {
        return mDrawerLayout != null && mDrawerLayout.isDrawerOpen(mFragmentContainerView);
    }

    public void setUp(int fragmentId, final DrawerLayout drawerLayout, Toolbar toolbar, final MainNavigationDrawerFragment mainDrawer) {
        mFragmentContainerView = getActivity().findViewById(fragmentId);
        mDrawerLayout = drawerLayout;

        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);

        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

        // ActionBarDrawerToggle ties together the the proper interactions
        // between the navigation drawer and the action bar app icon.
        mDrawerToggle = new ActionBarDrawerToggle(
                getActivity(),                    /* host Activity */
                mDrawerLayout,                    /* DrawerLayout object */
                toolbar,             /* nav drawer image to replace 'Up' caret */
                R.string.drawer_open,  /* "open drawer" description for accessibility */
                R.string.drawer_close  /* "close drawer" description for accessibility */
        ) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                if (!isAdded()) {
                    return;
                }

                getActivity().supportInvalidateOptionsMenu(); // calls onPrepareOptionsMenu()
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);

                MainActivity activity = (MainActivity)getActivity();

                if (!isAdded()) {
                    return;
                }

                if (!mUserLearnedDrawer) {
                    // The user manually opened the drawer; store this flag to prevent auto-showing
                    // the navigation drawer automatically in the future.
                    mUserLearnedDrawer = true;
                    SharedPreferences sp = PreferenceManager
                            .getDefaultSharedPreferences(getActivity());
                    sp.edit().putBoolean(PREF_USER_LEARNED_DRAWER, true).apply();
                }

                activity.supportInvalidateOptionsMenu(); // calls onPrepareOptionsMenu()

                try {
                    String name = getIDName(drawerView, R.id.class);

                    MainActivity mainActivity = (MainActivity)getActivity();

                    if(name.equals("layer_drawer")){
                        mainActivity.closeNavDrawer();
                    } else {
                        mainActivity.closeLayerDrawer();
                    }
                } catch (Exception e) {
                    e.printStackTrace();

                    mAnalytics.sendException(e);
                }

            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                if(mainDrawer != null) {
                    mainDrawer.getCurretSelectionByContentFragment();
                }

                super.onDrawerSlide(drawerView, slideOffset);
            }
        };

        if (!mUserLearnedDrawer && !mFromSavedInstanceState) {
            //mDrawerLayout.openDrawer(mFragmentContainerView);
        }

        // Defer code dependent on restoration of previous instance state.
        mDrawerLayout.post(new Runnable() {
            @Override
            public void run() {
                mDrawerToggle.syncState();
            }
        });

        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }

    public static String getIDName(View view, Class<?> clazz) throws Exception {

        Integer id = view.getId();
        Field[] ids = clazz.getFields();
        for (int i = 0; i < ids.length; i++) {
            Object val = ids[i].get(null);
            if (val != null && val instanceof Integer
                    && ((Integer) val).intValue() == id.intValue()) {
                return ids[i].getName();
            }
        }

        return "";

    }

    private void selectItem(int position) {
        mCurrentSelectedPosition = position;
        if (mDrawerListView != null) {
            mDrawerListView.setItemChecked(position, true);
        }
        if (mDrawerLayout != null) {
            mDrawerLayout.closeDrawer(mFragmentContainerView);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(STATE_SELECTED_POSITION, mCurrentSelectedPosition);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Forward the new configuration the drawer toggle component.
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if (mDrawerLayout != null && isDrawerOpen()) {
            showGlobalContextActionBar();
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return mDrawerToggle.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
    }

    private void showGlobalContextActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
    }

    private ActionBar getActionBar() {
        return ((ActionBarActivity) getActivity()).getSupportActionBar();
    }

    public void refresh() {
        mGetLayersTask = setTask();
        mGetLayersTask.getAll(new GetLayersTaskParams(this));
    }

    @Override
    public void onPostExecute(List<Folder> folders) {
        application.setLayerDrawer(mDrawerLayout);

        new LegendLayerSectionBuilder(getActivity())
                .BuildAdapter(folders, 0)
                .setRecycler(mRecyclerView);
    }
}
