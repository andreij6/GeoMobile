package com.geospatialcorporation.android.geomobile.ui.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.geospatialcorporation.android.geomobile.R;
import com.geospatialcorporation.android.geomobile.library.constants.ClientTypeCodes;
import com.geospatialcorporation.android.geomobile.ui.fragments.clientselectors.ClientSelectorFragmentBase;
import com.geospatialcorporation.android.geomobile.ui.fragments.clientselectors.PluginOwnerClientSelectorFragment;
import com.geospatialcorporation.android.geomobile.ui.fragments.clientselectors.SSPClientSelectorFragment;
import com.geospatialcorporation.android.geomobile.ui.fragments.clientselectors.StandardClientSelectorFragment;

import java.util.Locale;

public class ClientSelectorSectionsPagerAdapter extends FragmentStatePagerAdapter  {
    Context mContext;
    private static final String TAG = ClientSelectorSectionsPagerAdapter.class.getSimpleName();
    ClientSelectorFragmentBase[] mClientFragments;

    public ClientSelectorSectionsPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        mContext = context;

        mClientFragments = new ClientSelectorFragmentBase[]{ new StandardClientSelectorFragment(),
                                                             new SSPClientSelectorFragment(), new PluginOwnerClientSelectorFragment() };
    }

    @Override
    public Fragment getItem(int position) {
        return mClientFragments[position];
    }

    @Override
    public int getCount() {
        return mClientFragments.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        Locale l = Locale.getDefault();
        switch (position) {
            case 0:
                return mContext.getString(R.string.standard_subscription_section).toUpperCase(l);
            //case 1:
            //    return mContext.getString(R.string.tutorial_subscription_section).toUpperCase(l);
            //case 2:
            //    return mContext.getString(R.string.default_subscription_section).toUpperCase(l);
            case 1:
                return mContext.getString(R.string.ssp_subscription_section).toUpperCase(l);
            case 2:
                return mContext.getString(R.string.plugin_owners_section).toUpperCase(l);
            default:
                return null;
        }
        //return null;
    }
}
