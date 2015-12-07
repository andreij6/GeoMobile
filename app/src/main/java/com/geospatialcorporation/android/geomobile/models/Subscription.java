package com.geospatialcorporation.android.geomobile.models;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import com.geospatialcorporation.android.geomobile.data.tables.SubscriptionColumns;
import com.geospatialcorporation.android.geomobile.library.constants.ClientTypeCodes;

public class Subscription implements Parcelable{
    //region Getters & Setters
    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public int getType() {
        return Type;
    }

    public void setType(int type) {
        Type = type;
    }

    public String getCreated() {
        return Created;
    }

    public void setCreated(String created) {
        Created = created;
    }
    //endregion

    String Name;
    int Id;
    int Type;
    String Created;

    public static final String INTENT = "Subscription";

    public Subscription(){}

    public Subscription(String name, int id, int type, String created) {
        Name = name;
        Id = id;
        Type = type;
        Created = created;
    }

    public String getReadableType() {
        ClientTypeCodes clientTypeCodes = new ClientTypeCodes();

        return clientTypeCodes.getClientTypeName(Type);
    }

    public static Subscription fromCursor(Cursor cursor) {
        Subscription result = new Subscription();

        int createdIndex = cursor.getColumnIndex(SubscriptionColumns.CREATED);
        int nameIdx = cursor.getColumnIndex(SubscriptionColumns.NAME);
        int typeIdx = cursor.getColumnIndex(SubscriptionColumns.TYPE);
        int idIdx = cursor.getColumnIndex(SubscriptionColumns.SUBSCRIPTION_ID);

        result.setCreated(cursor.getString(createdIndex));
        result.setType(cursor.getInt(typeIdx));
        result.setName(cursor.getString(nameIdx));
        result.setId(cursor.getInt(idIdx));

        return result;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(Id);
        dest.writeInt(Type);
        dest.writeString(Created);
        dest.writeString(Name);
    }

    private Subscription(Parcel in){
        Id = in.readInt();
        Type = in.readInt();
        Created = in.readString();
        Name = in.readString();
    }

    public static final Creator<Subscription> CREATOR = new Creator<Subscription>() {
        @Override
        public Subscription createFromParcel(Parcel source) {
            return new Subscription(source);
        }

        @Override
        public Subscription[] newArray(int size) {
            return new Subscription[size];
        }
    };
}
