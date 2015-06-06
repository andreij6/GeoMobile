package com.geospatialcorporation.android.geomobile.models.Bookmarks;

import com.geospatialcorporation.android.geomobile.models.Interfaces.IdModel;

/**
 * Created by andre on 6/5/2015.
 */
public class Bookmark implements IdModel {

    int Id;
    String Name;

    BookmarkPosition Position;

    //region Getters & Setters
    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public BookmarkPosition getPosition() {
        return Position;
    }

    public void setPosition(BookmarkPosition position) {
        Position = position;
    }

    //endregion

    public Bookmark(){}
    public Bookmark(String name, BookmarkPosition position){
        Name = name;
        Position = position;
    }
}
