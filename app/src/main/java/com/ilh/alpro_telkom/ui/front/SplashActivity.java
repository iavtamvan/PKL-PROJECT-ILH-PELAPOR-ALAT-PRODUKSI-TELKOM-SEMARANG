package com.ilh.alpro_telkom.ui.front;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessaging;
import com.ilh.alpro_telkom.R;
import com.ilh.alpro_telkom.helper.Config;
import com.ilh.alpro_telkom.ui.pelapor.activity.PelaporNavActivity;
import com.ilh.alpro_telkom.ui.teknisi.activity.TeknisiNavActivity;
import com.ilh.alpro_telkom.ui.validator.activity.ValidatorNavActivity;
import com.ilh.alpro_telkom.util.NotificationUtils;

public class SplashActivity extends AppCompatActivity {

    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private static final String TAG = SplashActivity.class.getSimpleName();
    private String regId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        getSupportActionBar().hide();
        displayFirebaseRegId();
        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                // checking for type intent filter
                if (intent.getAction().equals(Config.REGISTRATION_COMPLETE)) {
                    // gcm successfully registered
                    // now subscribe to `global` topic to receive app wide notifications
                    FirebaseMessaging.getInstance().subscribeToTopic(Config.TOPIC_GLOBAL);

                    displayFirebaseRegId();

                } else if (intent.getAction().equals(Config.PUSH_NOTIFICATION)) {
                    // new push notification is received

                    String message = intent.getStringExtra("message");
                }
            }
        };

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                SharedPreferences sp = getSharedPreferences(Config.SHARED_PREF_NAME, MODE_PRIVATE);
                String username = sp.getString(Config.SHARED_PREF_USERNAME, "");
                String rule = sp.getString(Config.SHARED_PREF_RULE, "");

                // TODO jika belum masuk ke LoginActivity
                if (username.equalsIgnoreCase("") || TextUtils.isEmpty(username)){
                    finishAffinity();
                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                }
                // TODO jika sudah nantinya akan masuk ke Home
                else {
                    if (rule.contains("user")){
                        finishAffinity();
                        startActivity(new Intent(getApplicationContext(), PelaporNavActivity.class));
                    } else if (rule.contains("validator")){
                        finishAffinity();
                        startActivity(new Intent(getApplicationContext(), ValidatorNavActivity.class));
                    } else if (rule.contains("teknisi")){
                        finishAffinity();
                        startActivity(new Intent(getApplicationContext(), TeknisiNavActivity.class));
                    }

                }
            }
        }, 2000);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // register GCM registration complete receiver
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.REGISTRATION_COMPLETE));

        // register new push message receiver
        // by doing this, the activity will be notified each time a new message arrives
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.PUSH_NOTIFICATION));

        // clear the notification area when the app is opened
        NotificationUtils.clearNotifications(getApplicationContext());
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
    }

    // Fetches reg id from shared preferences
    // and displays on the screen
    private void displayFirebaseRegId() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF_NAME, 0);
        regId = pref.getString("regId", null);
        Log.e(TAG, "Firebase reg id: " + regId);
    }


}
