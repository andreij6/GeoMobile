package com.geospatialcorporation.android.geomobile.models;

public class PermissionsSaveRequest {
    //region Getters & Setters
    public Integer getRoleId() {
        return RoleId;
    }

    public void setRoleId(Integer roleId) {
        RoleId = roleId;
    }

    public Integer getAccessLevel() {
        return AccessLevel;
    }

    public void setAccessLevel(Integer accessLevel) {
        AccessLevel = accessLevel;
    }
    //endregion

    Integer RoleId;
    Integer AccessLevel;
}
