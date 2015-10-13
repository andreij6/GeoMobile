package com.geospatialcorporation.android.geomobile.models;

public class ClientSearchFilter {
    String type;
    String name;
    String page;
    String pagesize;

    public ClientSearchFilter(int clientTypeCode) {
        type = clientTypeCode + "";
        name = "";
        page = "1";
        pagesize = "20"; //doesnt matter the server always sends all for the type
    }


    //G's & S's

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public String getPagesize() {
        return pagesize;
    }

    public void setPagesize(String pagesize) {
        this.pagesize = pagesize;
    }
}
