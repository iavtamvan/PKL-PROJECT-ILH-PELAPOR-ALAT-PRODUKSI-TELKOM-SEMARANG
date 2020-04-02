package com.ilh.alpro_telkom.helper;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.ilh.alpro_telkom.rest.serverSandec.ApiConfigServer;
import com.ilh.alpro_telkom.rest.serverSandec.ApiService;
import com.ilh.alpro_telkom.ui.front.LoginActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

public final class Config {
    public static final String SHARED_PREF_NAME = "ALPRO_TELKOM";
    public static final String SHARED_PREF_ID = "ALPRO_ID_USER";
    public static final String SHARED_PREF_USERNAME = "ALPRO_USERNAME";
    public static final String SHARED_PREF_RULE = "ALPRO_RULE";
    public static final String SHARED_PREF_FEEDBACK = "ALPRO_FEEDBACK";
    public static final String SHARED_PREF_REGID_FIREBASE = "ALPRO_REGID_FIREBASE";
    public static final String SHARED_PREF_ERROR_MSG = "ALPRO_ERROR_MSG";

    // global topic to receive app wide push notifications
    public static final String TOPIC_GLOBAL = "global";

    // broadcast receiver intent filters
    public static final String REGISTRATION_COMPLETE = "registrationComplete";
    public static final String PUSH_NOTIFICATION = "pushNotification";

    // id to handle the notification in the notification tray
    public static final int NOTIFICATION_ID = 100;
    public static final int NOTIFICATION_ID_BIG_IMAGE = 101;


    public static void sharedPref(Context context, String idUser, String username, String rule) {
        SharedPreferences sharedPreferences  = context.getSharedPreferences(Config.SHARED_PREF_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(Config.SHARED_PREF_ID, idUser);
        editor.putString(Config.SHARED_PREF_USERNAME, username);
        editor.putString(Config.SHARED_PREF_RULE, rule);


        editor.apply();
    }

    public static void pushNotif(final Context context, String tittle, String message, String pushtype, String regid){
        ApiService apiService = ApiConfigServer.getApiService();
        apiService.postDataNotif(tittle, message, pushtype, regid)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()){
                            try {
                                JSONObject jsonObject = new JSONObject(response.body().string());
                                String tittesle = jsonObject.optString("tittle");
//                                Toast.makeText(context, "" + tittesle, Toast.LENGTH_SHORT).show();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {

                    }
                });
    }

    public static void logout(Context context){
        SharedPreferences sharedPreferences  = context.getSharedPreferences(Config.SHARED_PREF_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(Config.SHARED_PREF_ID, "");
        editor.putString(Config.SHARED_PREF_USERNAME, "");
        editor.putString(Config.SHARED_PREF_RULE, "");
        editor.apply();

        context.startActivity(new Intent(context, LoginActivity.class));
    }
}
