package com.geospatialcorporation.android.geomobile.library.helpers;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.geospatialcorporation.android.geomobile.R;

import java.util.zip.Inflater;

public class TableFactory {

    Context mContext;
    TableLayout mTableLayout;
    LayoutInflater mInflater;

    public TableFactory(Context context, TableLayout layout, LayoutInflater inflater) {
        mContext = context;
        mTableLayout = layout;
        mInflater = inflater;
    }


    public void addHeaders(int layout, String...headers) {
        TableRow row = new TableRow(mContext);

        for(String header : headers){
            TextView columnName = (TextView)mInflater.inflate(layout, null);
            columnName.setText(header);

            row.addView(columnName);
        }

        mTableLayout.addView(row);
    }

    public TableLayout build() {
        return mTableLayout;
    }
}
