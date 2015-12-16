package com.geospatialcorporation.android.geomobile.library.DI.Tasks.Implementations;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.geospatialcorporation.android.geomobile.application;
import com.geospatialcorporation.android.geomobile.data.GeoUndergoundProvider;
import com.geospatialcorporation.android.geomobile.data.tables.SubscriptionColumns;
import com.geospatialcorporation.android.geomobile.library.DI.Tasks.Interfaces.IGetClientsTask;
import com.geospatialcorporation.android.geomobile.library.DI.Tasks.models.GetClientsTaskParams;
import com.geospatialcorporation.android.geomobile.library.constants.ClientTypeCodes;
import com.geospatialcorporation.android.geomobile.library.rest.LoginService;
import com.geospatialcorporation.android.geomobile.models.PluginSubscriptionResponse;
import com.geospatialcorporation.android.geomobile.models.Subscription;

import java.util.List;
import java.util.Vector;

import retrofit.RetrofitError;

public class GetClientsTask implements IGetClientsTask {

    private static final String TAG = GetClientsTask.class.getSimpleName();

    public void getClients(GetClientsTaskParams params) {
        new GetClientsAsync(params).execute();
    }

    protected class GetClientsAsync extends AsyncTask<Void, Void, Void> {

        List<Subscription> mDataSet;
        int mClientTypeCode;
        int mSSPClientTypeCode;
        FragmentActivity mContext;
        LoginService mLoginService;

        public GetClientsAsync(GetClientsTaskParams params) {
            mDataSet = params.getDataSet();
            mClientTypeCode = params.getClientTypeCode();
            mContext = params.getContext();
            mSSPClientTypeCode = params.getSSPClientTypeCode();
            mLoginService = application.getRestAdapter().create(LoginService.class);
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {

                if (mClientTypeCode == ClientTypeCodes.SSP.getKey()) {
                    PluginSubscriptionResponse response = mLoginService.searchPluginClients(mSSPClientTypeCode);
                    mDataSet = response.getItems();

                    saveData(mDataSet, true);
                } else {
                    Integer pageSize = 25000;
                    mDataSet = mLoginService.searchClients(mClientTypeCode, pageSize);

                    saveData(mDataSet, false);
                }


            } catch (RetrofitError e) {
                if (e.getResponse() != null) {
                    //Log.d(TAG, e.getResponse().getStatus() + " : Line 112");
                }
            }

            return null;
        }

        private void saveData(List<Subscription> dataSet, boolean isSSP) {
            Vector<ContentValues> cVVector = new Vector<>();

            Cursor cursor;

            for (Subscription subscription : dataSet) {
                Uri uri = GeoUndergoundProvider.Subscriptions.withId(subscription.getId());

                cursor = mContext.getContentResolver().query(uri, null, null, null, null);

                try {
                    if (cursor == null || cursor.getCount() == 0) {
                        ContentValues contentValues = new ContentValues();

                        contentValues.put(SubscriptionColumns.SUBSCRIPTION_ID, subscription.getId());
                        contentValues.put(SubscriptionColumns.CREATED, subscription.getCreated());
                        contentValues.put(SubscriptionColumns.NAME, subscription.getName());

                        contentValues.put(SubscriptionColumns.SSP, isSSP);
                        if (isSSP) {
                            contentValues.put(SubscriptionColumns.TYPE, mSSPClientTypeCode);
                        } else {
                            contentValues.put(SubscriptionColumns.TYPE, subscription.getType());
                        }

                        cVVector.add(contentValues);
                    }
                } finally {
                    if(cursor != null){
                        cursor.close();
                    }
                }

            }

            int inserted = 0;

            if (cVVector.size() > 0) {
                ContentValues[] cvArray = new ContentValues[cVVector.size()];
                cVVector.toArray(cvArray);
                inserted = mContext.getContentResolver().bulkInsert(GeoUndergoundProvider.Subscriptions.CONTENT_URI, cvArray);
            }

            //Log.d(TAG, "fETCH clients complete. " + inserted + " Inserted");


        }

    }
}
