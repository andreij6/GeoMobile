package com.geospatialcorporation.android.geomobile.library.util.rest;

import com.geospatialcorporation.android.geomobile.library.constants.Domains;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;

/**
 * Created by andre on 4/7/2015.
 */
public class RestService {
    //region Constants
    private final static String BASE_URL = Domains.DEVELOPMENT;
    //endregion

    //region Getters & Setters
    public String getHttpAction() {
        return mHttpAction;
    }

    public void setHttpAction(String httpAction) {
        mHttpAction = httpAction;
    }

    public String getEndPoint() {
        return mEndPoint;
    }

    public void setEndPoint(String endPoint) {
        mEndPoint = endPoint;
    }

    public Callback getCallback() {
        return mCallback;
    }

    public void setCallback(Callback callback) {
        mCallback = callback;
    }

    public AuthHeader getAuth() {
        return mAuth;
    }

    public void setAuth(AuthHeader auth) {
        mAuth = auth;
    }

    public RequestBody getRequestBody() {
        return mRequestBody;
    }

    public void setRequestBody(RequestBody rb) {
        mRequestBody = rb;
    }
    //endregion

    //region Properties
    private String mHttpAction;
    private String mEndPoint;
    private Callback mCallback;
    private AuthHeader mAuth;
    private RequestBody mRequestBody;
    private OkHttpClient mOkHttpClient;
    //endregion

    //region Constructors
    public RestService(String action,
                       String endPoint,
                       Callback callback,
                       AuthHeader auth,
                       RequestBody rb){
        mHttpAction = action;
        mEndPoint = endPoint;
        mCallback = callback;
        mAuth = auth;
        mRequestBody = rb;
        mOkHttpClient = new OkHttpClient();
    }
    public RestService(){
        mOkHttpClient = new OkHttpClient();
    }
    //endregion

    //region Methods
    public void Send(){
        Request.Builder builder = new Request.Builder().url(BASE_URL + mEndPoint)
                                                    .header(mAuth.getName(), mAuth.getValue());

        if(HasBody())
            SetAction(builder);

        Request request = builder.build();

        Call call = mOkHttpClient.newCall(request);

        call.enqueue(mCallback);
    }

    protected void SetAction(Request.Builder builder){

    }

    protected Boolean HasBody(){
        return mRequestBody != null;
    }
    //endregion

    public static class Builder{

        private String mHttpAction;
        private String mEndPoint;
        private Callback mCallback;
        private AuthHeader mAuth;
        private RequestBody mRequestBody;

        //region Methods
        public Builder(){
            mHttpAction = "GET";
            mRequestBody = null;
        }

        public Builder endPoint(String endpoint){
            mEndPoint = endpoint;

            return this;
        }

        public Builder setAuthToken(String value){
            AuthHeader auth = new AuthHeader(value);

            mAuth = auth;
            return this;
        }

        public Builder callBack(Callback callback){
            mCallback = callback;

            return this;
        }

        public RestService build(){
            return new RestService(
                        mHttpAction, mEndPoint,
                        mCallback,mAuth, mRequestBody);
        }
        //endregion
    }

    public static class AuthHeader {

        //region Getters & Setters
        public String getName() {
            return mName;
        }

        public void setName(String name) {
            mName = name;
        }

        public String getValue() {
            return mValue;
        }

        public void setValue(String value) {
            mValue = value;
        }
        //endregion

        //region Properties
        private String mName;
        private String mValue;
        //endregion

        //region Constructors
        public AuthHeader(String value){
            mName = "Authorization";
            mValue = value;
        }

        public AuthHeader(){}
        //endregion
    }
}
