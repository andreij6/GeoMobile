package com.geospatialcorporation.android.geomobile.ui.activity;

import android.accounts.AccountManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import com.afollestad.materialdialogs.MaterialDialog;
import com.geospatialcorporation.android.geomobile.R;
import com.geospatialcorporation.android.geomobile.application;
import com.geospatialcorporation.android.geomobile.library.DI.Analytics.Interfaces.IGeoAnalytics;
import com.geospatialcorporation.android.geomobile.library.DI.Authentication.IGoogleAuthTokenService;
import com.geospatialcorporation.android.geomobile.library.DI.Authentication.Implementations.GoogleAuthTokenService;
import com.geospatialcorporation.android.geomobile.library.DI.ErrorHandler.Interfaces.IGeoErrorHandler;
import com.geospatialcorporation.android.geomobile.library.DI.SharedPreferences.Interfaces.IGeoSharedPrefs;
import com.geospatialcorporation.android.geomobile.library.constants.GeoSharedPreferences;
import com.geospatialcorporation.android.geomobile.library.util.ConnectionDetector;
import com.google.android.gms.common.AccountPicker;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.plus.Plus;


public class GoogleApiActivity extends AppCompatActivity implements
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
    protected MaterialDialog ProgressHelper;
    protected MaterialDialog FailureHelper;

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
        ProgressHelper = new MaterialDialog.Builder(this)
                .title(R.string.progress_dialog)
                .content(R.string.please_wait)
                .progress(true, 0)
                .build();

        FailureHelper = new MaterialDialog.Builder(this)
                .title(R.string.error)
                .content("Check your email and password.")
                .neutralText(R.string.accept)
                .build();

        mGoogleAuthTokenService = application.getGoogleAuthComponent().provideGoogleAuthTokenService();
        mGeoSharedPrefs = application.getGeoSharedPrefsComponent().provideGeoSharedPrefs();
        mAnalytics = application.getAnalyticsComponent().provideGeoAnalytics();

        mErrorHandler = application.getErrorsComponent().provideErrorHandler();
        Thread.setDefaultUncaughtExceptionHandler(mErrorHandler.UncaughtExceptionHandler(this));

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
        ConnectionDetector detector = new ConnectionDetector(this);

        boolean isConnected = detector.isConnectingToInternet();

        if(!isConnected){
            showAlertDialog(getString(R.string.no_internet_connection_title), getString(R.string.no_internet_connection_message));
        } else {
            Intent intent = AccountPicker.newChooseAccountIntent(null, null, new String[]{"com.google"},
                    false, null, null, null, null);

            startActivityForResult(intent, ACTIVITY_AUTH_REQUEST_CODE);

            if (!mGoogleApiClient.isConnected()) {
                mGoogleApiClient.connect();
            }
        }
    }

    protected void showAlertDialog(String title, String message) {
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();

        // Setting Dialog Title
        alertDialog.setTitle(title);

        // Setting Dialog Message
        alertDialog.setMessage(message);

        // Setting alert dialog icon
        //alertDialog.setIcon(R.drawable.fail);

        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getString(R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        // Showing Alert Message
        alertDialog.show();
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
        mAnalytics.onStop(this);
    }

    public GoogleAuthTokenService.Parameters getGoogleAuthParmaters(String accountname, MaterialDialog helper){
        return new GoogleAuthTokenService.Parameters(
                ACTIVITY_AUTH_REQUEST_CODE,
                accountname, this, helper);
    }

    protected void onActivityResult(final int requestCode, final int responseCode, final Intent data) {
        if (requestCode == ACTIVITY_AUTH_REQUEST_CODE) {
            if (responseCode == RESULT_OK) {
                accountName = data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);

                mGeoSharedPrefs.add(GeoSharedPreferences.GOOGLE_ACCOUNT, accountName);
                mGeoSharedPrefs.apply();

                ProgressHelper = new MaterialDialog.Builder(this)
                        .title(R.string.progress_dialog)
                        .content(R.string.please_wait)
                        .progress(true, 0)
                        .build();

                mGoogleAuthTokenService.GetAndUseAuthToken(getGoogleAuthParmaters(accountName, ProgressHelper));
            }
        }

        if (requestCode == RC_SIGN_IN) {
            mIntentInProgress = false;

            if (!mGoogleApiClient.isConnecting()) {
                mGoogleApiClient.connect();
            }
        }
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

                mAnalytics.sendException(e);
            }
        }
    }

    @Override
    public void onConnected(Bundle connectionHint) {
        // We've resolved any connection errors.  mGoogleApiClient can be used to
        // access Google APIs on behalf of the user.
        // TODO: store authtoken send to geounderground api
        //Log.d(TAG, "connected");
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