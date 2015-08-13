package com.geospatialcorporation.android.geomobile.models.Login;

import android.os.Parcel;
import android.os.Parcelable;

public class LoginBody implements Parcelable {
    //region Getters & Setters
    public String getLoginAttemptId() { return LoginAttemptId; }
    public void setLoginAttemptId(String loginAttemptId) { LoginAttemptId = loginAttemptId; }

    public String getUsername() { return Username; }
    public void setUsername(String username) { Username = username; }

    public String getPassword() { return Password; }
    public void setPassword(String password) { Password = password; }
    //endregion

    //region properties
    public String LoginAttemptId;
    public String Username;
    public String Password;
    //endregion

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(LoginAttemptId);
        dest.writeString(Username);
        dest.writeString(Password);
    }

    private LoginBody(Parcel in) {
        LoginAttemptId = in.readString();
        Username = in.readString();
        Password = in.readString();
    }

    public LoginBody(String loginAttemptId, String email, String password) {
        LoginAttemptId = loginAttemptId;
        Username = email;
        Password = password;
    }

    public static final Parcelable.Creator<LoginBody> CREATOR = new Parcelable.Creator<LoginBody>() {
        @Override
        public LoginBody createFromParcel(Parcel source) {
            return new LoginBody(source);
        }

        @Override
        public LoginBody[] newArray(int size) {
            return new LoginBody[size];
        }
    };
}
