package com.notifyrapp.www.notifyr.Data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.text.format.DateFormat;
import android.util.Log;

import com.notifyrapp.www.notifyr.Business.Business;
import com.notifyrapp.www.notifyr.Model.Article;
import com.notifyrapp.www.notifyr.Model.Item;
import com.notifyrapp.www.notifyr.Model.ItemType;
import com.notifyrapp.www.notifyr.Model.UserProfile;
import com.notifyrapp.www.notifyr.Model.UserSetting;

import org.joda.time.DateTime;
import org.joda.time.format.ISODateTimeFormat;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;


public class Repository {

    /* Private Fields */
    private Context context;
    private String dbName = "NotifyrLocal.db";
    private SQLiteDatabase notifyrDB;

    /* Constructor */
    public Repository(Context context) {
        this.context = context;
    }

    /* Member Functions */
    //region Items

    public List<Item> getItems() {
        String TableName = "Item";
        String Data = "";
        String PrintToConsole = "";
        SQLiteDatabase db = null;
        ArrayList<Item> items = new ArrayList<Item>();

        try {
            File path = context.getDatabasePath(dbName);
            db = SQLiteDatabase.openDatabase(String.valueOf(path), null, SQLiteDatabase.NO_LOCALIZED_COLLATORS | SQLiteDatabase.OPEN_READWRITE);
            Cursor c = db.rawQuery("SELECT * FROM " + TableName + " ORDER BY NAME", null);

            int col_itemid = c.getColumnIndex("ItemId");
            int col_name = c.getColumnIndex("Name");
            int col_iurl = c.getColumnIndex("IUrl");
            int col_itemtypeid = c.getColumnIndex("ItemTypeId");
            int col_itemtypename = c.getColumnIndex("ItemTypeName");

            // Check if our result was valid.
            c.moveToFirst();
            if (c != null) {
                // Loop through all Results
                do {
                    Item item = new Item();
                    item.setId(c.getInt(col_itemid));
                    item.setName(c.getString(col_name));
                    item.setIurl(c.getString(col_iurl));
                    item.setItemTypeId(c.getInt(col_itemtypeid));
                    item.setItemTypeName(c.getString(col_itemtypename));
                    items.add(item);
                } while (c.moveToNext());
            }

        } catch (Exception e) {
            Log.e("exception", e.getMessage());
        } finally {
            if (db != null) {
                db.close();
            }
        }
        return items;
    }

    public List<Item> getItemsByQuery(String query) {
        String TableName = "Item";
        String Data = "";
        String PrintToConsole = "";
        SQLiteDatabase db = null;
        ArrayList<Item> items = new ArrayList<Item>();

        try {
            File path = context.getDatabasePath(dbName);
            db = SQLiteDatabase.openDatabase(String.valueOf(path), null, SQLiteDatabase.NO_LOCALIZED_COLLATORS | SQLiteDatabase.OPEN_READWRITE);
            Cursor c = db.rawQuery("SELECT * FROM " + TableName + " WHERE Name like '%"+query+"%' ORDER BY NAME", null);

            int col_itemid = c.getColumnIndex("ItemId");
            int col_name = c.getColumnIndex("Name");
            int col_iurl = c.getColumnIndex("IUrl");
            int col_itemtypeid = c.getColumnIndex("ItemTypeId");
            int col_itemtypename = c.getColumnIndex("ItemTypeName");

            // Check if our result was valid.
            c.moveToFirst();
            if (c != null) {
                // Loop through all Results
                do {
                    Item item = new Item();
                    item.setId(c.getInt(col_itemid));
                    item.setName(c.getString(col_name));
                    item.setIurl(c.getString(col_iurl));
                    item.setItemTypeId(c.getInt(col_itemtypeid));
                    item.setItemTypeName(c.getString(col_itemtypename));
                    items.add(item);
                } while (c.moveToNext());
            }

        } catch (Exception e) {
            Log.e("exception", e.getMessage());
        } finally {
            if (db != null) {
                db.close();
            }
        }
        return items;
    }

    public Boolean saveItem(Item item) {

        String tableName = "Item";
        SQLiteDatabase db = null;
        boolean isSuccess = true;
        try {
            File path = context.getDatabasePath(dbName);
            db = SQLiteDatabase.openDatabase(String.valueOf(path), null, SQLiteDatabase.NO_LOCALIZED_COLLATORS | SQLiteDatabase.OPEN_READWRITE);
            Boolean exists = checkIsDataAlreadyInDBorNot(tableName, "ItemId", String.valueOf(item.getId()), db);
            if (!exists) {
                db.insert(tableName, null, getItemParams(item));
            } else {
                String[] selectionArgs = { String.valueOf(item.getId()) };
                db.update(tableName, getItemParams(item),String.format("%s = ?", "ItemId"),selectionArgs);
            }
        } catch (Exception e) {
            isSuccess = false;
            Log.e("exception", e.getMessage());
        } finally {
            if (db != null) {
                db.close();
            }
        }
        return isSuccess;
    }

    public Boolean deleteAllItems() {
        String tableName = "Item";
        SQLiteDatabase db = null;
        boolean isSuccess = true;
        try {
            File path = context.getDatabasePath(dbName);
            db = SQLiteDatabase.openDatabase(String.valueOf(path), null, SQLiteDatabase.NO_LOCALIZED_COLLATORS | SQLiteDatabase.OPEN_READWRITE);
            db.execSQL("DELETE FROM " + tableName);

        } catch (Exception e) {
            isSuccess = false;
            Log.e("exception", e.getMessage());
        } finally {
            if (db != null) {
                db.close();
            }
        }
        return isSuccess;
    }

    public Boolean saveUserItem(Item userItem) {

        String tableName = "UserItem";
        SQLiteDatabase db = null;
        boolean isSuccess = true;
        try {
            File path = context.getDatabasePath(dbName);
            db = SQLiteDatabase.openDatabase(String.valueOf(path), null, SQLiteDatabase.NO_LOCALIZED_COLLATORS | SQLiteDatabase.OPEN_READWRITE);
            Boolean exists = checkIsDataAlreadyInDBorNot(tableName, "ItemId", String.valueOf(userItem.getId()), db);
            if (!exists) {
                db.insert(tableName, null, getUserItemParams(userItem));
            } else {
                String[] selectionArgs = { String.valueOf(userItem.getId()) };
                db.update(tableName, getUserItemParams(userItem),String.format("%s = ?", "ItemId"),selectionArgs);
            }
        } catch (Exception e) {
            isSuccess = false;
            Log.e("exception", e.getMessage());
        } finally {
            if (db != null) {
                db.close();
            }
        }
        return isSuccess;
    }

    public Boolean deleteUserItem(Item userItem) {
        String tableName = "UserItem";
        SQLiteDatabase db = null;
        boolean isSuccess = true;
        try {
            File path = context.getDatabasePath(dbName);
            db = SQLiteDatabase.openDatabase(String.valueOf(path), null, SQLiteDatabase.NO_LOCALIZED_COLLATORS | SQLiteDatabase.OPEN_READWRITE);
            Boolean exists = checkIsDataAlreadyInDBorNot(tableName, "ItemId", String.valueOf(userItem.getId()), db);
            if (exists) {
                db.execSQL("DELETE FROM "
                        + tableName
                        + " WHERE ItemId = " + userItem.getId());
            }
        } catch (Exception e) {
            isSuccess = false;
            Log.e("exception", e.getMessage());
        } finally {
            if (db != null) {
                db.close();
            }
        }
        return isSuccess;
    }

    public List<Item> getUserItems() {
        String TableName = "UserItem";
        String Data = "";
        String PrintToConsole = "";
        SQLiteDatabase db = null;
        ArrayList<Item> items = new ArrayList<Item>();

        try {
            File path = context.getDatabasePath(dbName);
            db = SQLiteDatabase.openDatabase(String.valueOf(path), null, SQLiteDatabase.NO_LOCALIZED_COLLATORS | SQLiteDatabase.OPEN_READWRITE);
            Cursor c = db.rawQuery("SELECT * FROM " + TableName + " ORDER BY NAME", null);

            int col_itemid = c.getColumnIndex("ItemId");
            int col_name = c.getColumnIndex("Name");
            int col_iurl = c.getColumnIndex("IUrl");
            int col_itemtypeid = c.getColumnIndex("ItemTypeId");
            int col_itemtypename = c.getColumnIndex("ItemTypeName");
            int col_useritemid = c.getColumnIndex("UserItemId");
            int col_priority = c.getColumnIndex("Priority");

            // Check if our result was valid.
            c.moveToFirst();
            if (c != null) {
                // Loop through all Results
                do {
                    Item item = new Item();
                    item.setId(c.getInt(col_itemid));
                    item.setName(c.getString(col_name));
                    item.setIurl(c.getString(col_iurl));
                    item.setItemTypeId(c.getInt(col_itemtypeid));
                    item.setItemTypeName(c.getString(col_itemtypename));
                    item.setUserItemId(c.getInt(col_useritemid));
                    item.setPriority(c.getInt(col_priority));
                    items.add(item);
                } while (c.moveToNext());
            }

        } catch (Exception e) {
            Log.e("exception", e.getMessage());
        } finally {
            if (db != null) {
                db.close();
            }
        }
        return items;
    }

    public List<ItemType> getItemCategories() {
        String TableName = "UserItem";
        SQLiteDatabase db = null;
        List<ItemType> itemCategories = new ArrayList<ItemType>();

        try {
            File path = context.getDatabasePath(dbName);
            db = SQLiteDatabase.openDatabase(String.valueOf(path), null, SQLiteDatabase.NO_LOCALIZED_COLLATORS | SQLiteDatabase.OPEN_READWRITE);
            Cursor c = db.rawQuery("SELECT" +
                    " ItemTypeId, ItemTypeName" +
                    " FROM " + TableName +
                    " GROUP BY ItemTypeId,ItemTypeName" +
                    " ORDER BY ItemTypeName", null);

            int col_itemtypeid = c.getColumnIndex("ItemTypeId");
            int col_itemtypename = c.getColumnIndex("ItemTypeName");

            // Check if our result was valid.
            if (c != null && c.moveToFirst()) {
                // Loop through all Results
                do {
                    ItemType itemType = new ItemType();
                    itemType.setId(c.getInt(col_itemtypeid));
                    itemType.setItemTypeName(c.getString(col_itemtypename));
                    itemCategories.add(itemType);

                } while (c.moveToNext());
            }
        } catch (Exception e) {
            Log.e("exception", e.getMessage());
        } finally {
            if (db != null) {
                db.close();
            }
        }
        return itemCategories;
    }

    public Boolean isUserFollowingItem(Item userItem) {
        String tableName = "UserItem";
        SQLiteDatabase db = null;

        try {
            File path = context.getDatabasePath(dbName);
            db = SQLiteDatabase.openDatabase(String.valueOf(path), null, SQLiteDatabase.NO_LOCALIZED_COLLATORS | SQLiteDatabase.OPEN_READWRITE);
            Boolean exists = checkIsDataAlreadyInDBorNot(tableName, "ItemId", String.valueOf(userItem.getId()), db);
            return exists;
        } catch (Exception e) {
            Log.e("exception", e.getMessage());
        } finally {
            if (db != null) {
                db.close();
            }
        }
        return null;
    }


    //endregion

    //region Article
    public Boolean saveArticles(List<Article> articles) {
        String tableName = "Article";
        SQLiteDatabase db = null;
        boolean isSuccess = true;
        try {
            File path = context.getDatabasePath(dbName);
            db = SQLiteDatabase.openDatabase(String.valueOf(path), null, SQLiteDatabase.NO_LOCALIZED_COLLATORS | SQLiteDatabase.OPEN_READWRITE);
            for (Article article : articles) {

                Boolean exists = checkIsDataAlreadyInDBorNot(tableName, "ArticleId", String.valueOf(article.getId()), db);
                if (!exists) {
                    db.insert(tableName, null, getArticleParams(article));
                }
            }
        } catch (Exception e) {
            isSuccess = false;
            Log.e("exception", e.getMessage());
        } finally {
            if (db != null) {
                db.close();
            }
        }
        return isSuccess;
    }

    public List<Article> getArticles(int skip, int take, String sortBy) {
        String TableName = "Article";
        String Data = "";
        String PrintToConsole = "";
        ArrayList<Article> articles = new ArrayList<Article>();
        this.notifyrDB = this.context.openOrCreateDatabase(dbName, MODE_PRIVATE, null);

        try {

            Cursor c = this.notifyrDB.rawQuery("SELECT * FROM " + TableName, null);

            int col_userId = c.getColumnIndex("ArticleId");
            int col_email = c.getColumnIndex("Email");
            int col_accountType = c.getColumnIndex("AccountType");

            // Check if our result was valid.
            c.moveToFirst();
            if (c != null) {
                // Loop through all Results
                do {
                    String Id = c.getString(col_userId);
                    String Name = c.getString(col_email);
                    PrintToConsole = PrintToConsole + Id + "/" + Name + "\n";
                    Log.e("CreateUserProfileTable", PrintToConsole);
                } while (c.moveToNext());
            }

        } catch (Exception e) {
            Log.e("exception", e.getMessage());
        } finally {
            if (notifyrDB != null) {
                notifyrDB.close();
            }
        }
        return new ArrayList<Article>();
    }

    public List<Article> getUserArticles(int skip, int take, String sortBy, int itemTypeId) {
        String TableName = "Article";
        String Data = "";
        SQLiteDatabase db = null;
        List<Article> articles = new ArrayList<Article>();
        try {
            File path = context.getDatabasePath(dbName);
            db = SQLiteDatabase.openDatabase(String.valueOf(path), null, SQLiteDatabase.NO_LOCALIZED_COLLATORS | SQLiteDatabase.OPEN_READWRITE);
            Cursor c = null;
            /* TODO: finish this query */

            if (sortBy == Business.SortBy.Newest.toString()) {
                c = db.rawQuery("SELECT * FROM " + TableName
                        + " ORDER BY PublishDateUnix DESC LIMIT " + take + " OFFSET " + skip, null);
            } else {
                c = db.rawQuery("SELECT * FROM " + TableName
                        + " ORDER BY Score DESC LIMIT " + take + " OFFSET " + skip, null);
            }

            articles = getArticles(c);
        } catch (Exception e) {
            Log.e("exception", e.getMessage());
        } finally {
            if (db != null) {
                db.close();
            }
        }
        return articles;
    }
    //endregion

    //region User

    public Boolean saveUserProfile(UserProfile userProfile) {
        String tableName = "UserProfile";
        SQLiteDatabase notifyrDB;
        notifyrDB = this.context.openOrCreateDatabase(dbName, MODE_PRIVATE, null);

        Boolean accountExists = checkIfTableHasRows(tableName);

        if (accountExists) {
            // Clear the Account Row
            notifyrDB.execSQL("DELETE FROM " + tableName);
        }
        // Insert the profile
        notifyrDB.execSQL("INSERT INTO "
                + tableName
                + " (Id, Email,AccountType)"
                + " VALUES ('" + userProfile.UserId + "', null,null);");

        if (notifyrDB != null) {
            notifyrDB.close();
        }

        return true;
    }

    public UserProfile getUserProfile() {
        return new UserProfile();
    }

    public UserSetting getUserSettings() {
        String TableName = "UserSetting";
        String Data = "";
        String PrintToConsole = "";
        UserSetting userSetting = new UserSetting();
        SQLiteDatabase db = null;

        try {

            File path = context.getDatabasePath(dbName);
            db = SQLiteDatabase.openDatabase(String.valueOf(path), null, SQLiteDatabase.NO_LOCALIZED_COLLATORS | SQLiteDatabase.OPEN_READWRITE);

            Cursor c = db.rawQuery("SELECT * FROM " + TableName, null);

            int col_settingName = c.getColumnIndex("SettingName");
            int col_settingValue = c.getColumnIndex("SettingValue");

            // Check if our result was valid.
            c.moveToFirst();
            if (c != null) {
                // Loop through all Results
                do {
                    String settingName = c.getString(col_settingName);
                    String settingValue = c.getString(col_settingValue);

                    switch (settingName) {
                        case "MaxNotifications":
                            userSetting.setMaxNotificaitons(Integer.parseInt(settingValue));
                            break;
                        case "ArticleDisplayType":
                            userSetting.setArticleDisplayType(Integer.parseInt(settingValue));
                            break;
                        case "ArticleReaderMode":
                            userSetting.setArticleReaderMode(Boolean.parseBoolean(settingValue));
                            break;
                    }
                } while (c.moveToNext());
            }
        } catch (Exception e) {
            Log.e("exception", e.getMessage());
        } finally {
            if (db != null) {
                db.close();
            }
        }
        return userSetting;
    }

    public Boolean saveUserSettings(UserSetting userSetting) {
        String tableName = "UserSetting";
        SQLiteDatabase notifyrDB;
        SQLiteDatabase db = null;
        boolean isSuccess = true;
        int isArticleReaderMode = (userSetting.isArticleReaderMode()) ? 1 : 0;
        try {
            File path = context.getDatabasePath(dbName);
            db = SQLiteDatabase.openDatabase(String.valueOf(path), null, SQLiteDatabase.NO_LOCALIZED_COLLATORS | SQLiteDatabase.OPEN_READWRITE);
            // Insert the profile
            db.execSQL("UPDATE " //update
                    + tableName
                    + " SET SettingValue = '" + String.valueOf(userSetting.getMaxNotificaitons()+"'")
                    + " WHERE SettingName = 'MaxNotifications'");
            db.execSQL("UPDATE " //update
                    + tableName
                    + " SET SettingValue = '" + String.valueOf(userSetting.isArticleReaderMode()+"'")
                    + " WHERE SettingName = 'ArticleReaderMode'");
            db.execSQL("UPDATE " //update
                    + tableName
                    + " SET SettingValue = '" + String.valueOf(userSetting.getArticleDisplayType()+"'")
                    + " WHERE SettingName = 'ArticleDisplayType'");

        } catch (Exception e) {
            isSuccess = false;
            Log.e("exception", e.getMessage());
        } finally {
            if (db != null) {
                db.close();
            }
        }


        return isSuccess;
    }
    //endregion

    //region Notifications
    public Boolean saveUserNotifications(List<Article> articles) {

        String tableName = "UserNotification";
        SQLiteDatabase db = null;
        boolean isSuccess = true;
        try {
            File path = context.getDatabasePath(dbName);
            db = SQLiteDatabase.openDatabase(String.valueOf(path), null, SQLiteDatabase.NO_LOCALIZED_COLLATORS | SQLiteDatabase.OPEN_READWRITE);
            for (Article article : articles) {

                Boolean exists = checkIsDataAlreadyInDBorNot(tableName, "ArticleId", String.valueOf(article.getId()), db);
                if (!exists) {
                    // Insert the UserNotification
                    db.insert(tableName, null, getArticleParams(article));
                }
            }
        } catch (Exception e) {
            isSuccess = false;
            Log.e("exception", e.getMessage());
        } finally {
            if (db != null) {
                db.close();
            }
        }
        return isSuccess;
    }

    public List<Article> getUserNotifications(int skip, int take) /* TODO ADD PARAMS FOR PAGING) */ {
        String TableName = "UserNotification";
        String Data = "";
        SQLiteDatabase db = null;
        List<Article> articles = new ArrayList<Article>();
        try {
            File path = context.getDatabasePath(dbName);
            db = SQLiteDatabase.openDatabase(String.valueOf(path), null, SQLiteDatabase.NO_LOCALIZED_COLLATORS | SQLiteDatabase.OPEN_READWRITE);
            Cursor c = db.rawQuery("SELECT * FROM " + TableName
                    + " ORDER BY ArticleNotifiedDateUnix DESC LIMIT " + take + " OFFSET " + skip, null);

            articles = getArticles(c);

        } catch (Exception e) {
            Log.e("exception", e.getMessage());
        } finally {
            if (db != null) {
                db.close();
            }
        }
        return articles;
    }

    public Boolean deleteUserNotifications()
    {
        String tableName = "UserNotification";
        SQLiteDatabase db = null;
        boolean isSuccess = true;
        try {
            File path = context.getDatabasePath(dbName);
            db = SQLiteDatabase.openDatabase(String.valueOf(path), null, SQLiteDatabase.NO_LOCALIZED_COLLATORS | SQLiteDatabase.OPEN_READWRITE);
            db.execSQL("DELETE FROM " + tableName);
        } catch (Exception e) {
            isSuccess = false;
            Log.e("DATABASE_QUERY", "Error in deleteUserNotifications(): " + e.getMessage());
        } finally {
            if (db != null) {
                db.close();
                Log.d("DATABASE_QUERY", "Success deleting all user notifications - deleteUserNotifications()");
            }
        }
        return isSuccess;
    }


    //endregion

    //region Bookmarks


    public List<Article> getBookmarks(int skip, int take) {
        String TableName = "ArticleBookmark";
        String Data = "";
        SQLiteDatabase db = null;
        List<Article> articles = new ArrayList<Article>();
        try {
            File path = context.getDatabasePath(dbName);
            db = SQLiteDatabase.openDatabase(String.valueOf(path), null, SQLiteDatabase.NO_LOCALIZED_COLLATORS | SQLiteDatabase.OPEN_READWRITE);
            Cursor c = db.rawQuery("SELECT * FROM " + TableName
                    + " ORDER BY ArticleNotifiedDate DESC LIMIT " + take + " OFFSET " + skip, null);

            articles = getArticles(c);

        } catch (Exception e) {
            Log.e("exception", e.getMessage());
        } finally {
            if (db != null) {
                db.close();
            }
        }
        return articles;
    }

    public boolean saveBookmark(Article article) {
        Log.d("saveBookmark", article.getTitle());
        String tableName = "ArticleBookmark";
        SQLiteDatabase db = null;
        boolean isSuccess = true;
        try {
            File path = context.getDatabasePath(dbName);
            db = SQLiteDatabase.openDatabase(String.valueOf(path), null, SQLiteDatabase.NO_LOCALIZED_COLLATORS | SQLiteDatabase.OPEN_READWRITE);
            Boolean exists = checkIsDataAlreadyInDBorNot(tableName, "ArticleId", String.valueOf(article.getId()), db);
            if (!exists) {
                db.insert(tableName, null, getArticleParams(article));
            }
        } catch (Exception e) {
            isSuccess = false;
            Log.e("exception", e.getMessage());
        } finally {
            if (db != null) {
                db.close();
            }
        }
        Log.d("saveBookmark", "Status(" + isSuccess + ") " + article.getTitle());
        return isSuccess;
    }

    public boolean deleteBookmark(Article article) {
        String tableName = "ArticleBookmark";
        SQLiteDatabase db = null;
        boolean isSuccess = true;
        try {
            File path = context.getDatabasePath(dbName);
            db = SQLiteDatabase.openDatabase(String.valueOf(path), null, SQLiteDatabase.NO_LOCALIZED_COLLATORS | SQLiteDatabase.OPEN_READWRITE);
            db.execSQL("DELETE FROM "
                    + tableName
                    + " WHERE ArticleId =" + article.getId());
        } catch (Exception e) {
            isSuccess = false;
            Log.e("exception", e.getMessage());
        } finally {
            if (db != null) {
                db.close();
            }
        }
        return isSuccess;
    }
    //endregion

    //region Helpers
    private List<Article> getArticles(Cursor c) {
        List<Article> articles = new ArrayList<Article>();
        int col_articleid = c.getColumnIndex("ArticleId");
        int col_source = c.getColumnIndex("Source");
        int col_score = c.getColumnIndex("Score");
        int col_title = c.getColumnIndex("Title");
        int col_author = c.getColumnIndex("Author");
        int col_description = c.getColumnIndex("Description");
        int col_url = c.getColumnIndex("URL");
        int col_iurl = c.getColumnIndex("IURL");
        int col_articleNotifiedDate = c.getColumnIndex("ArticleNotifiedDate");
        int col_publishDate = c.getColumnIndex("PublishDate");
        int col_isFavourite = c.getColumnIndex("IsFavourite");
        int col_shortLinkURL = c.getColumnIndex("ShortLinkURL");
        int col_relatedInterests = c.getColumnIndex("RelatedInterests");
        int col_timeAgo = c.getColumnIndex("TimeAgo");
        int col_notifiedTimeAgo = c.getColumnIndex("NotifiedTimeAgo");
        int col_realtedInterestsURL = c.getColumnIndex("RelatedInterestsURL");


        // Check if our result was valid.
        c.moveToFirst();
        if (c != null) {
            // Loop through all Results
            do {

                /* Need to convert the strings to Date types */
                String pubDateStr = c.getString(col_articleNotifiedDate);
                String notifyrDateStr = c.getString(col_publishDate);

                DateTime pubDate = ISODateTimeFormat.dateTimeParser().parseDateTime(pubDateStr);
                DateTime notifyrDate = ISODateTimeFormat.dateTimeParser().parseDateTime(notifyrDateStr);

                Article article = new Article();
                article.setScore(c.getInt(col_score));
                article.setId(c.getInt(col_articleid));
                article.setTitle(c.getString(col_title));
                article.setTimeAgo(c.getString(col_author));
                article.setDescription(c.getString(col_description));
                article.setUrl(c.getString(col_url));
                article.setSource(c.getString(col_source));
                article.setTimeAgo(c.getString(col_timeAgo));
                article.setIurl(c.getString(col_iurl));
                article.setArticleNotifiedDate(notifyrDate);
                article.setPublishDate(pubDate);
                article.setFavourite(c.getInt(col_isFavourite) == 1 ? true : false);
                article.setShortLinkUrl(c.getString(col_shortLinkURL));
                article.setRelatedInterests(c.getString(col_relatedInterests));
                article.setNotifiedTimeAgo(c.getString(col_notifiedTimeAgo));
                article.setRelatedInterestsURL(c.getString(col_realtedInterestsURL));
                articles.add(article);

            } while (c.moveToNext());
        }
        return articles;
    }

    private ContentValues getItemParams(Item item) {
        ContentValues param = new ContentValues();
        param.put("ItemId", item.getId());
        param.put("Name", item.getName());
        param.put("IUrl", item.getIurl());
        param.put("ItemTypeId", item.getItemTypeId());
        param.put("ItemTypeName", item.getItemTypeName());
        return param;
    }

    private ContentValues getUserItemParams(Item item) {
        ContentValues param = new ContentValues();
        param.put("ItemId", item.getId());
        param.put("Name", item.getName());
        param.put("IUrl", item.getIurl());
        param.put("ItemTypeId", item.getItemTypeId());
        param.put("ItemTypeName", item.getItemTypeName());
        param.put("UserItemId", item.getUserItemId());
        param.put("Priority", item.getPriority());
        return param;
    }

    private ContentValues getArticleParams(Article article) {
        ContentValues param = new ContentValues();
        param.put("ArticleId", article.getId());
        param.put("Source", article.getSource());
        param.put("Score", article.getScore());
        param.put("Title", article.getTitle());
        param.put("Author", article.getAuthor());
        param.put("Description", article.getDescription());
        param.put("URL", article.getUrl());
        param.put("IURL", article.getIurl());
        param.put("ArticleNotifiedDate", article.getArticleNotifiedDate().toString());
        param.put("PublishDate", article.getPublishDate().toString());
        param.put("IsFavourite", article.getFavourite());
        param.put("ShortLinkURL", article.getShortLinkUrl());
        param.put("RelatedInterests", article.getRelatedInterests());
        param.put("TimeAgo", article.getTimeAgo());
        param.put("NotifiedTimeAgo", article.getNotifiedTimeAgo());
        param.put("RelatedInterestsURL", article.getRelatedInterestsURL());
        param.put("PublishDateUnix", article.getPublishDate().getMillis());
        param.put("ArticleNotifiedDateUnix", article.getArticleNotifiedDate().getMillis());
        return param;
    }



    private boolean checkIsDataAlreadyInDBorNot(String TableName,
                                                String dbfield, String fieldValue, SQLiteDatabase sqldb) {
        //SQLiteDatabase sqldb = this.context.openOrCreateDatabase(dbName, MODE_PRIVATE, null);
        String Query = "Select * from " + TableName + " where " + dbfield + " = " + fieldValue;
        Cursor cursor = sqldb.rawQuery(Query, null);
        if (cursor.getCount() <= 0) {
            cursor.close();
            return false;
        }
        cursor.moveToFirst();//
        //cursor.close();
        return true;
    }

    private boolean checkIfTableHasRows(String TableName) {
        SQLiteDatabase sqldb = this.context.openOrCreateDatabase(dbName, MODE_PRIVATE, null);
        String Query = "Select * from " + TableName;
        Cursor cursor = sqldb.rawQuery(Query, null);
        if (cursor.getCount() <= 0) {
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }

    public boolean checkIfDatabaseExists() {

        SQLiteDatabase checkDB = null;
        try {
            File path = context.getDatabasePath(dbName);
            checkDB = SQLiteDatabase.openDatabase(String.valueOf(path), null,
                    SQLiteDatabase.OPEN_READONLY);
            checkDB.close();
        } catch (SQLiteException e) {
            // database doesn't exist yet.
        }
        return checkDB != null;
    }

    //endregion
}
