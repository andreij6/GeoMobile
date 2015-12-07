package com.geospatialcorporation.android.geomobile.models.Folders;


public class FolderPermissionsResponse {
    //region Getters & Setters
    public Boolean getIsFixed() {
        return IsFixed;
    }

    public void setIsFixed(Boolean isFixed) {
        IsFixed = isFixed;
    }

    public String getRoleName() {
        return RoleName;
    }

    public void setRoleName(String roleName) {
        RoleName = roleName;
    }

    public Integer getInheritedFolderId() {
        return InheritedFolderId;
    }

    public void setInheritedFolderId(Integer inheritedFolderId) {
        InheritedFolderId = inheritedFolderId;
    }

    public Integer getInheritedAccessLevel() {
        return InheritedAccessLevel;
    }

    public void setInheritedAccessLevel(Integer inheritedAccessLevel) {
        InheritedAccessLevel = inheritedAccessLevel;
    }

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

    public FolderPermissionsResponse(){
        AccessLevel = null;
    }

    Boolean IsFixed;
    String RoleName;
    Integer InheritedFolderId;
    Integer InheritedAccessLevel;
    Integer RoleId;
    Integer AccessLevel;
}
