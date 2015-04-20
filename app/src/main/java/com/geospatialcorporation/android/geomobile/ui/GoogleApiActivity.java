package com.geospatialcorporation.android.geomobile.ui;

import android.accounts.AccountManager;
import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.nfc.Tag;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.geospatialcorporation.android.geomobile.R;
import com.geospatialcorporation.android.geomobile.application;
import com.geospatialcorporation.android.geomobile.library.util.Dialogs;
import com.geospatialcorporation.android.geomobile.library.rest.LoginService;
import com.geospatialcorporation.android.geomobile.ui.fragments.ClientFragment;
import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.GooglePlayServicesAvailabilityException;
import com.google.android.gms.auth.UserRecoverableAuthException;
import com.google.android.gms.common.AccountPicker;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks;
import com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.plus.Plus;

import java.io.IOException;

import retrofit.RetrofitError;
import retrofit.client.Response;

public class GoogleApiActivity extends Activity implements
        ConnectionCallbacks, OnConnectionFailedListener {
    private static final String TAG = "GoogleApiActivity";
    private static final int REQ_SIGN_IN_REQUIRED = 55664;
    /* Request code used to invoke sign in user interactions. */
    private static final int RC_SIGN_IN = 0;
    private static final int ACTIVITY_AUTH_REQUEST_CODE = 1;
    private static final String SCOPES = "oauth2:https://www.googleapis.com/auth/userinfo.email https://www.googleapis.com/auth/plus.me https://www.googleapis.com/auth/userinfo.profile https://www.googleapis.com/auth/mapsengine";

    private Dialogs dialog;

    /* Client used to interact with Google APIs. */
    private GoogleApiClient mGoogleApiClient;
    private LoginService service;
    private String accountName;
    private Context context;

    /* A flag indicating that a PendingIntent is in progress and prevents
     * us from starting further intents.
     */
    private boolean mIntentInProgress;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;

        dialog = new Dialogs();

        service = application.getRestAdapter().create(LoginService.class);

        if (application.getGoogleClient() == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addApi(Plus.API)
                    .addScope(Plus.SCOPE_PLUS_LOGIN)
                    .build();

            application.setGoogleClient(mGoogleApiClient);
        } else {
            mGoogleApiClient = application.getGoogleClient();
        }
    }

    /**
     * Try to sign in the user.
     */
    public void signIn() {
        Intent intent = AccountPicker.newChooseAccountIntent(null, null, new String[]{"com.google"},
                false, null, null, null, null);

        startActivityForResult(intent, 1);

        if (!mGoogleApiClient.isConnected()) {
            mGoogleApiClient.connect();
        }

        //updateConnectButtonState();
    }

    public void signOut() {
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    public void revokeAccess() {
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.clearDefaultAccountAndReconnect();
        }
    }

    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    protected void onStop() {
        super.onStop();

        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    public void onDisconnected() {
        // TODO: implement
    }

    protected void onActivityResult(final int requestCode, final int responseCode, final Intent data) {

        int MY_ACTIVITYS_AUTH_REQUEST_CODE = 0;

        accountName = data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);

        /*if (requestCode == MY_ACTIVITYS_AUTH_REQUEST_CODE) {
            if (responseCode == RESULT_OK) {
                new RetrieveTokenTask().execute(accountName);
            }
        }*/
        Log.d(TAG, "Request Code: " + requestCode + " || Response Code: " + responseCode);

        getAndUseAuthTokenInAsyncTask();
        if (requestCode == MY_ACTIVITYS_AUTH_REQUEST_CODE) {
            if (responseCode == RESULT_OK) {
            }

            if (responseCode == RESULT_CANCELED) {
                //dialog.message("Result cancelled.");
            }
        } else {
            //dialog.message("No auth request code.");
        }

        //dialog.message(accountName, this);

        if (requestCode == RC_SIGN_IN) {
            mIntentInProgress = false;

            if (!mGoogleApiClient.isConnecting()) {
                mGoogleApiClient.connect();
            }
        }

        //dialog.message(application.getAuthToken());

        Intent mainActivityIntent = new Intent(this, MainActivity.class);

        // TODO: Remove this horrible testing code
        mainActivityIntent.putExtra("authToken", "ya29.TwEnwvsNEaINP6fGQvox11OrWHWJj1IKHxOi-oMi0hp8HpMywTwos9qW");

        // TODO: Client request - select client?
        //startActivity(mainActivityIntent);
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        if (!mIntentInProgress && result.hasResolution()) {
            try {
                mIntentInProgress = true;
                startIntentSenderForResult(result.getResolution().getIntentSender(),
                        RC_SIGN_IN, null, 0, 0, 0);
            } catch (IntentSender.SendIntentException e) {
                // The intent was canceled before it was sent.  Return to the default
                // state and attempt to connect to get an updated ConnectionResult.
                mIntentInProgress = false;
                mGoogleApiClient.connect();
            }
        }
    }

    @Override
    public void onConnected(Bundle connectionHint) {
        // We've resolved any connection errors.  mGoogleApiClient can be used to
        // access Google APIs on behalf of the user.
        // TODO: store authtoken send to geounderground api

        // code
    }

    public void onConnectionSuspended(int cause) {
        mGoogleApiClient.connect();
    }

    public GoogleApiClient getGoogleApiClient() {
        return mGoogleApiClient;
    }

    private class RetrieveTokenTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String accountName = params[0];
            String scopes = "oauth2:profile email";
            String token = null;
            try {
                token = GoogleAuthUtil.getToken(getApplicationContext(), accountName, scopes);

                dialog.message(token);

                Response response = service.google(token);
                application.setAuthToken(response.getBody().toString());
            } catch (IOException e) {
                Log.e(TAG, e.getMessage());
            } catch (UserRecoverableAuthException e) {
                startActivityForResult(e.getIntent(), REQ_SIGN_IN_REQUIRED);
            } catch (GoogleAuthException e) {
                Log.e(TAG, e.getMessage());
            }
            return token;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            // TODO: Make request to GeoUnderground to authenticate user based on Google AuthToken
        }
    }


    // Example of how to use the GoogleAuthUtil in a blocking, non-main thread context
    void getAndUseAuthTokenBlocking() {
        try {
            // Retrieve a token for the given account and scope. It will always return either
            // a non-empty String or throw an exception.
            final String token = GoogleAuthUtil.getToken(this, accountName, SCOPES);
            // Do work with token.

            application.setGoogleAuthToken(token);

            Log.d(TAG, "Attempting GeoLogin with token: " + token);
            new RetrieveAuthToken().execute(token);

            if (token != null) {
                // invalidate the token that we found is bad so that GoogleAuthUtil won't
                // return it next time (it may have cached it)
                //GoogleAuthUtil.invalidateToken(Context, String)(context, token);
                // consider retrying getAndUseTokenBlocking() once more
                return;
            }
            return;
        } catch (GooglePlayServicesAvailabilityException playEx) {
            Dialog alert = GooglePlayServicesUtil.getErrorDialog(
                    playEx.getConnectionStatusCode(),
                    this,
                    ACTIVITY_AUTH_REQUEST_CODE);
        } catch (UserRecoverableAuthException userAuthEx) {
            // Start the user recoverable action using the intent returned by
            // getIntent()
            this.startActivityForResult(
                    userAuthEx.getIntent(),
                    ACTIVITY_AUTH_REQUEST_CODE);
            return;
        } catch (IOException transientEx) {
            // network or server error, the call is expected to succeed if you try again later.
            // Don't attempt to call again immediately - the request is likely to
            // fail, you'll hit quotas or back-off.
            return;
        } catch (GoogleAuthException authEx) {
            // Failure. The call is not expected to ever succeed so it should not be
            // retried.
            return;
        }
    }

    // Example of how to use AsyncTask to call blocking code on a background thread.
    void getAndUseAuthTokenInAsyncTask() {
        AsyncTask task = new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] params) {
                getAndUseAuthTokenBlocking();
                return new Object();
            }
        };

        task.execute();
    }

    private void useGoogleAuthToken() {
        String googleAuthToken = application.getGoogleAuthToken();

        class RetrieveAuthToken extends AsyncTask<String, Void, String> {

            private Exception exception;

            protected String doInBackground(String... urls) {
                try {
                    Response response = application.getRestAdapter().create(LoginService.class).google(urls[0]);
                    String authToken = response.getBody().toString();
                    return authToken;
                } catch (Exception e) {
                    Log.e(TAG, "Login by GoogleAuthToken failed.");
                    this.exception = e;
                }

                return null;
            }

            protected void onPostExecute(String authToken) {
                if (application.getAuthToken() != null) {
                    Log.d(TAG, "GeoAuthToken set: " + application.getAuthToken());

                    tryCurrentClient();
                }
            }
        }

        Log.d(TAG, "Attempting GeoLogin with google auth token: " + googleAuthToken);
        new RetrieveAuthToken().execute(googleAuthToken);
    }

    class RetrieveAuthToken extends AsyncTask<String, Void, String> {

        private Exception exception;

        protected String doInBackground(String... urls) {
            try {
                Response response = application.getRestAdapter().create(LoginService.class).google(urls[0]);
                String authToken = response.getBody().toString();
                return authToken;
            } catch (Exception e) {
                Log.e(TAG, "Login by GoogleAuthToken failed.");
                this.exception = e;
            }

            return null;
        }

        protected void onPostExecute(String authToken) {
            if (application.getAuthToken() != null) {
                Log.i(TAG, "GeoAuthToken set: " + application.getAuthToken());

                tryCurrentClient();
            }
        }
    }

    private void tryCurrentClient() {
        try {
            service.getCurrentClient();
            startActivity(new Intent(context, MainActivity.class));
        } catch (RetrofitError e) {
            if (e.getResponse() != null) {
                Log.d(TAG, Integer.toString(e.getResponse().getStatus()));
            }

            Fragment fragment = new ClientFragment();

            FragmentManager fragmentManager = getFragmentManager();

            fragmentManager.beginTransaction()
                    .replace(R.id.login_content_frame, fragment)
                    .commit();
        }
    }
}