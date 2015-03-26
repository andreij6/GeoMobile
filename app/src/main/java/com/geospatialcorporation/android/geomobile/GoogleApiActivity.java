package com.geospatialcorporation.android.geomobile;

import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;

import com.afollestad.materialdialogs.MaterialDialog;
import com.geospatialcorporation.android.geomobile.util.Dialogs;
import com.google.android.gms.common.AccountPicker;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks;
import com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.plus.Plus;

public class GoogleApiActivity extends Activity implements
        ConnectionCallbacks, OnConnectionFailedListener {

    private Dialogs dialog;

    /* Request code used to invoke sign in user interactions. */
    private static final int RC_SIGN_IN = 0;

    /* Client used to interact with Google APIs. */
    private GoogleApiClient mGoogleApiClient;

    /* A flag indicating that a PendingIntent is in progress and prevents
     * us from starting further intents.
     */
    private boolean mIntentInProgress;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dialog = new Dialogs();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Plus.API)
                .addScope(Plus.SCOPE_PLUS_LOGIN)
                .build();
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
/*
    protected void onActivityResult(final int requestCode, final int resultCode,
                                    final Intent data) {
        if (requestCode == 1 && resultCode == RESULT_OK) {
            String accountName = data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
        }
    }
*/
    protected void onActivityResult(final int requestCode, final int responseCode, final Intent data) {
        dialog.message(data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME), this);

        if (requestCode == RC_SIGN_IN) {
            mIntentInProgress = false;

            if (!mGoogleApiClient.isConnecting()) {
                mGoogleApiClient.connect();
            }
        }

        Intent mainActivityIntent = new Intent(this, MainActivity.class);

        // TODO: Remove this horrible testing code
        mainActivityIntent.putExtra("authToken", "boop");
        startActivity(mainActivityIntent);
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
    }

    public void onConnectionSuspended(int cause) {
        mGoogleApiClient.connect();
    }

    public GoogleApiClient getGoogleApiClient() {
        return mGoogleApiClient;
    }
}