package com.geospatialcorporation.android.geomobile.ui;

import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.util.Log;

import com.geospatialcorporation.android.geomobile.application;
import com.geospatialcorporation.android.geomobile.library.DI.Analytics.Interfaces.IGeoAnalytics;
import com.geospatialcorporation.android.geomobile.library.DI.Authentication.IGoogleAuthTokenService;
import com.geospatialcorporation.android.geomobile.library.DI.Authentication.Implementations.GoogleAuthTokenService;
import com.geospatialcorporation.android.geomobile.library.DI.ErrorHandler.Interfaces.IGeoErrorHandler;
import com.geospatialcorporation.android.geomobile.library.DI.SharedPreferences.IGeoSharedPrefs;
import com.geospatialcorporation.android.geomobile.library.DI.SharedPreferences.Implementations.GeoSharedPrefs;
import com.geospatialcorporation.android.geomobile.library.helpers.ProgressDialogHelper;
import com.google.android.gms.common.AccountPicker;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.plus.Plus;


public class GoogleApiActivity extends Activity implements
        ConnectionCallbacks, OnConnectionFailedListener {

    private static final String TAG = GoogleApiActivity.class.getSimpleName();
    private static final int REQ_SIGN_IN_REQUIRED = 55664;
    /* Request code used to invoke sign in user interactions. */
    private static final int RC_SIGN_IN = 0;
    private static final int ACTIVITY_AUTH_REQUEST_CODE = 1;
    /* Client used to interact with Google APIs. */
    private GoogleApiClient mGoogleApiClient;
    private String accountName;
    private Context context;
    protected ProgressDialogHelper ProgressHelper;

    protected IGoogleAuthTokenService mGoogleAuthTokenService;
    protected IGeoSharedPrefs mGeoSharedPrefs;
    protected IGeoAnalytics mAnalytics;
    protected IGeoErrorHandler mErrorHandler;
    /* A flag indicating that a PendingIntent is in progress and prevents
     * us from starting further intents.
     */
    private boolean mIntentInProgress;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        ProgressHelper = new ProgressDialogHelper(context);

        mGoogleAuthTokenService = application.getGoogleAuthComponent().provideGoogleAuthTokenService();
        mGeoSharedPrefs = application.getGeoSharedPrefsComponent().provideGeoSharedPrefs();
        mAnalytics = application.getAnalyticsComponent().provideGeoAnalytics();
        mErrorHandler = application.getErrorsComponent().provideErrorHandler();

        Thread.setDefaultUncaughtExceptionHandler(mErrorHandler.UncaughtExceptionHandler());

        if (application.getGoogleClient() == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(context)
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


        startActivityForResult(intent, ACTIVITY_AUTH_REQUEST_CODE);

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

    public GoogleAuthTokenService.Parameters getGoogleAuthParmaters(String accountname, ProgressDialogHelper helper){

        return new GoogleAuthTokenService.Parameters(
                ACTIVITY_AUTH_REQUEST_CODE,
                accountname, this, helper);
    }

    protected void onActivityResult(final int requestCode, final int responseCode, final Intent data) {

        //region Commented code
        /*if (requestCode == MY_ACTIVITYS_AUTH_REQUEST_CODE) {
            if (responseCode == RESULT_OK) {
                new RetrieveTokenTask().execute(accountName);
            }
        }*/
        //endregion

        if (requestCode == ACTIVITY_AUTH_REQUEST_CODE) {
            if (responseCode == RESULT_OK) {
                accountName = data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);

                mGeoSharedPrefs.add(GeoSharedPrefs.Items.GOOGLE_ACCOUNT_NAME, accountName);

                ProgressHelper = new ProgressDialogHelper(this);

                mGoogleAuthTokenService.GetAndUseAuthToken(getGoogleAuthParmaters(accountName, ProgressHelper));
            }

            if (responseCode == RESULT_CANCELED) {
                //dialog.message("Result cancelled.");
            }
        }

        //region Commented Code
        //else {
            //dialog.message("No auth request code.");
        //}


        //dialog.message(accountName, this);
        //endregion

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
        Log.d(TAG, "connected");
        // code
    }

    @Override
    public void onConnectionSuspended(int i) {
        // TODO: implement
    }

    //public void onConnectionSuspended(int cause) {
    //    mGoogleApiClient.connect();
    //}

    public GoogleApiClient getGoogleApiClient() {
        return mGoogleApiClient;
    }

}