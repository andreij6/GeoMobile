package com.geospatialcorporation.android.geomobile.ui;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.design.widget.TabLayout;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.geospatialcorporation.android.geomobile.R;
import com.geospatialcorporation.android.geomobile.application;
import com.geospatialcorporation.android.geomobile.library.DI.ErrorHandler.Interfaces.IGeoErrorHandler;
import com.geospatialcorporation.android.geomobile.library.helpers.ZoomOutPageTransformer;
import com.geospatialcorporation.android.geomobile.library.util.DeviceTypeUtil;
import com.geospatialcorporation.android.geomobile.ui.adapters.ClientSelectorSectionsPagerAdapter;

public class SubscriptionSelectorActivity extends AppCompatActivity implements ActionBar.TabListener {

    ClientSelectorSectionsPagerAdapter mSectionsPagerAdapter;

    ViewPager mViewPager;
    TabLayout mTabLayout;
    IGeoErrorHandler mErrorHandler;

    //region Getters & Setters
    public void setBackButtonClickOnce(boolean backButtonClickOnce) {
        mBackButtonClickOnce = backButtonClickOnce;
    }
    //endregion

    private boolean mBackButtonClickOnce;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.title_activity_select_subscription);
        setContentView(R.layout.activity_subscription_selector);
        setBackButtonClickOnce(false);
        application.clearUserSpecificData();

        if(DeviceTypeUtil.isTablet(getResources())){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        getSupportActionBar().hide();

        application.getLayerManager().reset();

        mViewPager = (ViewPager) findViewById(R.id.pager);
        mTabLayout = (TabLayout) findViewById(R.id.tab_layout);

        mTabLayout.addTab(mTabLayout.newTab().setText(R.string.standard_subscription_section));
        //mTabLayout.addTab(mTabLayout.newTab().setText(R.string.tutorial_subscription_section));
        //mTabLayout.addTab(mTabLayout.newTab().setText(R.string.default_subscription_section));
        mTabLayout.addTab(mTabLayout.newTab().setText(R.string.ssp_subscription_section));
        mTabLayout.addTab(mTabLayout.newTab().setText(R.string.plugin_owners_section));

        mTabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        mTabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);

        mSectionsPagerAdapter = new ClientSelectorSectionsPagerAdapter(getSupportFragmentManager(), this);

        //mErrorHandler = application.getErrorsComponent().provideErrorHandler();
        //Thread.setDefaultUncaughtExceptionHandler(mErrorHandler.UncaughtExceptionHandler());

        mViewPager.setAdapter(mSectionsPagerAdapter);

        mViewPager.setPageTransformer(true, new ZoomOutPageTransformer());

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));

        mTabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mViewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return false;
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_client_selector, menu);
//        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        return id == R.id.action_settings || super.onOptionsItemSelected(item);

    }

    @Override
    public void onBackPressed() {
        if(mBackButtonClickOnce){
            application.Logout();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        } else {
            Toast.makeText(this, "You will be signed out if you click the back button again", Toast.LENGTH_LONG).show();
            setBackButtonClickOnce(true);
        }
    }


    //region Action.TabListner Interface
    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {

    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {

    }
    //endregion

}
