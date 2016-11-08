package com.notifyrapp.www.notifyr.Business;

import android.content.Context;

import com.notifyrapp.www.notifyr.Data.RepositoryBuilder;
import com.notifyrapp.www.notifyr.Model.Article;
import com.notifyrapp.www.notifyr.Data.WebApi;
import com.notifyrapp.www.notifyr.Data.Repository;
import com.notifyrapp.www.notifyr.Model.UserProfile;

import org.json.JSONException;

import java.io.IOException;
import java.util.List;


public class Business {

    /* The View Context */
    Context context;

    /* Data comes from the Notifyr Web Api */
    WebApi webApi;

    /* Data comes from local SQL Lite */
    Repository repo;

    /* Builds Databases and Tables */
    RepositoryBuilder repoBuilder;

    public Business(Context context)
    {
        this.context = context;
    }

    //region Article

    /* Get All Articles for All Items */
    public List<Article> GetArticles(int skip, int take,String sortBy,int itemTypeId) {
        return webApi.GetArticles(skip,take,sortBy,itemTypeId);
    }

    //endregion

    //region Item

    //endregion

    //region Data Access
    public Boolean CheckIfDatabaseExists()
    {
        return new RepositoryBuilder(context).CheckIfDatabaseExists();
    }
    public Boolean CreateNotifyrDatabase(String userId)
    {
        return new RepositoryBuilder(context).CreateNotifyrDatabase(userId);
    }

    //endregion

    //region Articles
    public void GetItemArticles(long itemId,int skip,int take,String sortBy, Runnable callback){
        new WebApi(context).GetItemArticles(itemId,skip,take,sortBy,callback);
    }
    //endregion

    //region User Accounts
    public void RegisterAccount(String userName, String password, Runnable callback){
        new WebApi(context).RegisterUserProfile(userName,password,callback);
    }


    public void UpdateToken(Runnable callback)
    {
        new WebApi(context).GetAccessToken(callback);
    }
    //endregion


}
