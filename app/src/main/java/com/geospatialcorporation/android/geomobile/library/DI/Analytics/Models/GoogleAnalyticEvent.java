package com.geospatialcorporation.android.geomobile.library.DI.Analytics.Models;

import android.content.Context;

import com.geospatialcorporation.android.geomobile.R;
import com.geospatialcorporation.android.geomobile.application;

public class GoogleAnalyticEvent extends AnalyticEvent {

    private Context mContext;
    private String mScreenName;
    private String mLabel;
    private String mCategory;
    private String mAction;

    public GoogleAnalyticEvent(){
        mContext = application.getAppContext();
        setCategory(R.string.ui);
        setAction(R.string.clicked);
    }

    //region Gs & Ss
    public String getScreenName() {
        return mScreenName;
    }

    public String getLabel() {
        return mLabel;
    }

    public String getCategory() {
        return mCategory;
    }

    public String getAction() {
        return mAction;
    }

    public void setLabel(int labelResource) {
        mLabel = mContext.getString(labelResource);
    }

    public void setScreenName(int screenName) {
        mScreenName = mContext.getString(screenName);
    }

    public void setCategory(int categoryId){
        mCategory = mContext.getString(categoryId);
    }

    public void setAction(int actionId){
        mAction = mContext.getString(actionId);
    }
    //endregion

    //region Screen Events
    public GoogleAnalyticEvent MapScreen() {
        this.setScreenName(R.string.map_screen);
        return this;
    }

    public GoogleAnalyticEvent LayerFolderDetail() {
        this.setScreenName(R.string.layer_folder_detail_screen);
        return this;
    }

    public GoogleAnalyticEvent BookmarkScreen() {
        this.setScreenName(R.string.bookmark);
        return this;
    }

    public GoogleAnalyticEvent LibraryTreeScreen() {
        this.setScreenName(R.string.library_tree_screen);
        return this;
    }

    public GoogleAnalyticEvent DocumentDetailScreen() {
        this.setScreenName(R.string.document_detail_screen);
        return this;
    }

    public GoogleAnalyticEvent SublayersTab() {
        this.setScreenName(R.string.sublayers_tab_screen);
        return this;
    }

    public GoogleAnalyticEvent LayerDetailScreen() {
        this.setScreenName(R.string.layer_detail_tab_screen);
        return this;
    }

    public GoogleAnalyticEvent AttributeLayoutTab() {
        this.setScreenName(R.string.attribute_layout);
        return this;
    }

    public GoogleAnalyticEvent FolderPermissionsScreen() {
        this.setScreenName(R.string.folder_permissions_screen);
        return this;
    }

    public GoogleAnalyticEvent FolderDetailTab() {
        this.setScreenName(R.string.folder_detail_screen);
        return this;
    }

    public GoogleAnalyticEvent FeatureAttributesTab() {
        this.setScreenName(R.string.feature_window_attributes_tab);
        return this;
    }

    public GoogleAnalyticEvent MapInfoTab() {
        this.setScreenName(R.string.map_info_tab);
        return this;
    }

    public GoogleAnalyticEvent MapFeatureDocumentsTab() {
        this.setScreenName(R.string.map_feature_document);
        return this;
    }
    //endregion

    //region Click Events
    public GoogleAnalyticEvent LoginAttempt() {
        this.setLabel(R.string.loginAttempt);
        return this;
    }

    public GoogleAnalyticEvent DeleteDocument() {
        this.setLabel(R.string.delete_document);
        return this;
    }

    public GoogleAnalyticEvent OpenLayerDrawer() {
        this.setLabel(R.string.show_layers);
        return this;
    }

    public GoogleAnalyticEvent SignInBtn() {
        this.setLabel(R.string.GeoUndergroundSignIn);
        return this;
    }

    public GoogleAnalyticEvent GoogleSignIn() {
        this.setLabel(R.string.GoogleSignIn);
        return this;
    }

    public GoogleAnalyticEvent ShowLayer() {
        this.setLabel(R.string.showLayerCheckBox);
        return this;
    }

    public GoogleAnalyticEvent HideLayer() {
        this.setLabel(R.string.hideLayerCheckBox);
        return this;
    }

    public GoogleAnalyticEvent CurrentLocation() {
        this.setLabel(R.string.current_location);
        return this;
    }

    public GoogleAnalyticEvent ChangeBaseMap() {
        this.setLabel(R.string.change_basemap);
        return this;
    }

    public GoogleAnalyticEvent QueryModeInit() {
        this.setLabel(R.string.query_mode);
        return this;
    }

    public GoogleAnalyticEvent QuickSearchInit() {
        this.setLabel(R.string.quicksearch_mode);
        return this;
    }

    public GoogleAnalyticEvent ShowDocumentActions() {
        this.setLabel(R.string.show_document_actions);
        return this;
    }

    public GoogleAnalyticEvent DownloadDocument() {
        this.setLabel(R.string.download_file);
        return this;
    }

    public GoogleAnalyticEvent ShowLayerActions() {
        this.setLabel(R.string.show_layer_actions);
        return this;
    }

    public GoogleAnalyticEvent FolderPermissionsClick() {
        this.setLabel(R.string.folder_permission_click);
        return this;
    }

    public GoogleAnalyticEvent ShowFolderActions() {
        this.setLabel(R.string.show_folder_actions);
        return this;
    }

    public GoogleAnalyticEvent ShowAddFeatureDocumentDialog() {
        this.setLabel(R.string.show_add_feature_document_dialog);
        return this;
    }

    public GoogleAnalyticEvent Logout() {
        this.setLabel(R.string.logout_event);
        return this;
    }

    public GoogleAnalyticEvent UploadImage() {
        this.setLabel(R.string.upload_image_event);
        return this;
    }

    public GoogleAnalyticEvent CreateLayer() {
        this.setLabel(R.string.create_layer_event);
        return this;
    }

    public GoogleAnalyticEvent CreateFolder() {
        this.setLabel(R.string.create_folder_event);
        return this;
    }

    public GoogleAnalyticEvent DeleteFolder() {
        this.setLabel(R.string.delete_folder_event);
        return this;
    }

    public GoogleAnalyticEvent MapfeatureDocument() {
        this.setLabel(R.string.map_feature_document_event);
        return this;
    }

    public GoogleAnalyticEvent EditAttributes() {
        this.setLabel(R.string.edit_attributes_event);
        return this;
    }

    public GoogleAnalyticEvent CreateAttribute() {
        this.setLabel(R.string.create_attribute);
        return this;
    }

    public GoogleAnalyticEvent SignUp() {
        this.setLabel(R.string.signup_clicked);
        return this;
    }
    //endregion
}
