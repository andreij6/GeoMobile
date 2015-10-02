package com.geospatialcorporation.android.geomobile.library.DI.Authentication.Implementations;

import android.app.Dialog;
import android.os.AsyncTask;

import com.afollestad.materialdialogs.MaterialDialog;
import com.geospatialcorporation.android.geomobile.application;
import com.geospatialcorporation.android.geomobile.library.DI.Authentication.IGoogleAuthTokenService;
import com.geospatialcorporation.android.geomobile.library.DI.Authentication.models.AuthTokenParams;
import com.geospatialcorporation.android.geomobile.library.helpers.ProgressDialogHelper;
import com.geospatialcorporation.android.geomobile.ui.GoogleApiActivity;
import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.GooglePlayServicesAvailabilityException;
import com.google.android.gms.auth.UserRecoverableAuthException;
import com.google.android.gms.common.GooglePlayServicesUtil;

import java.io.IOException;

public class GoogleAuthTokenService implements IGoogleAuthTokenService {

    private AuthTokenRetriever mAuthTokenRetriever;
    protected int ACTIVITY_AUTH_REQUEST_CODE;
    protected String AccountName;
    private static final String SCOPES = "oauth2:https://www.googleapis.com/auth/userinfo.email https://www.googleapis.com/auth/plus.me https://www.googleapis.com/auth/userinfo.profile https://www.googleapis.com/auth/mapsengine";
    protected MaterialDialog ProgressHelper;
    protected GoogleApiActivity mContext;

    public GoogleAuthTokenService(AuthTokenRetriever retriever){
        mAuthTokenRetriever = retriever;
    }

    public void GetAndUseAuthToken(AuthTokenParams parameters) {
        ACTIVITY_AUTH_REQUEST_CODE = parameters.activity_auth_request_code;
        AccountName = parameters.account_name;
        ProgressHelper = parameters.mProgress_helper;
        mContext = parameters.google_context;

        new GetAndUseAuthTokenInAsyncTask().execute();
    }

    private class GetAndUseAuthTokenInAsyncTask extends AsyncTask<Object, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Object[] params) {
            getAndUseAuthTokenBlocking();
            return true;
        }

        // Example of how to use the GoogleAuthUtil in a blocking, non-main thread context
        void getAndUseAuthTokenBlocking() {
            try {
                // Retrieve a token for the given account and scope. It will always return either
                // a non-empty String or throw an exception.
                final String token = GoogleAuthUtil.getToken(mContext, AccountName, SCOPES);
                // Do work with token.

                application.setGoogleAuthToken(token);

                mAuthTokenRetriever.retrieve(token, mContext, ProgressHelper);

                if (token != null) {
                    // invalidate the token that we found is bad so that GoogleAuthUtil won't
                    // return it next time (it may have cached it)
                    //GoogleAuthUtil.invalidateToken(Context, String)(context, token);
                    // consider retrying getAndUseTokenBlocking() once more
                    return;
                }
            } catch (GooglePlayServicesAvailabilityException playEx) {
                Dialog alert = GooglePlayServicesUtil.getErrorDialog(
                        playEx.getConnectionStatusCode(),
                        mContext,
                        ACTIVITY_AUTH_REQUEST_CODE);

                alert.show();
            } catch (UserRecoverableAuthException userAuthEx) {
                // Start the user recoverable action using the intent returned by
                // getIntent()
                mContext.startActivityForResult(
                        userAuthEx.getIntent(),
                        ACTIVITY_AUTH_REQUEST_CODE);
            } catch (IOException | GoogleAuthException transientEx) {
                // network or server error, the call is expected to succeed if you try again later.
                // Don't attempt to call again immediately - the request is likely to
                // fail, you'll hit quotas or back-off.
            }
        }

    }

    public static class Parameters extends AuthTokenParams{

        public Parameters(int requestCode, String accountName, GoogleApiActivity context, MaterialDialog progess_helper){
            activity_auth_request_code = requestCode;
            account_name = accountName;
            google_context = context;
            mProgress_helper = progess_helper;
        }
    }
}
