package com.notifyrapp.www.notifyr;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.support.v7.app.AppCompatActivity;

import android.content.Intent;

import com.notifyrapp.www.notifyr.Business.Business;
import com.notifyrapp.www.notifyr.Business.CallbackInterface;
import com.notifyrapp.www.notifyr.Model.Item;
import com.notifyrapp.www.notifyr.Model.UserSetting;

import java.util.ArrayList;

import static java.lang.Thread.sleep;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */



public class AppStartActivity extends AppCompatActivity{

    Context ctx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_start);
        Business biz = new Business(this);
        String userId = "";
        ctx = this;
        /* CHECK IF USER EXISTS  */
    //  PreferenceManager.getDefaultSharedPreferences(ctx).edit().putString("userid", "").commit();
        userId = PreferenceManager.getDefaultSharedPreferences(this).getString("userid", "");
       // userId = "77dbbcbf-ef0d-42b6-91c8-5d830b6b004b"; // TODO: Remove this line (Hardcoded Klein account)
        if(userId.equals(""))
        {
            Log.d("ACCOUNT_CHECK","No Account Found... Contacting Server to create one " + userId);
            //* CREATE ACCOUNT *//*
            try {
                biz.registerAccount("","",new CallbackInterface()
                {
                    @Override
                    public void onCompleted(Object data) {
                        // Running callback
                        Log.d("CALLBACK_CHECK","REACHED CALL BACK");
                        String userId = (String) data;
                        PreferenceManager.getDefaultSharedPreferences(ctx).edit().putString("userid", userId).commit();
                        //******* CREATE LOCAL DB HERE *******//*
                        Business business = new Business(ctx);
                        business.createNotifyrDatabase(userId);
                        UserSetting settings = business.getUserSettings();
                        Log.d("SETTINGS", String.valueOf(settings.getMaxNotificaitons()));
                        business.updateToken(new CallbackInterface() {
                            @Override
                            public void onCompleted(Object data) {
                                startActivity(new Intent(AppStartActivity.this, MainActivity.class));
                            }
                        });
                    }

                });
            } catch (Exception e) {
                e.printStackTrace();
                Log.d("ACCOUNT_CHECK","Account Failed: " + e.getMessage());
            }
            Log.d("ACCOUNT_CHECK","Account Created: " + userId);
        }
        else
        {

            Business business = new Business(ctx);
            if(!business.checkIfDatabaseExists()) {
                business.createNotifyrDatabase(userId);
                Log.d("DATABASE_CHECK","Account Exists... But Not Database: " + userId);
            }
            else {
                Log.d("DATABASE_CHECK", "Database Exists...: " + userId);
            }

            //* ACCOUNT EXISTS *//*
            Log.d("ACCOUNT_CHECK","Account Exists... Logging In As: " + userId);
            UserSetting settings = business.getUserSettings();

            new Business(ctx).updateToken(new CallbackInterface() {
                @Override
                public void onCompleted(Object data) {
                    new Business(ctx).getUserItemsFromLocal(new CallbackInterface()
                    {
                        @Override
                        public void onCompleted(Object data) {
                            // Running callback
                            Log.d("CALLBACK_CHECK","SAVED ITEMS TO LOCAL STORE");
                            ArrayList<Item> items = (ArrayList<Item>) data;
                            for (Item currentItem: items) {

                            }
                        }
                    });
                     startActivity(new Intent(AppStartActivity.this, MainActivity.class));
                }
            });
        }

        /* REGISTER FOR REMOTE NOTIFICATIONS */

    }

    private AsyncTask<Object, Void, String> createOrUpdateDatabase = new AsyncTask<Object, Void, String>() {

       //  if(createOrUpdateDatabase.getStatus().equals(AsyncTask.Status.PENDING)) {
            //createOrUpdateDatabase.execute(this,user);
        //        }

        @Override
        protected String doInBackground(Object... params) {

            /******* CREATE LOCAL DB HERE *******/
            Context context = (Context)params[0];
            String userId =  (String)params[1];
            Business business = new Business(context);
            business.createNotifyrDatabase(userId);
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //get sharedPreferences here
        }

        @Override
        protected void onPostExecute(String s) {
           // startActivity(new Intent(AppStartActivity.this, ArticleActivity.class));
            //do something as the execution completed, you can launch your real activity.
        }
    };


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }

}
