package com.geospatialcorporation.android.geomobile.ui.fragments.clientselectors;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.geospatialcorporation.android.geomobile.R;
import com.geospatialcorporation.android.geomobile.data.GeoUndergoundProvider;
import com.geospatialcorporation.android.geomobile.data.tables.SubscriptionColumns;
import com.geospatialcorporation.android.geomobile.library.constants.ClientTypeCodes;

import java.util.ArrayList;

public class SSPClientSelectorFragment extends ClientSelectorFragmentBase {

    private static final int CURSOR_LOADER_ID = 1;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mClientTypeCode = ClientTypeCodes.SSP.getKey();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(CURSOR_LOADER_ID, null, this);

        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] selectionArgs = { "" + mSSPClientTypeCode , "1"};
        return new CursorLoader(getActivity(), GeoUndergoundProvider.Subscriptions.CONTENT_URI, null,
                                                            SubscriptionColumns.TYPE + "= ? AND "+ SubscriptionColumns.SSP +" = ?", selectionArgs, null);
    }

    @Override
    protected void preloadSpinner(){
        mSSPClientSpinner.setVisibility(View.VISIBLE);

        final ArrayList<String> list = new ArrayList<>();

        list.add("Administrator");
        list.add("Regional Administrators");
        list.add("Project Manager");
        list.add("SSP Clients");

        ArrayAdapter<CharSequence> dataAdapter = ArrayAdapter.createFromResource(mContext, R.array.ssp_clients, R.layout.simple_spinner_item);
        dataAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSSPClientSpinner.setAdapter(dataAdapter);

        mSSPClientSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mSSPClientTypeCode = position + 1;

                refresh();

                getLoaderManager().restartLoader(CURSOR_LOADER_ID, null, SSPClientSelectorFragment.this);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(getActivity(), "Nothing Selected", Toast.LENGTH_LONG).show();
            }
        });
    }
}
