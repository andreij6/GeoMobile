package com.geospatialcorporation.android.geomobile.ui.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.geospatialcorporation.android.geomobile.R;
import com.geospatialcorporation.android.geomobile.library.constants.ClientTypeCodes;
import com.geospatialcorporation.android.geomobile.ui.fragments.ClientSelectorFragment;

import java.util.Locale;

public class ClientSelectorSectionsPagerAdapter extends FragmentPagerAdapter {
    Context mContext;

    public ClientSelectorSectionsPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        Integer clientCode = ClientTypeCodes.STANDARD.getKey();

        /*
        switch (position){
            case 0:
                clientCode = ClientTypeCodes.STANDARD.getKey();
                break;
            case 1:
                clientCode = ClientTypeCodes.TUTORIAL.getKey();
                break;
            case 2:
                clientCode = ClientTypeCodes.DEFAULT.getKey();
                break;
            case 3:
                clientCode = ClientTypeCodes.SSP.getKey();
                break;
            case 4:
                clientCode = ClientTypeCodes.DEMOCLONE.getKey();
                break;
            default:
                clientCode = 333;
                break;
        }
        */

        return new ClientSelectorFragment().initialize(clientCode);
    }

    @Override
    public int getCount() {
        return 1;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mContext.getString(R.string.all_subscriptions_section).toUpperCase();
        //Locale l = Locale.getDefault();
        //switch (position) {
        //    case 0:
        //        return mContext.getString(R.string.standard_subscription_section).toUpperCase(l);
        //    case 1:
        //        return mContext.getString(R.string.tutorial_clone_subscription_section).toUpperCase(l);
        //    case 2:
        //        return mContext.getString(R.string.default_subscription_section).toUpperCase(l);
        //    case 3:
        //        return mContext.getString(R.string.ssp_subscription_section).toUpperCase(l);
        //    case 4:
        //        return mContext.getString(R.string.plugin_owners_section).toUpperCase(l);
        //}
        //return null;
    }
}
