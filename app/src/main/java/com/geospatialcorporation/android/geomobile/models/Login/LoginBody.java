package com.geospatialcorporation.android.geomobile.models.Login;

import android.os.Parcel;
import android.os.Parcelable;

public class LoginBody implements Parcelable {
    //region Getters & Setters
    public int getLoginAttemptId() { return LoginAttemptId; }
    public void setLoginAttemptId(int loginAttemptId) { LoginAttemptId = loginAttemptId; }

    public String getEmail() { return Email; }
    public void setEmail(String email) { Email = email; }

    public String setPassword() { return Password; }
    public void setPassword(String password) { Password = password; }
    //endregion

    //region properties
    public int LoginAttemptId;
    public String Email;
    public String Password;
    //endregion

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(LoginAttemptId);
        dest.writeString(Email);
        dest.writeString(Password);
    }

    private LoginBody(Parcel in) {
        LoginAttemptId = in.readInt();
        Email = in.readString();
        Password = in.readString();
    }

    public LoginBody(int loginAttemptId, String email, String password) {
        LoginAttemptId = loginAttemptId;
        Email = email;
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
