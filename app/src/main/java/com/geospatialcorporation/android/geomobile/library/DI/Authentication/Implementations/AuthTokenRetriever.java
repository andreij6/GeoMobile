package com.geospatialcorporation.android.geomobile.library.DI.Authentication.Implementations;

import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.geospatialcorporation.android.geomobile.application;
import com.geospatialcorporation.android.geomobile.api.LoginService;
import com.geospatialcorporation.android.geomobile.models.Subscription;
import com.geospatialcorporation.android.geomobile.ui.activity.GoogleApiActivity;
import com.geospatialcorporation.android.geomobile.ui.activity.LoginActivity;
import com.geospatialcorporation.android.geomobile.ui.activity.MainActivity;
import com.geospatialcorporation.android.geomobile.ui.activity.SubscriptionSelectorActivity;

import retrofit.RetrofitError;
import retrofit.client.Response;

public class AuthTokenRetriever {
    private static final String TAG = AuthTokenRetriever.class.getSimpleName();

    GoogleApiActivity mContext;
    LoginService mLoginService;
    MaterialDialog ProgressHelper;

    public void retrieve(String token, GoogleApiActivity context, MaterialDialog helper) {
        mContext = context;
        ProgressHelper = helper;
        mLoginService = application.getRestAdapter().create(LoginService.class);

        new RetrieveAuthToken().execute(token);
    }

    protected class RetrieveAuthToken extends AsyncTask<String, Void, String> {

        private Exception exception;

        @Override
        protected String doInBackground(String... urls) {
            try {
                Response response = mLoginService.google(urls[0]);

                return response.getBody().toString();

            } catch (Exception e) {
                //Log.e(TAG, "Login by GoogleAuthToken failed.");
                //Log.d(TAG, e.getMessage());
                this.exception = e;
            }

            return null;
        }

        @Override
        protected void onPostExecute(String authToken) {
            if (application.getAuthToken() != null) {
                getCurrentClient();
            } else {
                if(ProgressHelper != null){
                    ProgressHelper.hide();
                }
                Toast.makeText(mContext, "Google Login Failed", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void getCurrentClient() {
        AsyncTask task = new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] params) {
                try {
                    mLoginService.getCurrentClient();


                    mContext.startActivity(new Intent(mContext, MainActivity.class));

                } catch (RetrofitError e) {
                    if (e.getResponse() != null) {
                        //Log.d(TAG, Integer.toString(e.getResponse().getStatus()));

                        if (e.getResponse().getStatus() == 401) {
                            //Log.d(TAG, "Unauthorized.");
                        } else {
                            //Log.d(TAG, e.getResponse().getBody().toString());
                            application.setIsAdminUser(true);

                            mContext.startActivity(new Intent(mContext, SubscriptionSelectorActivity.class));
                        }
                    }
                }

                return new Object();
            }

            @Override
            protected void onPostExecute(Object o) {
                if(ProgressHelper != null){
                    ProgressHelper.hide();
                }
            }
        };

        task.execute();
    }

    public void getCurrentClient(LoginActivity context, MaterialDialog progressHelper) {
        mLoginService = application.getRestAdapter().create(LoginService.class);
        mContext = context;
        ProgressHelper = progressHelper;

        AsyncTask task = new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] params) {
                try {
                    Subscription subscription = mLoginService.getCurrentClient();

                    Intent intent = new Intent(mContext, MainActivity.class);
                    application.setGeoSubscription(subscription);

                    mContext.startActivity(intent);

                    mContext.finish();
                } catch (RetrofitError e) {
                    if (e.getResponse() != null) {
                        //Log.d(TAG, Integer.toString(e.getResponse().getStatus()));

                        if (e.getResponse().getStatus() == 401) {
                            //Log.d(TAG, "Unauthorized.");
                        } else {
                            //Log.d(TAG, e.getResponse().getBody().toString());
                            application.setIsAdminUser(true);

                            mContext.startActivity(new Intent(mContext, SubscriptionSelectorActivity.class));
                            mContext.finish();
                        }
                    }
                }

                return new Object();
            }

            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);

                if (ProgressHelper != null) {
                    ProgressHelper.hide();
                }
            }
        };

        task.execute();
    }
}
