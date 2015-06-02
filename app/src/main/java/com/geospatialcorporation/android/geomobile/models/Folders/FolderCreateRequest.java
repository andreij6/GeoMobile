package com.geospatialcorporation.android.geomobile.models.Folders;

import com.geospatialcorporation.android.geomobile.models.Item.ItemCreateRequest;

/**
 * Created by andre on 6/1/2015.
 */
public class FolderCreateRequest extends ItemCreateRequest {
    //region Getters & Setters
    public Boolean getIsFixed() {
        return IsFixed;
    }

    public void setIsFixed(Boolean isFixed) {
        IsFixed = isFixed;
    }
    //endregion

    Boolean IsFixed;
    //PluginCodes? Plugin;

    public FolderCreateRequest(String folderName, int parentId){
        Name = folderName;
        ParentId = parentId;
        IsFixed = false;
    }
}
