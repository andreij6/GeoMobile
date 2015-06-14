package com.geospatialcorporation.android.geomobile.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.geospatialcorporation.android.geomobile.R;

import java.util.List;

public class MainNavigationAdapter extends ArrayAdapter<String> {
    List<String> mMenuItems;
    Context mContext;

    public MainNavigationAdapter(Context context, List<String> menuItems) {
        super(context, R.layout.drawer_main_nav_list_item, menuItems);
        mMenuItems = menuItems;
        mContext = context;
    }

    @Override
    public View getView(int position, View coverView, ViewGroup parent) {
        // TODO Auto-generated method stub

        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View v = inflater.inflate(R.layout.drawer_main_nav_list_item, null);

        TextView name = (TextView)v.findViewById(R.id.menuItemTV);
        ImageView icon = (ImageView)v.findViewById(R.id.menuItemIcon);

        name.setText(mMenuItems.get(position));

        switch (mMenuItems.get(position)){
            case MenuConstants.MAP:
                icon.setImageDrawable(mContext.getDrawable(R.drawable.ic_map_marker_white_18dp));
                break;
            case MenuConstants.LAYERS:
                icon.setImageDrawable(mContext.getDrawable(R.drawable.ic_layers_white_18dp));
                break;
            case MenuConstants.LIBRARY:
                icon.setImageDrawable(mContext.getDrawable(R.drawable.ic_book_white_18dp));
                break;
            case MenuConstants.ADMINCLITENTS:
                icon.setImageDrawable(mContext.getDrawable(R.drawable.ic_account_search_white_18dp));
                break;
            case MenuConstants.ACCOUNT:
                icon.setImageDrawable(mContext.getDrawable(R.drawable.ic_account_circle_white_18dp));
                break;
            case MenuConstants.LOGOUT:
                icon.setImageDrawable(mContext.getDrawable(R.drawable.ic_logout_white_18dp));
            default:
                break;
        }

        return v;

    }

    private class MenuConstants {
        public static final String MAP = "Map";
        public static final String LAYERS = "Layers";
        public static final String LIBRARY = "Library";
        public static final String ADMINCLITENTS = "Admin Clients";
        public static final String ACCOUNT = "Account";
        public static final String LOGOUT = "Logout";
    }

}

