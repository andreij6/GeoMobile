package com.geospatialcorporation.android.geomobile.library.util;

import android.content.Context;
import android.util.Base64;
import android.util.Log;

import com.afollestad.materialdialogs.MaterialDialog;
import com.geospatialcorporation.android.geomobile.R;
import com.geospatialcorporation.android.geomobile.application;
import com.geospatialcorporation.android.geomobile.library.DI.Analytics.Interfaces.IGeoAnalytics;
import com.geospatialcorporation.android.geomobile.library.DI.Authentication.Implementations.AuthTokenRetriever;
import com.geospatialcorporation.android.geomobile.library.DI.ErrorHandler.Interfaces.IGeoErrorHandler;
import com.geospatialcorporation.android.geomobile.library.helpers.ProgressDialogHelper;
import com.geospatialcorporation.android.geomobile.library.rest.LoginService;
import com.geospatialcorporation.android.geomobile.models.Login.ErrorResponse;
import com.geospatialcorporation.android.geomobile.models.Login.LoginBody;
import com.geospatialcorporation.android.geomobile.models.Login.LoginBodyJsonSerializer;
import com.geospatialcorporation.android.geomobile.ui.LoginActivity;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.apache.commons.io.Charsets;

import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.RSAPublicKeySpec;
import java.util.List;

import javax.crypto.Cipher;
import javax.crypto.Mac;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Header;
import retrofit.client.Response;
import retrofit.mime.TypedByteArray;
import retrofit.mime.TypedString;

public class Authentication {
    private final static String TAG = Authentication.class.getSimpleName();

    private RestAdapter mRestAdapter;
    private LoginService mService;
    private MaterialDialog mProgressHelper;
    private MaterialDialog mFailureHelper;
    private LoginActivity mContext;

    protected IGeoAnalytics mAnalytics;
    protected IGeoErrorHandler mErrorHandler;

    /** These are constant forever. **/
    private static String fixedKey = "oAbNi0ZTjacrnbY4kASQ2u3ZdSuWxBvebzelCvLo221Bc";
    private static byte[] AESKey = {(byte)41, (byte)215, (byte)158, (byte)196, (byte)35, (byte)207, (byte)59, (byte)115, (byte)124, (byte)79, (byte)67, (byte)156, (byte)144, (byte)20, (byte)246, (byte)202, (byte)66, (byte)71, (byte)211, (byte)231, (byte)228, (byte)14, (byte)104, (byte)41, (byte)118, (byte)251, (byte)185, (byte)64, (byte)252, (byte)33, (byte)29, (byte)42};
    private static byte[] AESIV = {(byte)37, (byte)28, (byte)238, (byte)138, (byte)84, (byte)112, (byte)161, (byte)12, (byte)159, (byte)115, (byte)52, (byte)63, (byte)240, (byte)14, (byte)82, (byte)171};

    /** These variables change per mobile version number. Accessible at: (?)geounderground.com/admin/mobile **/
    public final static String version = "1.0.0";
    public final static String versionId = "5AC2F6BD-AA2D-402F-B4E0-DFC3545602BC";
    private static BigInteger keyOneModulus = new BigInteger(1, Base64.decode("4UTjXI3IEnFyF5pfSUrImOl6R3OAUzzwtC/emrR/A+2k4qKqnfPlQy7fmIPeYhDAvuhZlbSudmMU+hEm7lMhyIpTBIDJeyHG0TnSx0/ODsBwXEM4NBLAXaQKj2mRmJ2PNM/FKjOYWjGOZZBHKO4EkRC1yPYU9AOR+qYy9eC1iHE=", Base64.NO_WRAP));
    private static BigInteger keyOneExponent = new BigInteger(1, Base64.decode("AQAB", Base64.NO_WRAP));
    private static byte[] keyTwo = null;

    /** This changes (or can change) per user device **/
    public final static String deviceId = "defaultDeviceId";

    /** These change with each login attempt. **/
    private String mUsername;
    private String mPassword;
    private String mLoginAttemptId;
    private byte[] mLoginAttemptSalt;
    private String mBody;
    //endregion

    public Authentication (LoginActivity context, MaterialDialog progressHelper, MaterialDialog failureHelper) {
        mContext = context;
        mProgressHelper = progressHelper;
        mFailureHelper = failureHelper;

        try {
            keyTwo = "Bj+nS+pODStVVi8nODOLAA==".getBytes("UTF-8");
        } catch (Exception e) { Log.e(TAG, e.getMessage()); }

        mRestAdapter = new RestAdapter.Builder()
                .setEndpoint(application.getDomain())
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();
        mService = mRestAdapter.create(LoginService.class);

        mAnalytics = application.getAnalyticsComponent().provideGeoAnalytics();
        mErrorHandler = application.getErrorsComponent().provideErrorHandler();
    }

    // Get login attempt information
    public void emailLoginAttempt(String username, String password) {
        if (mProgressHelper != null) {
            mProgressHelper.show();
        }

        mUsername = username;
        mPassword = password;

        Callback<Response> callback = new Callback<Response>() {
            @Override
            public void success(Response result, Response response) {
                String responseBody = new String(((TypedByteArray) result.getBody()).getBytes());

                Log.d(TAG, "emailLoginStart success. Response mBody: " + responseBody);

                parseStartString(responseBody);

                postEmailLogin();
            }

            @Override
            public void failure(RetrofitError retrofitError) {
                Log.d(TAG, "emailLoginStart failure");
                Log.d(TAG, "Message: " + retrofitError.getMessage());
                Log.e(TAG, "Error calling: " + retrofitError.getUrl());

                Response response = retrofitError.getResponse();

                if (response != null && response.getBody() != null) {
                    Log.e(TAG, "emailLoginStart failure: " + new String(((TypedByteArray) retrofitError.getResponse().getBody()).getBytes()));
                }

                if (mProgressHelper != null) {
                    mProgressHelper.hide();

                    try {
                        Gson gson = new Gson();
                        ErrorResponse errorResponse = gson.fromJson(new String(((TypedByteArray) retrofitError.getResponse().getBody()).getBytes()), ErrorResponse.class);
                        mFailureHelper.setContent(errorResponse.getMessage());
                        mFailureHelper.show();
                    } catch (Exception e) {
                        Log.e(TAG, "emailLoginStart failure failure: error showing error message");

                        mAnalytics.sendException(e);
                    }
                }
            }
        };

        // Returns {attemptId}.{base64Key}
        mService.start(new TypedString(fixedKey), callback);
    }

    private void postEmailLogin() {
        Log.d(TAG, "Beginning postEmailLogin");
        LoginBody loginBody = new LoginBody(mLoginAttemptId, mUsername, mPassword);

        // required because order of properties matters for hash
        Gson gson = new GsonBuilder().registerTypeAdapter(LoginBody.class, new LoginBodyJsonSerializer()).create();
        mBody = gson.toJson(loginBody);
        String encryptedJSONBody = encryptRSA(mBody);

        Callback<Response> callback = new Callback<Response>() {
            @Override
            public void success(Response result, Response response) {
                Log.d(TAG, "postEmailLogin success");
                List<Header> headers = response.getHeaders();

                boolean success = false;
                for(Header header : headers) {
                    Log.d(TAG, "Header " + header.getName());
                    Log.d(TAG, header.getName() + " Value " + header.getValue());

                    if (header.getName().equals("X-WebToken")) {
                        application.setAuthToken(header.getValue());

                        success = true;
                    }

                }

                if (success) {
                    new AuthTokenRetriever().getCurrentClient(mContext, mProgressHelper);
                }
            }

            @Override
            public void failure(RetrofitError retrofitError) {
                Log.e(TAG, "postEmailLogin failure: " + new String(((TypedByteArray) retrofitError.getResponse().getBody()).getBytes()));

                if (mProgressHelper != null) {
                    mProgressHelper.hide();
                }

                try {
                    Gson gson = new Gson();
                    ErrorResponse errorResponse = gson.fromJson(new String(((TypedByteArray) retrofitError.getResponse().getBody()).getBytes()), ErrorResponse.class);
                    mFailureHelper.setContent(errorResponse.getMessage());
                    mFailureHelper.show();
                } catch (Exception e) {
                    Log.e(TAG, "postEmailLogin failure failure: error showing error message");

                    mAnalytics.sendException(e);
                }
            }
        };

        String signature = getLatestSignature();

        Log.d(TAG, "Login Attempt, Email, Password: " + mLoginAttemptId + " | " + mUsername + " | " + mPassword);
        try { Log.d(TAG, "Login Attempt Salt: " + new String(mLoginAttemptSalt, "UTF-8")); } catch (Exception e) { Log.d(TAG, "Couldn't convert mLoginAttemptSalt to string."); }
        Log.d(TAG, "JSON Body: " + mBody);
        Log.d(TAG, "Encrypted JSON Body: " + encryptedJSONBody);
        Log.d(TAG, "Signature: " + signature);

        mService.login(getLatestSignature(), new TypedString(encryptedJSONBody), callback);
    }

    //region Helpers
    private String getLatestSignature() {
        Log.d(TAG, "Beginning getLatestSignature");
        String returnString = null;
        try {
            Log.d(TAG, "HmacSHA256 salt1: " + new String(mLoginAttemptSalt, "UTF-8"));
            Log.d(TAG, "HmacSHA256 salt2: " + new String(keyTwo, "UTF-8"));

            String saltKeyTwo = Base64.encodeToString(concat(Base64.decode(mLoginAttemptSalt, Base64.NO_WRAP), Base64.decode(keyTwo, Base64.NO_WRAP)), Base64.NO_WRAP);

            String preHash = versionId + "." + deviceId + "." + mBody;

            Log.d(TAG, "HmacSHA256 on: " + preHash);
            Log.d(TAG, "HmacSHA256 secret: " + saltKeyTwo);

            returnString = testHmac(preHash, saltKeyTwo);

            Log.d(TAG, "HmacSHA256 final: " + returnString);
        } catch (Exception e) {
            Log.e(TAG, "getLatestSignature error: " + e.getMessage());

            mAnalytics.sendException(e);
        }

        return returnString;
    }

    private String encryptRSA(String content) {
        Log.d(TAG, "Beginning encryptRSA");
        String returnString = null;
        try {
            KeyFactory factory = KeyFactory.getInstance("RSA");

            RSAPublicKeySpec keySpec = new RSAPublicKeySpec(keyOneModulus, keyOneExponent);

            PublicKey publicKey = factory.generatePublic(keySpec);

            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);

            byte[] encrypted = cipher.doFinal(content.getBytes("UTF-8"));

            returnString = Base64.encodeToString(encrypted, Base64.NO_WRAP);
        } catch (Exception e) {
            Log.e(TAG, "encryptRSA error: " + e.getMessage());

            mAnalytics.sendException(e);
        }

        Log.d(TAG, "Completed encryptRSA");
        return returnString;
    }

    private static String decryptAES(String base64Content, boolean returnBase64) {
        Log.d(TAG, "Beginning decryptAES");
        String returnString = null;
        try {
            byte[] base64ContentBytes = Base64.decode(base64Content, Base64.NO_WRAP);

            SecretKeySpec keySpec = new SecretKeySpec(AESKey, "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
            cipher.init(Cipher.DECRYPT_MODE, keySpec, new IvParameterSpec(AESIV));
            byte[] decrypted = cipher.doFinal(base64ContentBytes);

            returnString = (returnBase64) ? new String(decrypted, "UTF-8") : Base64.encodeToString(decrypted, Base64.NO_WRAP);
        } catch (Exception e) {
            Log.e(TAG, "decryptAES error: " + e.getMessage());
        }

        Log.d(TAG, "Completed decryptAES");
        return returnString;
    }

    public static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len/2];

        for(int i = 0; i < len; i+=2){
            data[i/2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) + Character.digit(s.charAt(i+1), 16));
        }

        return data;
    }

    final protected static char[] hexArray = "0123456789ABCDEF".toCharArray();
    public static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for ( int j = 0; j < bytes.length; j++ ) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

    private void parseStartString(String theStartString) {
        String[] loginAttemptStrings = theStartString.split("\\.");

        if (loginAttemptStrings.length != 2) {
            Log.d(TAG, "emailLoginStart error: Too many strings after split.");
        }

        try {
            String loginAttemptIdString = loginAttemptStrings[0];
            Log.d(TAG, "emailLoginStart setting mLoginAttemptId to: " + loginAttemptIdString);
            mLoginAttemptId = loginAttemptIdString;

            Log.d(TAG, "emailLoginStart setting mLoginAttemptSalt to: " + loginAttemptStrings[1]);
            mLoginAttemptSalt = loginAttemptStrings[1].getBytes("UTF-8");
        } catch (Exception e) {
            Log.d(TAG, "emailLoginStart error: " + e.getMessage());

            mAnalytics.sendException(e);
        }
    }

    private String testHmac(String data, String secretAccessKey) {
        String returnString = "";
        try {
            byte[] secretKey = secretAccessKey.getBytes("UTF-8");
            Log.d(TAG,"Secret key");
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < secretKey.length; i++) {
                int test = secretKey[i];
                sb.append(test);
                sb.append(',');
            }
            Log.d(TAG, sb.toString());

            Log.d(TAG,"data");
            sb = new StringBuilder();
            byte[] bytes = data.getBytes("UTF-8");
            for (int i = 0; i < bytes.length; i++) {
                int test = bytes[i];
                sb.append(test);
                sb.append(',');
            }
            Log.d(TAG, sb.toString());

            SecretKeySpec signingKey = new SecretKeySpec(secretKey, "HmacSHA256");
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(signingKey);

            byte[] rawHmac = mac.doFinal(bytes);
            Log.d(TAG, "testHmac started");

            returnString = new String(Base64.encode(rawHmac, Base64.NO_WRAP), Charsets.UTF_8);

            Log.d(TAG, returnString);
        } catch (Exception e) {
            Log.e(TAG, "testHmac error");

            mAnalytics.sendException(e);
        }
        return returnString;
    }

    public byte[] concat(byte[] a, byte[] b) {
        int aLen = a.length;
        int bLen = b.length;
        byte[] c= new byte[aLen+bLen];
        System.arraycopy(a, 0, c, 0, aLen);
        System.arraycopy(b, 0, c, aLen, bLen);
        return c;
    }
    // end region
}
