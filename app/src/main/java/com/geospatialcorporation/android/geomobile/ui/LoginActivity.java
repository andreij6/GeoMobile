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
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.geospatialcorporation.android.geomobile.BuildConfig;
import com.geospatialcorporation.android.geomobile.R;
import com.geospatialcorporation.android.geomobile.application;
import com.geospatialcorporation.android.geomobile.library.DI.Analytics.Models.GoogleAnalyticEvent;
import com.geospatialcorporation.android.geomobile.library.DI.Tasks.Interfaces.IUserLoginTask;
import com.geospatialcorporation.android.geomobile.library.constants.GeoSharedPreferences;
import com.geospatialcorporation.android.geomobile.library.rest.LoginService;
import com.geospatialcorporation.android.geomobile.library.util.LoginValidator;
import com.geospatialcorporation.android.geomobile.models.Login.LoginBody;
import com.geospatialcorporation.android.geomobile.ui.Interfaces.IFullExecuter;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.SignInButton;
import com.google.gson.Gson;

import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.RSAPublicKeySpec;
import java.util.ArrayList;
import java.util.List;

import javax.crypto.Cipher;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Header;
import retrofit.client.Response;
import retrofit.mime.TypedByteArray;

public class LoginActivity extends GoogleApiActivity implements LoaderCallbacks<Cursor>, IFullExecuter<Boolean> {
    private final static String TAG = LoginActivity.class.getSimpleName();

    private RestAdapter loginRestAdapter;
    private LoginService loginService;

    /** These are constant forever. **/
    private static String fixedKey = "oAbNi0ZTjacrnbY4kASQ2u3ZdSuWxBvebzelCvLo221Bc";
    private static byte[] AESKey = {(byte)41, (byte)215, (byte)158, (byte)196, (byte)35, (byte)207, (byte)59, (byte)115, (byte)124, (byte)79, (byte)67, (byte)156, (byte)144, (byte)20, (byte)246, (byte)202, (byte)66, (byte)71, (byte)211, (byte)231, (byte)228, (byte)14, (byte)104, (byte)41, (byte)118, (byte)251, (byte)185, (byte)64, (byte)252, (byte)33, (byte)29, (byte)42};
    private static byte[] AESIV = {(byte)37, (byte)28, (byte)238, (byte)138, (byte)84, (byte)112, (byte)161, (byte)12, (byte)159, (byte)115, (byte)52, (byte)63, (byte)240, (byte)14, (byte)82, (byte)171};

    /** These variables change per mobile version number. Accessible at: (?)geounderground.com/admin/mobile **/
    public final static String version = "1.0.0";
    public final static String versionId = "5AC2F6BD-AA2D-402F-B4E0-DFC3545602BC";
    private static BigInteger keyOneModulus = new BigInteger(1, Base64.decode("leF2gRR62cjRujE0yRzTFThdXMcGmLkS4EBAowTmBqXsXUd01dYFKFnusTNf8Jm2irRzL/6viqKawWnCwbNYlC62Xezba4z9/0bpf+6Rugu19pez+LpwxMV9B6O1orw2LiwkHgamjC/vg9shXCzQ78B07AEpP5bdVqIDTTvPcMHm4xuYffMOgPhyfYKyRsEW".getBytes(), Base64.DEFAULT));
    private static BigInteger keyOneExponent = new BigInteger(1, Base64.decode("gOfjk9sgx+xJ6xd0V5X80A==".getBytes(), Base64.DEFAULT));
    private static String keyTwo = "FJvcDaSRQ5sOnd3bbCA9pYhTe4hEj5WuCxYsKgAUDfY=";

    /** This changes (or can change) per user device **/
    public final static String deviceId = "debugDeviceId";

    /** These change with each login attempt. **/
    private int loginAttemptId;
    private String loginAttemptSalt;
    private String body;

    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
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

        emailLoginStart();
    }
    //endregion

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (hasAuthToken()) {
            setContentView(R.layout.activity_login);
            ButterKnife.inject(this);
            mSignUpLink.setMovementMethod(LinkMovementMethod.getInstance());
        }

        if (!supportsGooglePlayServices()) {
            // Don't offer G+ sign in if the app's version is too low to support Google Play
            // Services.
            mPlusSignInButton.setVisibility(View.GONE);
            return;
        }

        mUserLoginTask = null;

        populateAutoComplete();
        
        AttemptAutomaticLogin();

        if (BuildConfig.DEBUG) {
            mEmailView.setText("jon.shaffer@geospatialcorporation.com");
            mPasswordView.setText("f5eHXqWEGp1W");
        }

        loginRestAdapter = new RestAdapter.Builder()
                .setEndpoint(application.getDomain())
                .build();
        loginService = loginRestAdapter.create(LoginService.class);

        mPlusSignInButton.requestFocus();

        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.password || id == EditorInfo.IME_NULL) {
                    emailLoginStart();
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

    private void emailLoginStart() {
        Log.d(TAG, "Beginning emailLoginStart");
        String[] loginAttemptStrings = null;

        Callback<String> callback = new Callback<String>() {
            @Override
            public void success(String loginAttemptString, Response response) {
                Log.d(TAG, "emailLoginStart success");
                String[] loginAttemptStrings = loginAttemptString.split(".");

                loginAttemptId = Integer.getInteger(loginAttemptStrings[0]);
                loginAttemptSalt = loginAttemptStrings[1];

                validateLogin();
            }

            @Override
            public void failure(RetrofitError retrofitError) {
                Log.d(TAG, "Mobile header: X-GeoUnderground: Version " + LoginActivity.version + ';' + LoginActivity.versionId + ';' + LoginActivity.deviceId);
                Log.d(TAG, "Body: " + fixedKey);
                Log.e(TAG, "emailLoginStart failure: " + new String(((TypedByteArray)retrofitError.getResponse().getBody()).getBytes()));
                // TODO: Implement
            }
        };

        // Returns {attemptId}.{base64Key}
        loginService.start(fixedKey, callback);
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

        // TODO: Remove this horrible testing code
        String authtoken = (application.getAuthToken() == null) ? "fakeAuthToken" : application.getAuthToken();
        //mainActivityIntent.putExtra("authToken", authtoken);
        //startActivity(mainActivityIntent);

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
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            mAnalytics.trackClick(new GoogleAnalyticEvent().LoginAttempt());

            Toast.makeText(this, "Not Implemented - Try Google Sign-in", Toast.LENGTH_LONG).show();

            postEmailLogin(email, password);
        }
    }

    private void postEmailLogin(String email, String password) {
        Log.d(TAG, "Beginning postEmailLogin");
        LoginBody loginBody = new LoginBody(loginAttemptId, email, password);

        body = new Gson().toJson(loginBody);
        String encryptedJSONBody = encryptRSA(body);

        Callback callback = new Callback() {
            @Override
            public void success(Object o, Response response) {
                Log.d(TAG, "postEmailLogin success");
                List<Header> headers = response.getHeaders();

                for(Header header : headers) {
                    if (header.getName().equals("X-WebToken")) {
                        application.setAuthToken(header.getValue());
                        break;
                    }
                }
            }

            @Override
            public void failure(RetrofitError retrofitError) {
                Log.e(TAG, "postEmailLogin failure: " + new String(((TypedByteArray) retrofitError.getResponse().getBody()).getBytes()));
                // TODO: Implement
            }
        };

        loginService.login(getLatestSignature(), encryptedJSONBody, callback);
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

    private String getLatestSignature() {
        Log.d(TAG, "Beginning getLatestSignature");
        String returnString = null;
        try {
            Mac sha256Hmac = Mac.getInstance("HmacSHA256");
            SecretKeySpec secretKey = new SecretKeySpec((loginAttemptSalt + keyTwo).getBytes(), "HmacSHA256");
            sha256Hmac.init(secretKey);
            byte[] hmacBytes = sha256Hmac.doFinal((versionId + "." + deviceId + "." + body + "|" + loginAttemptSalt + keyTwo).getBytes());

            returnString = Base64.encodeToString(hmacBytes, Base64.DEFAULT);
        } catch (Exception e) {
            Log.e(TAG, "getLatestSignature error: " + e.getMessage());
            Toast.makeText(this, "Error posting login. Try google login.", Toast.LENGTH_LONG).show();
        }

        return returnString;
    }

    private String encryptRSA(String content) {
        Log.d(TAG, "Beginning encryptRSA");
        String returnString = null;
        try {
            RSAPublicKeySpec keySpec = new RSAPublicKeySpec(keyOneModulus, keyOneExponent);
            KeyFactory factory = KeyFactory.getInstance("RSA");
            PublicKey publicKey = factory.generatePublic(keySpec);

            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            byte[] encrypted = cipher.doFinal(content.getBytes());

            returnString = Base64.encodeToString(encrypted, Base64.DEFAULT);
        } catch (Exception e) {
            Log.e(TAG, "encryptRSA error: " + e.getMessage());
            Toast.makeText(this, "Error posting login. Try google login.", Toast.LENGTH_LONG).show();
        }

        return returnString;
    }

    private String decryptAES(String base64Content, boolean returnBase64) {
        Log.d(TAG, "Beginning decryptAES");
        String returnString = null;
        try {
            byte[] contentBytes = Base64.decode(base64Content, Base64.DEFAULT);

            SecretKeySpec keySpec = new SecretKeySpec(AESKey, "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, keySpec);
            byte[] decrypted = cipher.doFinal(contentBytes);

            returnString = (returnBase64) ? new String(decrypted, "UTF-8") : Base64.encodeToString(decrypted, Base64.DEFAULT);
        } catch (Exception e) {
            Log.e(TAG, "decryptAES error: " + e.getMessage());
            Toast.makeText(this, "Error posting login. Try google login.", Toast.LENGTH_LONG).show();
        }

        return returnString;
    }

    private String encryptAES(String content) {
        Log.d(TAG, "Beginning encryptAES");
        String returnString = null;
        try {
            SecretKeySpec keySpec = new SecretKeySpec(AESKey, "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, keySpec);
            byte[] encrypted = cipher.doFinal(content.getBytes());

            returnString = Base64.encodeToString(encrypted, Base64.DEFAULT);
        } catch (Exception e) {
            Log.e(TAG, "encryptAES error: " + e.getMessage());
            Toast.makeText(this, "Error posting login. Try google login.", Toast.LENGTH_LONG).show();
        }

        return returnString;
    }
    //endregion

}