package com.geospatialcorporation.android.geomobile.models;

/**
 * Created by andre on 6/8/2015.
 */
public class NodeDetailsVm {

    //region Getters & Setters
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

    String CreateUser;
    String CreateDateTime;
    String UpdateUser;
    String UpdateDateTime;

}
