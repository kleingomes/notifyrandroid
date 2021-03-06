package com.notifyrapp.www.notifyr.Model;

/**
 * Created by K on 11/4/2016.
 */

public class Item {
    private int id;
    private String name;
    private String iurl;
    private int itemTypeId;
    private String itemTypeName;
    private int priority;
    private int userItemId;
    private int itemRowId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIurl() {
        return iurl;
    }

    public void setIurl(String iurl) {
        this.iurl = iurl;
    }

    public int getItemTypeId() {
        return itemTypeId;
    }

    public void setItemTypeId(int itemTypeId) {
        this.itemTypeId = itemTypeId;
    }

    public String getItemTypeName() {
        return itemTypeName;
    }

    public void setItemTypeName(String itemTypeName) {
        this.itemTypeName = itemTypeName;
    }

    public int getUserItemId() {
        return userItemId;
    }

    public void setUserItemId(int userItemId) {
        this.userItemId = userItemId;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public String getPriorityString()
    {
        if(priority == 0)
            return "None";
        else if(priority == 1)
            return "Low";
        else if(priority == 2)
            return "Medium";
        else if(priority == 3)
            return "High";
        return "None";
    }

    public int getItemRowId() {
        return itemRowId;
    }

    public void setItemRowId(int itemRowId) {
        this.itemRowId = itemRowId;
    }
}
