package com.notifyrapp.www.notifyr.Data;

import android.content.Context;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import com.notifyrapp.www.notifyr.Business.CallbackInterface;
import com.notifyrapp.www.notifyr.Model.AccessToken;
import com.notifyrapp.www.notifyr.Model.Article;
import com.notifyrapp.www.notifyr.Model.Item;
import com.notifyrapp.www.notifyr.Model.UserSetting;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDateTime;
import org.joda.time.format.ISODateTimeFormat;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.notifyrapp.www.notifyr.Data.WebApi.NotifyrObjects.JsonStatusResult;
import static com.notifyrapp.www.notifyr.Data.WebApi.NotifyrObjects.Post;
import static com.notifyrapp.www.notifyr.Data.WebApi.NotifyrObjects.Item;
import static com.notifyrapp.www.notifyr.Data.WebApi.NotifyrObjects.NetworkStatus;
import static com.notifyrapp.www.notifyr.Data.WebApi.NotifyrObjects.Put;
import static java.lang.Thread.sleep;

public class WebApi {

    /* Private Fields */
    private final String apiBaseUrl = "https://www.notifyr.ca/service/v2/api/";
    private final String tokenUrl = "https://www.notifyr.ca/service/v2/token";
    private final String defaultPassword = "2014$NotifyrPassword$2014";

    //private String apiBaseUrlDev = "http://www.notifyr.ca/dev/service/api/";
    private Context context;
    private String accessToken = "";
    private String userId = "";

    /* Constructor */
    public WebApi(Context context) {
        this.context = context;
        this.userId = PreferenceManager.getDefaultSharedPreferences(context).getString("userid", "");
        this.accessToken = PreferenceManager.getDefaultSharedPreferences(context).getString("accesstoken", "");

   /*     if(this.accessToken.equals("") && !this.userId.equals(""))
        {
            getAccessToken(null);
        }*/
    }
    //region Security

    public void getAccessToken(CallbackInterface callback) {
        String url = tokenUrl;

        if (postJSONObjectFromURL.getStatus().equals(AsyncTask.Status.PENDING)) {
            postJSONObjectFromURL.execute(url, context, callback, NotifyrObjects.AccessToken);
        }
    }


    //endregion

    //region User Profile
    public void registerUserProfile(String userName, String password, CallbackInterface callback) {
        String urlPath = "Account/RegisterGuest";
        String url = apiBaseUrl + urlPath;

        if (postJSONObjectFromURL.getStatus().equals(AsyncTask.Status.PENDING)) {
            postJSONObjectFromURL.execute(url, context, callback, NotifyrObjects.UserProfile);
        }
    }

    public void registerDevice(String token, CallbackInterface callback) {
        String urlPath = "Account/RegisterAndroidDevice?deviceToken="+token;
        String url = apiBaseUrl + urlPath;

        if (postJSONObjectFromURL.getStatus().equals(AsyncTask.Status.PENDING)) {
            postJSONObjectFromURL.execute(url, context, callback, JsonStatusResult);
        }
    }

    public void saveUserSettings(UserSetting userSetting, CallbackInterface callback) {
        String urlPath = "User/SaveUserSettings";

        String url = apiBaseUrl + urlPath;
        Log.d("saveUserSettings:", url);
        if (postJSONObjectFromURL.getStatus().equals(AsyncTask.Status.PENDING)) {
            postJSONObjectFromURL.execute(url, context, callback, NotifyrObjects.UserSetting, userSetting);
        }
    }
    //endregion

    //region Items
    public void getItemArticles(long itemId, int skip, int take, String sortBy, CallbackInterface callback) {
        String urlPath = "Item/getItemArticles?itemId=" + itemId + "&skip=" + skip + "&take=" + take;
        String url = apiBaseUrl + urlPath;
        if (postJSONObjectFromURL.getStatus().equals(AsyncTask.Status.PENDING)) {
            postJSONObjectFromURL.execute(url, context, callback, Item);
        }
    }

    public void getPopularItems(int skip, int take, CallbackInterface callback) {
        String urlPath = "Item/GetPopularItemsByType?skip=" + skip + "&take=" + take+ "&type=0";
        String url = apiBaseUrl + urlPath;
        if (postJSONObjectFromURL.getStatus().equals(AsyncTask.Status.PENDING)) {
            postJSONObjectFromURL.execute(url, context, callback, Item);
        }
    }

    public void getPopularItemsByItemTypeId(int skip, int take, int itemTypeId, CallbackInterface callback) {
        String urlPath = "Item/GetPopularItemsByType?skip=" + skip + "&take=" + take + "&type=" + itemTypeId;
        String url = apiBaseUrl + urlPath;
        if (postJSONObjectFromURL.getStatus().equals(AsyncTask.Status.PENDING)) {
            postJSONObjectFromURL.execute(url, context, callback, Item);
        }
    }

    public void getAllItems(CallbackInterface callback) {
        String urlPath = "Item/GetAllItems";
        String url = apiBaseUrl + urlPath;
        if (postJSONObjectFromURL.getStatus().equals(AsyncTask.Status.PENDING)) {
            postJSONObjectFromURL.execute(url, context, callback, Item);
        }
    }

    public void getUserItems(CallbackInterface callback) {
        String urlPath = "Item/GetUserItems";
        String url = apiBaseUrl + urlPath;
        if (postJSONObjectFromURL.getStatus().equals(AsyncTask.Status.PENDING)) {
            postJSONObjectFromURL.execute(url, context, callback, Item);
        }
    }

    public void getItemsByQuery(String query, CallbackInterface callback) {
        String urlPath = null;
        try {
            urlPath = "Item/GetItems?query=" +  URLEncoder.encode(query, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String url = apiBaseUrl + urlPath;
        if (postJSONObjectFromURL.getStatus().equals(AsyncTask.Status.PENDING)) {
            postJSONObjectFromURL.execute(url, context, callback, Item);
        }
    }

    public void updateUserItemPriority(int itemId, int priorityId, CallbackInterface callback) {
        String params = "itemId=" + itemId + "&priority=" + priorityId;
        String urlPath = "Item/UpdateUserItemPriority?" + params;
        String url = apiBaseUrl + urlPath;
        if (postJSONObjectFromURL.getStatus().equals(AsyncTask.Status.PENDING)) {
            postJSONObjectFromURL.execute(url, context, callback, Put, params);
        }
    }

    public void saveUserItem(Item item, CallbackInterface callback) {
        String params = "itemId=" + item.getId();
        String urlPath = "Item/AddUserItem?" + params;
        String url = apiBaseUrl + urlPath;
        if (postJSONObjectFromURL.getStatus().equals(AsyncTask.Status.PENDING)) {
            postJSONObjectFromURL.execute(url, context, callback, Post, params);
        }
    }

    public void deleteUserItem(int itemId, CallbackInterface callback) {
        String params = "itemId=" + itemId;
        String urlPath = "Item/DeleteUserItem?" + params;
        String url = apiBaseUrl + urlPath;
        if (postJSONObjectFromURL.getStatus().equals(AsyncTask.Status.PENDING)) {
            postJSONObjectFromURL.execute(url, context, callback, Post, params);
        }
    }


    //endregion

    //region Articles
    public void getUserArticles(int skip, int take, String sortBy, int itemTypeId, CallbackInterface callback) {
        String urlPath = "Item/GetArticlesForUserItems?skip=" + skip + "&take=" + take + "&sortBy=" + sortBy + "&itemTypeId=" + itemTypeId;
        String url = apiBaseUrl + urlPath;
        if (postJSONObjectFromURL.getStatus().equals(AsyncTask.Status.PENDING)) {
            postJSONObjectFromURL.execute(url, context, callback, NotifyrObjects.Article);
        }
    }

    public void getUserItemArticles(int skip, int take, String sortBy, int itemId, CallbackInterface callback) {
        String urlPath = "Item/GetArticlesByItem?skip=" + skip + "&take=" + take + "&sortBy=" + sortBy + "&itemId=" + itemId;
        String url = apiBaseUrl + urlPath;
        if (postJSONObjectFromURL.getStatus().equals(AsyncTask.Status.PENDING)) {
            postJSONObjectFromURL.execute(url, context, callback, NotifyrObjects.Article);
        }
    }

    //endregion

    //region Notifications
    public void getUserNotifications(int skip, int take, CallbackInterface callback) {
        String urlPath = "Article/GetUserNotifiedArticles?skip=" + skip + "&take=" + take;
        String url = apiBaseUrl + urlPath;
        if (postJSONObjectFromURL.getStatus().equals(AsyncTask.Status.PENDING)) {
            postJSONObjectFromURL.execute(url, context, callback, NotifyrObjects.Article);
        }
    }

    public void getUserNotifications(String fromDate, CallbackInterface callback) {
        if (fromDate == null || fromDate.isEmpty()) {
            fromDate = "2000-01-01";
        }
        String urlPath = "Article/GetUserNotifiedArticles?fromDate=" + fromDate;
        String url = apiBaseUrl + urlPath;
        if (postJSONObjectFromURL.getStatus().equals(AsyncTask.Status.PENDING)) {
            postJSONObjectFromURL.execute(url, context, callback, NotifyrObjects.Article);
        }
    }

    public void sendTestNotification(CallbackInterface callback){
        String urlPath = "User/SendAndroidTestNotification?userId="+this.userId;
        String url = apiBaseUrl + urlPath;
        if (postJSONObjectFromURL.getStatus().equals(AsyncTask.Status.PENDING)) {
            postJSONObjectFromURL.execute(url, context, callback, NotifyrObjects.Get);
        }
    }

    public void deleteUserNotificationsServer(CallbackInterface callback)
    {
        String params = "userId=" + this.userId;
        String urlPath = "Article/ClearUserNotifications?" + params;
        String url = apiBaseUrl + urlPath;

        if (postJSONObjectFromURL.getStatus().equals(AsyncTask.Status.PENDING)) {
            postJSONObjectFromURL.execute(url, context, callback, NotifyrObjects.Post,params);
        }
    }

    //endregions

    //region MISC
    public void getNetworkStatus(CallbackInterface callback) {
        String urlPath = "Status/IsNetworkOnline";
        String url = apiBaseUrl + urlPath;
        if (postJSONObjectFromURL.getStatus().equals(AsyncTask.Status.PENDING)) {
            postJSONObjectFromURL.execute(url, context, callback, NetworkStatus);
        }
    }
    //endregion

    //region Network Requests
    private AsyncTask<Object, Void, List<Object>> postJSONObjectFromURL = new AsyncTask<Object, Void, List<Object>>() {

        @Override
        protected List<Object> doInBackground(Object... params) {

            HttpURLConnection httpcon;
            String urlString = (String) params[0];
            Context parentContext = (Context) params[1];
            CallbackInterface callback = (CallbackInterface) params[2];
            NotifyrObjects notifyrObjectType = (NotifyrObjects) params[3];
            Log.d("URL_REQUEST", urlString);
            String result = "";
            Boolean hasErrors = false;
            try {

                URL url = new URL(urlString);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(10000);
                conn.setConnectTimeout(15000);

                /* Set Request Type */
                if (notifyrObjectType == NotifyrObjects.AccessToken || notifyrObjectType == NotifyrObjects.UserProfile || notifyrObjectType == NotifyrObjects.UserSetting
                        || notifyrObjectType == NotifyrObjects.Post) {
                    conn.setRequestMethod("POST");
                    conn.setDoInput(true);
                    conn.setDoOutput(true);
                } else if (notifyrObjectType == NotifyrObjects.Put) {
                    conn.setRequestMethod("PUT");
                    conn.setDoInput(true);
                    conn.setDoOutput(true);
                } else if (notifyrObjectType == Item || notifyrObjectType == NotifyrObjects.Article ||
                        notifyrObjectType == NotifyrObjects.JsonStatusResult) {
                    String bearer = "Bearer " + accessToken;
                    conn.setRequestProperty("Authorization", bearer);
                    conn.setRequestMethod("GET");
                    conn.setDoInput(true);
                }

                /* Set Request Body */
                // Access Token
                if (notifyrObjectType == NotifyrObjects.AccessToken) {
                    String userName = userId + "@notifyr.ca";
                    String str = "grant_type=password&username=" + userName + "&password=" + defaultPassword;
                    byte[] outputInBytes = str.getBytes("UTF-8");
                    OutputStream os = conn.getOutputStream();
                    os.write(outputInBytes);
                    os.close();
                }
                // User Settings
                else if (notifyrObjectType == NotifyrObjects.UserSetting) {
                    UserSetting userSetting = (UserSetting) params[4];
                    String bearer = "Bearer " + accessToken;
                    conn.setRequestProperty("Authorization", bearer);
                    String param = "maxNotifications=" + userSetting.getMaxNotificaitons() + "&"
                            + "articleDisplayType=" + userSetting.getArticleDisplayType() + "&"
                            + "articleReaderMode=" + userSetting.isArticleReaderMode();
                    byte[] outputInBytes = param.getBytes("UTF-8");
                    OutputStream os = conn.getOutputStream();
                    os.write(outputInBytes);
                    os.close();
                }
                // REMOVE item
                else if (notifyrObjectType == NotifyrObjects.Post) {
                    String param = (String) params[4];
                    String bearer = "Bearer " + accessToken;
                    conn.setRequestProperty("Authorization", bearer);
                    byte[] outputInBytes = param.getBytes("UTF-8");
                    OutputStream os = conn.getOutputStream();
                    os.write(outputInBytes);
                    os.close();
                } else if (notifyrObjectType == NotifyrObjects.Put) {
                    String param = (String) params[4];
                    String bearer = "Bearer " + accessToken;
                    conn.setRequestProperty("Authorization", bearer);
                }

                conn.connect();
                int statusCode = conn.getResponseCode();

                InputStream is = null;

                if (statusCode >= 200 && statusCode < 400) {
                    // Create an InputStream in order to extract the response object
                    is = conn.getInputStream();
                } else {
                    is = conn.getErrorStream();
                    hasErrors = true;
                    //Toast.makeText(context,"Unable To Connect To Server!", Toast.LENGTH_SHORT).show();
                }

                InputStream stream = conn.getInputStream();
                result = streamToString(stream);


            } catch (IOException e) {
                e.printStackTrace();
                hasErrors = true;
                //Toast.makeText(context,"Unable To Connect To Server!", Toast.LENGTH_SHORT).show();
            }

            try {
                List<Object> returnObj = new ArrayList<>();
                returnObj.add(hasErrors);
                if (result != null && !result.equals("")) {
                    if (notifyrObjectType == NotifyrObjects.Article || notifyrObjectType == NotifyrObjects.Item) {
                        returnObj.add(new JSONArray(result));
                    } else if (notifyrObjectType == NotifyrObjects.NetworkStatus) {
                        returnObj.add(new JSONObject(result));
                    } else if (notifyrObjectType == NotifyrObjects.Post || notifyrObjectType == Put || notifyrObjectType == NotifyrObjects.JsonStatusResult) {
                        returnObj.add(result);
                    } else {
                        returnObj.add(new JSONObject(result));
                    }

                    returnObj.add(callback);
                    returnObj.add(notifyrObjectType);
                    returnObj.add(context);
                }
                return returnObj;//new JSONObject(result);
            } catch (JSONException e) {
                Log.d("WEB_API",e.getMessage());
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<Object> returnObjects) {

            // Save to Local SettingsActivity and Database
            Boolean hasErrors = (Boolean) returnObjects.get(0);
            if(hasErrors) {
                //Toast.makeText(context, "Unable To Connect To Server!", Toast.LENGTH_LONG).show();
            }
            if (returnObjects != null && returnObjects.size() == 5 && hasErrors == false) {

                CallbackInterface callback = (CallbackInterface) returnObjects.get(2);
                NotifyrObjects notifyrType = (NotifyrObjects) returnObjects.get(3);
                JSONObject jsonObject = null;
                JSONArray jsonArray = null;
                String response = null;
                if (notifyrType == NotifyrObjects.Article || notifyrType == NotifyrObjects.Item) {
                    jsonArray = (JSONArray) returnObjects.get(1);
                } else if (notifyrType == NotifyrObjects.NetworkStatus) {
                    jsonObject = (JSONObject) returnObjects.get(1);
                } else if (notifyrType == NotifyrObjects.Post || notifyrType == NotifyrObjects.Put || notifyrType == NotifyrObjects.JsonStatusResult) {
                    response = (String) returnObjects.get(1);
                } else {
                    jsonObject = (JSONObject) returnObjects.get(1);
                }
                try {

                    if (notifyrType == NotifyrObjects.UserProfile) {
                        //Repository repo = new Repository(context);
                        //UserProfile userProfile = new UserProfile();
                        //userProfile.UserId = jsonObject.getString("UserId");
                        //userProfile.Email = jsonObject.getString("Email");
                        //repo.saveUserProfile(userProfile);
                        //PreferenceManager.getDefaultSharedPreferences(context).edit().putString("userid", userProfile.UserId).commit();
                        callback.onCompleted(jsonObject.getString("UserId"));
                    } else if (notifyrType == NotifyrObjects.Article) {
                        final DateTimeZone dtz = DateTimeZone.getDefault(); //DateTimeZone.forID("Europe/Warsaw")
                        List<Article> articles = new ArrayList<Article>();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            Article article = new Article();
                            JSONObject jsonItem = jsonArray.getJSONObject(i);
                            article.setId((!jsonItem.isNull("Id") ? jsonItem.getInt("Id") : -1));

                            String pubDateStr = !jsonItem.isNull("PublishDate") ? jsonItem.getString("PublishDate") : "2000-01-01T00:00.000";
                            String notifyrDateStr = !jsonItem.isNull("ArticleNotifiedDate") ? jsonItem.getString("ArticleNotifiedDate") : "2000-01-01T00:00.000";

                            LocalDateTime publishDate = new LocalDateTime(pubDateStr, dtz);
                            LocalDateTime notifyrLocalDate = new LocalDateTime(notifyrDateStr, dtz);
                            if (dtz.isLocalDateTimeGap(publishDate)){
                                publishDate = publishDate.plusHours(1);
                            }
                            if (dtz.isLocalDateTimeGap(notifyrLocalDate)){
                                notifyrLocalDate = notifyrLocalDate.plusHours(1);
                            }
                            DateTime pubDate = publishDate.toDateTime();
                            DateTime notifyrDate = notifyrLocalDate.toDateTime();

                            article.setScore((!jsonItem.isNull("Score") ? jsonItem.getInt("Score") : -1));
                            article.setSource(!jsonItem.isNull("Source") ? jsonItem.getString("Source") : "");
                            article.setTitle(!jsonItem.isNull("Title") ? jsonItem.getString("Title") : "");
                            article.setAuthor(!jsonItem.isNull("Author") ? jsonItem.getString("Author") : "");
                            article.setDescription(!jsonItem.isNull("Description") ? jsonItem.getString("Description") : "");
                            article.setUrl(!jsonItem.isNull("URL") ? jsonItem.getString("URL") : "");
                            article.setIurl(!jsonItem.isNull("IURL") ? jsonItem.getString("IURL") : "");
                            article.setArticleNotifiedDate(notifyrDate);
                            article.setPublishDate(pubDate);
                            article.setFavourite(!jsonItem.isNull("IsFavourite") ? (jsonItem.getInt("IsFavourite") == 0 ? false : true) : false);
                            article.setShortLinkUrl(!jsonItem.isNull("ShortLinkUrl") ? jsonItem.getString("ShortLinkUrl") : "");
                            article.setRelatedInterests(!jsonItem.isNull("RelatedInterests") ? jsonItem.getString("RelatedInterests") : "");
                            article.setTimeAgo(!jsonItem.isNull("TimeAgo") ? jsonItem.getString("TimeAgo") : "");
                            article.setNotifiedTimeAgo(!jsonItem.isNull("NotifiedTimeAgo") ? jsonItem.getString("NotifiedTimeAgo") : "");
                            article.setRelatedInterestsURL(!jsonItem.isNull("RelatedInterestsIURL") ? jsonItem.getString("RelatedInterestsIURL") : "");
                            articles.add(article);
                        }
                        callback.onCompleted(articles);
                    } else if (notifyrType == Item) {
                        ArrayList<Item> items = new ArrayList<Item>();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            Item item = new Item();
                            JSONObject jsonItem = jsonArray.getJSONObject(i);
                            item.setId((!jsonItem.isNull("Id") ? jsonItem.getInt("Id") : -1));
                            item.setName(!jsonItem.isNull("Name") ? jsonItem.getString("Name") : "");
                            item.setIurl(!jsonItem.isNull("IUrl") ? jsonItem.getString("IUrl") : "");
                            item.setItemTypeId((!jsonItem.isNull("ItemTypeId") ? jsonItem.getInt("ItemTypeId") : -1));
                            item.setItemTypeName((!jsonItem.isNull("ItemTypeName") ? jsonItem.getString("ItemTypeName") : ""));
                            item.setPriority((!jsonItem.isNull("Priority") ? jsonItem.getInt("Priority") : -1));
                            items.add(item);
                        }
                        callback.onCompleted(items);
                    } else if (notifyrType == NotifyrObjects.AccessToken) {
                        AccessToken acesssTokenObj = new AccessToken();
                        acesssTokenObj.setTokenValue(jsonObject.getString("access_token"));
                        accessToken = acesssTokenObj.getTokenValue();
                        PreferenceManager.getDefaultSharedPreferences(context).edit().putString("accesstoken", accessToken).commit();
                        callback.onCompleted(null);
                    } else if (notifyrType == NotifyrObjects.NetworkStatus) {
                        String status = jsonObject.getString("NetworkStatus");
                        callback.onCompleted(status);
                    } else if (notifyrType == NotifyrObjects.Post  || notifyrType == NotifyrObjects.Put
                            || notifyrType == NotifyrObjects.JsonStatusResult) {
                        callback.onCompleted(response);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            super.onPostExecute(returnObjects);
        }
    };

    //endregion

    //region Helpers
    public enum NotifyrObjects {
        Article, Item, UserProfile, AccessToken, UserSetting, NetworkStatus, Post, Put,Get,JsonStatusResult
    }

    private String streamToString(InputStream is) throws IOException {
        StringBuilder sb = new StringBuilder();
        BufferedReader rd = new BufferedReader(new InputStreamReader(is));
        String line;
        while ((line = rd.readLine()) != null) {
            sb.append(line);
        }
        return sb.toString();
    }

    //endregion
}
