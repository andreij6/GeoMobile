package com.geospatialcorporation.android.geomobile.library.DI.Authentication.Implementations;

import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.geospatialcorporation.android.geomobile.application;
import com.geospatialcorporation.android.geomobile.library.helpers.ProgressDialogHelper;
import com.geospatialcorporation.android.geomobile.library.rest.LoginService;
import com.geospatialcorporation.android.geomobile.models.Subscription;
import com.geospatialcorporation.android.geomobile.ui.LoginActivity;
import com.geospatialcorporation.android.geomobile.ui.SubscriptionSelectorActivity;
import com.geospatialcorporation.android.geomobile.ui.GoogleApiActivity;
import com.geospatialcorporation.android.geomobile.ui.MainActivity;

import retrofit.RetrofitError;
import retrofit.client.Response;

public class AuthTokenRetriever {
    private static final String TAG = AuthTokenRetriever.class.getSimpleName();

    GoogleApiActivity mContext;
    String Token;
    LoginService mLoginService;
    ProgressDialogHelper ProgressHelper;

    public void retrieve(String token, GoogleApiActivity context, ProgressDialogHelper helper) {
        Token = token;
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
                Log.e(TAG, "Login by GoogleAuthToken failed.");
                this.exception = e;
            }

            return null;
        }

        @Override
        protected void onPostExecute(String authToken) {
            if(ProgressHelper != null){
                ProgressHelper.toggleProgressDialog();
            }

            if (application.getAuthToken() != null) {

                getCurrentClient();
            } else {
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
                        Log.d(TAG, Integer.toString(e.getResponse().getStatus()));

                        if (e.getResponse().getStatus() == 401) {
                            Log.d(TAG, "Unauthorized.");
                        } else {
                            Log.d(TAG, e.getResponse().getBody().toString());
                            application.setIsAdminUser(true);

                            mContext.startActivity(new Intent(mContext, SubscriptionSelectorActivity.class));
                        }
                    }
                }

                return new Object();
            }
        };

        task.execute();
    }

    public void getCurrentClient(LoginActivity context, ProgressDialogHelper progressHelper) {
        mLoginService = application.getRestAdapter().create(LoginService.class);
        mContext = context;
        ProgressHelper = progressHelper;

        AsyncTask task = new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] params) {
                try {
                    Subscription subscription = mLoginService.getCurrentClient();

                    application.setGeoSubscription(subscription);

                    mContext.startActivity(new Intent(mContext, MainActivity.class));

                    if (ProgressHelper != null) {
                        ProgressHelper.hideProgressDialog();
                    }
                } catch (RetrofitError e) {
                    if (e.getResponse() != null) {
                        Log.d(TAG, Integer.toString(e.getResponse().getStatus()));

                        if (e.getResponse().getStatus() == 401) {
                            Log.d(TAG, "Unauthorized.");
                        } else {
                            Log.d(TAG, e.getResponse().getBody().toString());
                            application.setIsAdminUser(true);

                            mContext.startActivity(new Intent(mContext, SubscriptionSelectorActivity.class));

                            if (ProgressHelper != null) {
                                ProgressHelper.hideProgressDialog();
                            }
                        }
                    }
                }

                return new Object();
            }
        };

        task.execute();
    }
}
