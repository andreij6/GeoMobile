package com.geospatialcorporation.android.geomobile.models.Document;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import com.geospatialcorporation.android.geomobile.R;
import com.geospatialcorporation.android.geomobile.models.Folders.Folder;
import com.geospatialcorporation.android.geomobile.models.Interfaces.ITreeEntity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class Document implements Parcelable, ITreeEntity {

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
        return Ext;
    }

    public void setExtension(String extension) {
        Ext = extension;
    }
    public Folder getParentFolder() {
        return ParentFolder;
    }

    public void setParentFolder(Folder parentFolder) {
        ParentFolder = parentFolder;
    }
    //endregion

    //region Properties
    private int Id;
    private long Size;
    private String Name;
    private String UploadTime;
    private String MimeType;
    private String Ext;

    private Folder ParentFolder;
    //endregion

    public Document(){

    }

    //region Parcelable Contract
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
        dest.writeString(Ext);
    }

    private Document(Parcel in) {
        Size = in.readLong();
        Id = in.readInt();
        Name = in.readString();
        UploadTime = in.readString();
        MimeType = in.readString();
        Ext = in.readString();
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

    //endregion

    //region Methods & Intent String
    public String getNameWithExt() {
        return Name + Ext.toLowerCase();
    }

    public static String INTENT = "Document Intent";

    public int getFileTypeDrawable(Boolean white) {
        List<String> word = Arrays.asList(".docx", ".docm", ".dotx", ".dotm", ".docb");
        List<String> excel = Arrays.asList(".xls", ".xlt", ".xlm", ".xlsx", ".xltx", ".xltm", ".xlsb", ".xla", ".xlam", ".xll", ".xlw");
        List<String> powerpoint = Arrays.asList(".ppt", ".pot", ".pps", ".pptx");
        List<String> images = Arrays.asList(".jpg", ".png", ".jpeg");


        String lowerExt = getExtension().toLowerCase();

        if(word.contains(lowerExt)){
            //if(white){
            //    return R.drawable.ic_file_word_white_24dp;
            //}
            return R.drawable.ic_file_word_grey600_24dp;
        }

        if(excel.contains(lowerExt)){
            //if(white){
            //    return R.drawable.ic_file_excel_white_24dp;
            //}
            return R.drawable.ic_file_excel_grey600_24dp;
        }

        if(powerpoint.contains(lowerExt)){
            //if(white){
            //    return R.drawable.ic_file_powerpoint_white_24dp;
            //}
            return R.drawable.ic_file_powerpoint_grey600_24dp;
        }

        if(images.contains(lowerExt)){
            if(white){
                return R.drawable.ic_file_image_grey600_24dp;
            }
            return R.drawable.ic_file_image_grey600_24dp;
        }

        if (lowerExt.equals(".pdf")) {
            if(white){
                return R.drawable.ic_file_pdf_box_grey600_24dp;
            }
            return R.drawable.ic_file_pdf_box_grey600_24dp;
        }

        if(white){
            return R.drawable.ic_file_document_box_grey600_24dp;
        }
        return R.drawable.ic_file_document_box_grey600_24dp;
    }

    @Override
    public Bundle toBundle() {
        Bundle b = new Bundle();
        b.putParcelable(INTENT, this);
        return b;
    }
    //endregion

}
