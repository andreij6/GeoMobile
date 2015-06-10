package com.geospatialcorporation.android.geomobile.models.Folders;

/**
 * Created by andre on 6/9/2015.
 */
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
    //endregion

    Boolean IsFixed;
    String RoleName;
    Integer InheritedFolderId;
    Integer InheritedAccessLevel;
    Integer RoleId;
}
