package com.notifyrapp.www.notifyr.Data;

import android.content.Context;
import android.os.AsyncTask;
import android.preference.PreferenceManager;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.notifyrapp.www.notifyr.Model.Article;
import com.notifyrapp.www.notifyr.Model.UserProfile;

import org.json.JSONException;
import org.json.JSONObject;

import static java.lang.Thread.sleep;

public class WebApi {

    /* Private Fields */
    private String apiBaseUrl = "http://www.notifyr.ca/dev/service/api/";
    //private String apiBaseUrlDev = "http://www.notifyr.ca/dev/service/api/";
    private Context context;
    private String accesToken;

    /* Constructor */
    public WebApi(Context context)
    {
        this.context = context;
    }

    //region User Profile
    public void RegisterUserProfile(String userName, String password, Runnable callback) throws IOException, JSONException {
        String urlPath = "Account/RegisterGuest";
        String url = apiBaseUrl + urlPath;

        if(postJSONObjectFromURL.getStatus().equals(AsyncTask.Status.PENDING)) {
            postJSONObjectFromURL.execute(url,context,callback,NotifyrObjects.UserProfile);
        }
    }
    //endregion

    //region Articles
    public List<Article> GetArticles(int skip, int take,String sortBy, int itemTypeId )
    {
     //   Map<String,Object> map = new ObjectMapper().readValue(new URL("http://dot.com/api/?customerId=1234").openStream(), Map.class);
        List<Article> articles = new ArrayList<Article>();
        Article a = new Article();
        a.setId(2321);
        a.setTitle("Apple Releases new iPhone");
        a.setSource("CNN");
        articles.add(a);
        return articles;
    }
    //endregion

    //region Network Requests
    private AsyncTask<Object, Void, List<Object>> postJSONObjectFromURL = new AsyncTask<Object, Void, List<Object>>() {

        @Override
        protected List<Object> doInBackground(Object... params) {
            HttpURLConnection httpcon;
            String urlString = (String) params[0];
            Context parentContext = (Context) params[1];
            Runnable callback = (Runnable) params[2];
            NotifyrObjects notifyrObjectType = (NotifyrObjects) params[3];

            String result = "";
            try {

                URL url = new URL(urlString);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(10000 /* milliseconds */);
                conn.setConnectTimeout(15000 /* milliseconds */);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);   //* uses POST
                conn.setDoOutput(true);
                conn.connect();
                InputStream stream = conn.getInputStream();
                result = streamToString(stream);
                System.out.println("JSON: " + result);

            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                List<Object> returnObj = new ArrayList<>();
                returnObj.add(new JSONObject(result));
                returnObj.add(callback);
                returnObj.add(notifyrObjectType);
                returnObj.add(context);
                return returnObj;//new JSONObject(result);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<Object> returnObjects) {
            // Save to Local Settings and Database
            JSONObject jsonObject = (JSONObject) returnObjects.get(0);
            Runnable callback = (Runnable) returnObjects.get(1);
            NotifyrObjects notifyrType = (NotifyrObjects) returnObjects.get(3);

            try {

                if (notifyrType == NotifyrObjects.UserProfile) {
                    Repository repo = new Repository(context);
                    UserProfile userProfile = new UserProfile();
                    userProfile.UserId = jsonObject.getString("UserId");
                    userProfile.Email = jsonObject.getString("Email");
                    repo.SaveUserProfile(userProfile);
                    PreferenceManager.getDefaultSharedPreferences(context).edit().putString("userid", userProfile.UserId).commit();
                }

                if (notifyrType == NotifyrObjects.Article) {


                }

                if (notifyrType == NotifyrObjects.Item) {


                }


            } catch (JSONException e) {
                e.printStackTrace();
            }

            callback.run();
            super.onPostExecute(returnObjects);
        }
    };
    //endregion

    //region Helpers
    public enum NotifyrObjects {
        Article, Item, UserProfile
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
