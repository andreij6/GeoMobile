package com.geospatialcorporation.android.geomobile.models.Folders;

/**
 * Created by andre on 6/9/2015.
 */
public class FolderDetailsResponse {
    Integer FileCount;
    Integer FileSize;
    String CreateUser;
    String CreateDateTime;
    String UpdateUser;
    String UpdateDateTime;

    //region Getters & Setters
    public Integer getFileCount() {
        return FileCount;
    }

    public void setFileCount(Integer fileCount) {
        FileCount = fileCount;
    }

    public Integer getFileSize() {
        return FileSize;
    }

    public void setFileSize(Integer fileSize) {
        FileSize = fileSize;
    }

    public String getCreateUser() {
        return CreateUser;
    }

    public void setCreateUser(String createUser) {
        CreateUser = createUser;
    }

    public String getCreateDateTime() {
        return CreateDateTime;
    }

    public void setCreateDateTime(String createDateTime) {
        CreateDateTime = createDateTime;
    }

    public String getUpdateUser() {
        return UpdateUser;
    }

    public void setUpdateUser(String updateUser) {
        UpdateUser = updateUser;
    }

    public String getUpdateDateTime() {
        return UpdateDateTime;
    }

    public void setUpdateDateTime(String updateDateTime) {
        UpdateDateTime = updateDateTime;
    }
    //endregion

}
