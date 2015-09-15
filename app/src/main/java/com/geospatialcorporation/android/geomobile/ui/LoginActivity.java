package com.geospatialcorporation.android.geomobile.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.geospatialcorporation.android.geomobile.BuildConfig;
import com.geospatialcorporation.android.geomobile.R;
import com.geospatialcorporation.android.geomobile.application;
import com.geospatialcorporation.android.geomobile.library.DI.Analytics.Models.GoogleAnalyticEvent;
import com.geospatialcorporation.android.geomobile.library.DI.Map.Interfaces.ILayerManager;
import com.geospatialcorporation.android.geomobile.library.DI.Tasks.Interfaces.IUserLoginTask;
import com.geospatialcorporation.android.geomobile.library.constants.GeoSharedPreferences;
import com.geospatialcorporation.android.geomobile.library.util.Authentication;
import com.geospatialcorporation.android.geomobile.library.util.LoginValidator;
import com.geospatialcorporation.android.geomobile.ui.Interfaces.IFullExecuter;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.SignInButton;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class LoginActivity extends GoogleApiActivity implements LoaderCallbacks<Cursor>, IFullExecuter<Boolean> {

    private final static String TAG = LoginActivity.class.getSimpleName();

    private Authentication mAuthentication;
    private ILayerManager mLayerManager;
    private IUserLoginTask mUserLoginTask;

    //region  UI references.
    @InjectView(R.id.email) AutoCompleteTextView mEmailView;
    @InjectView(R.id.password) EditText mPasswordView;
    @InjectView(R.id.plus_sign_in_button) SignInButton mPlusSignInButton;
    @InjectView(R.id.login_form) View mLoginFormView;
    @InjectView(R.id.email_login_form) View mEmailLoginFormView;
    @InjectView(R.id.plus_sign_out_buttons) View mSignOutButtons;
    @InjectView(R.id.email_sign_in_button) Button mEmailSignInButton;
    @InjectView(R.id.signUpLink) TextView mSignUpLink;
    @InjectView(R.id.forgotPassword) TextView mForgotPassword;
    @InjectView(R.id.rememberCB) CheckBox mRememberMe;
    //endregion

    //region OnclickListeners
    @OnClick(R.id.plus_sign_in_button)
    public void GooglePlusSignInClick(){
        mAnalytics.trackClick(new GoogleAnalyticEvent().GoogleSignIn());

        signIn();
    }

    @OnClick(R.id.email_sign_in_button)
    public void EmailSignInClick(){
        mAnalytics.trackClick(new GoogleAnalyticEvent().SignInBtn());

        validateLogin();

    }

    @OnClick(R.id.signUpLink)
    public void potientialSignup(){
        mAnalytics.trackClick(new GoogleAnalyticEvent().SignUp());
    }

    @OnClick(R.id.forgotPassword)
    public void forgotPasswordLink(){
        mAnalytics.trackClick(new GoogleAnalyticEvent().ForgotPassword());
    }
    //endregion

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (hasAuthToken()) {
            setContentView(R.layout.activity_login);
            ButterKnife.inject(this);
            mSignUpLink.setMovementMethod(LinkMovementMethod.getInstance());
            mForgotPassword.setMovementMethod(LinkMovementMethod.getInstance());

        }

        application.setIsAdminUser(false);
        application.setAuthToken(null);

        mLayerManager = application.getLayerManager();
        mLayerManager.reset();

        //set email & password if saved
        mEmailView.setText(mGeoSharedPrefs.getString(GeoSharedPreferences.LOGIN_EMAIL, ""));
        mPasswordView.setText(mGeoSharedPrefs.getString(GeoSharedPreferences.LOGIN_PASSWORD, ""));
        mRememberMe.setChecked(mGeoSharedPrefs.getBoolean(GeoSharedPreferences.LOGIN_REMEMBER, false));

        if (!supportsGooglePlayServices()) {
            // Don't offer G+ sign in if the app's version is too low to support Google Play
            // Services.
            mPlusSignInButton.setVisibility(View.GONE);
            return;
        }

        mAuthentication = new Authentication(this, ProgressHelper, FailureHelper);

        mUserLoginTask = null;

        populateAutoComplete();
        
        AttemptAutomaticLogin();

        if (BuildConfig.DEBUG) {
            //commented for Cintacs review
            //mEmailView.setText("jon.shaffer@geospatialcorporation.com");
            //mPasswordView.setText("f5eHXqWEGp1W");
        }

        mPlusSignInButton.requestFocus();

        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.password || id == EditorInfo.IME_NULL) {
                    validateLogin();
                    return true;
                }
                return false;
            }
        });
    }

    private void AttemptAutomaticLogin() {
        String accountName = mGeoSharedPrefs.getString(GeoSharedPreferences.GOOGLE_ACCOUNT, null);

        if(accountName != null){
            mGoogleAuthTokenService.GetAndUseAuthToken(getGoogleAuthParmaters(accountName, ProgressHelper));
        }
    }

    private void populateAutoComplete() {
        getLoaderManager().initLoader(0, null, this);
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void validateLogin() {
        Log.d(TAG, "Beginning validateLogin");
        if (mUserLoginTask != null) {
            return;
        }

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !LoginValidator.isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        } else {
            mPasswordView.setError(null);
        }

        // Check for a valid email address.
        if (email.isEmpty()) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!LoginValidator.isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        } else {
            mEmailView.setError(null);
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {

            if(mRememberMe.isChecked()){
                mGeoSharedPrefs.add(GeoSharedPreferences.LOGIN_EMAIL, email);
                mGeoSharedPrefs.add(GeoSharedPreferences.LOGIN_PASSWORD, password);
                mGeoSharedPrefs.add(GeoSharedPreferences.LOGIN_REMEMBER, true);

                mGeoSharedPrefs.apply();
            } else {
                mGeoSharedPrefs.remove(GeoSharedPreferences.LOGIN_EMAIL);
                mGeoSharedPrefs.remove(GeoSharedPreferences.LOGIN_PASSWORD);
                mGeoSharedPrefs.remove(GeoSharedPreferences.LOGIN_REMEMBER);

                mGeoSharedPrefs.commit();
            }

            mAuthentication.emailLoginAttempt(email, password);
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            mAnalytics.trackClick(new GoogleAnalyticEvent().LoginAttempt());
        }
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    public void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    /**
     * Check if the device supports Google Play Services.  It's best
     * practice to check first rather than handling this as an error case.
     *
     * @return whether the device supports Google Play Services
     */
    private boolean supportsGooglePlayServices() {
        return GooglePlayServicesUtil.isGooglePlayServicesAvailable(this) ==
                ConnectionResult.SUCCESS;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this,
                // Retrieve data rows for the device user's 'profile' contact.
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,

                // Select only email addresses.
                ContactsContract.Contacts.Data.MIMETYPE +
                        " = ?", new String[]{ContactsContract.CommonDataKinds.Email
                .CONTENT_ITEM_TYPE},

                // Show primary email addresses first. Note that there won't be
                // a primary email address if the user hasn't specified one.
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        List<String> emails = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            emails.add(cursor.getString(ProfileQuery.ADDRESS));
            cursor.moveToNext();
        }

        addEmailsToAutoComplete(emails);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }

    @Override
    public void onPostExecute(Boolean success) {
        mUserLoginTask = null;

        showProgress(false);

        if (success) {
            Intent mainActivityIntent = new Intent(this, MainActivity.class);

            String authtoken = (application.getAuthToken() == null) ? "fakeAuthToken" : application.getAuthToken();

            // TODO: add logic checking login
            mainActivityIntent.putExtra("authToken", authtoken);
            startActivity(mainActivityIntent);

            finish();
        } else {
            mPasswordView.setError(getString(R.string.error_incorrect_password));
            mPasswordView.requestFocus();
        }
    }

    @Override
    public void onCancelled() {
        mUserLoginTask = null;

        showProgress(false);
    }

    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }

    private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(LoginActivity.this,
                        android.R.layout.simple_dropdown_item_1line, emailAddressCollection);

        mEmailView.setAdapter(adapter);
    }

    //region Helpers
    protected Boolean hasAuthToken() {
        SharedPreferences appState = application.getAppState();

        String geoAuthToken = appState.getString(application.getAppContext().getString(R.string.auth_token), null);

        return geoAuthToken == null;
    }
    //endregion

}