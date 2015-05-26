package com.geospatialcorporation.android.geomobile.models.Library;

import android.os.Parcel;
import android.os.Parcelable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Document implements Parcelable {

    //region Getters & Setters
    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public long getSize() {
        return Size;
    }

    public void setSize(long size) {
        Size = size;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getUploadTime() {
        return UploadTime;
    }

    public void setUploadTime(String uploadTime) {
        UploadTime = uploadTime;
    }

    public String getMimeType() {
        return MimeType;
    }

    public void setMimeType(String mimeType) {
        MimeType = mimeType;
    }

    public String getExtension() {
        return Extension;
    }

    public void setExtension(String extension) {
        Extension = extension;
    }
    //endregion

    //region Properties
    private int Id;
    private long Size;
    private String Name;
    private String UploadTime;
    private String MimeType;
    private String Extension;
    //endregion

    public Date getFormattedDate() {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/mm/yyyy h:mm a");

        Date date = null;

        try {
            date = formatter.parse(UploadTime);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return date;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(Size);
        dest.writeInt(Id);
        dest.writeString(Name);
        dest.writeString(UploadTime);
        dest.writeString(MimeType);
        dest.writeString(Extension);
    }

    private Document(Parcel in) {
        Size = in.readLong();
        Id = in.readInt();
        Name = in.readString();
        UploadTime = in.readString();
        MimeType = in.readString();
        Extension = in.readString();
    }

    public static final Parcelable.Creator<Document> CREATOR = new Parcelable.Creator<Document>() {

        @Override
        public Document createFromParcel(Parcel source) {
            return new Document(source);
        }

        @Override
        public Document[] newArray(int size) {
            return new Document[size];
        }
    };
}
