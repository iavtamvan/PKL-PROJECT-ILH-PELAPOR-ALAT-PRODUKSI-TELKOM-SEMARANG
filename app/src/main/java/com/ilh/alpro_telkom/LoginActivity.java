package com.ilh.alpro_telkom;

import android.Manifest;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.firebase.messaging.FirebaseMessaging;
import com.ilh.alpro_telkom.helper.Config;
import com.ilh.alpro_telkom.model.ResponseErrorModel;
import com.ilh.alpro_telkom.rest.ApiConfigServer;
import com.ilh.alpro_telkom.rest.ApiService;
import com.ilh.alpro_telkom.ui.pelapor.PelaporNavActivity;
import com.ilh.alpro_telkom.ui.teknisi.TeknisiNavActivity;
import com.ilh.alpro_telkom.ui.validator.ValidatorNavActivity;
import com.ilh.alpro_telkom.util.NotificationUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    private static final int RC_CAMERA_AND_LOCATION = 1;
    private static final String TAG = LoginActivity.class.getSimpleName();

    private EditText edtUsername;
    private EditText edtPassword;
    private TextView tvRegister;
    private Button btnLogin;

    private ResponseErrorModel responseErrorModel;

    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private NotificationManager mManager;
    private String regId;
    private String idUser;
    private String regIDShared;

    private SharedPreferences sharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();
        initView();
        methodRequiresTwoPermission();
        sharedPreferences = getSharedPreferences(Config.SHARED_PREF_NAME, MODE_PRIVATE);
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
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginRule();
//                ApiService apiService = ApiConfigServer.getApiService();
//                apiService.getDataFeedbackTeknisi("getFeedbackTeknisi", "3")
//                        .enqueue(new Callback<ResponseBody>() {
//                            @Override
//                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//                                if (response.isSuccessful()){
//                                    try {
//                                        JSONObject jsonObject  = new JSONObject(response.body().string());
//                                        String total_point = jsonObject.optString("total_point");
//                                        Toast.makeText(LoginActivity.this, "" + total_point, Toast.LENGTH_SHORT).show();
//                                        Toast.makeText(LoginActivity.this, "" + total_point, Toast.LENGTH_SHORT).show();
//                                        Toast.makeText(LoginActivity.this, "" + total_point, Toast.LENGTH_SHORT).show();
//                                        Toast.makeText(LoginActivity.this, "" + total_point, Toast.LENGTH_SHORT).show();
//                                        Toast.makeText(LoginActivity.this, "" + total_point, Toast.LENGTH_SHORT).show();
//                                    } catch (JSONException e) {
//                                        e.printStackTrace();
//                                    } catch (IOException e) {
//                                        e.printStackTrace();
//                                    }
//                                }
//                            }
//
//                            @Override
//                            public void onFailure(Call<ResponseBody> call, Throwable t) {
//                                Toast.makeText(LoginActivity.this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
//                            }
//                        });
            }
        });
        tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
            }
        });
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

    private void loginRule() {
        ApiService apiService = ApiConfigServer.getApiService();
        apiService.login(edtUsername.getText().toString().trim(), edtPassword.getText().toString().trim()).enqueue(new Callback<ResponseErrorModel>() {
            @Override
            public void onResponse(Call<ResponseErrorModel> call, Response<ResponseErrorModel> response) {
                if (response.isSuccessful()) {
                    responseErrorModel = response.body();
                    Toast.makeText(LoginActivity.this, "" + responseErrorModel.getRule(), Toast.LENGTH_SHORT).show();

                    if (responseErrorModel.getRule().contains("validator")) {
                        Config.sharedPref(LoginActivity.this, responseErrorModel.getId(), responseErrorModel.getUsername(), responseErrorModel.getRule());
                        idUser = sharedPreferences.getString(Config.SHARED_PREF_ID, "");
                        regIDShared = sharedPreferences.getString("regId", "");
                        updateRegID(regIDShared, idUser);
                        finishAffinity();
                        startActivity(new Intent(getApplicationContext(), ValidatorNavActivity.class));
                    } else if (responseErrorModel.getRule().contains("teknisi")) {
                        Config.sharedPref(LoginActivity.this, responseErrorModel.getId(), responseErrorModel.getUsername(), responseErrorModel.getRule());
                        idUser = sharedPreferences.getString(Config.SHARED_PREF_ID, "");
                        regIDShared = sharedPreferences.getString("regId", "");
                        updateRegID(regIDShared, idUser);
                        finishAffinity();
                        startActivity(new Intent(getApplicationContext(), TeknisiNavActivity.class));
                    } else if (responseErrorModel.getRule().contains("user")) {
                        Config.sharedPref(LoginActivity.this, responseErrorModel.getId(), responseErrorModel.getUsername(), responseErrorModel.getRule());
                        idUser = sharedPreferences.getString(Config.SHARED_PREF_ID, "");
                        regIDShared = sharedPreferences.getString("regId", "");
                        updateRegID(regIDShared, idUser);
                        finishAffinity();
                        startActivity(new Intent(getApplicationContext(), PelaporNavActivity.class));
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseErrorModel> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateRegID(String regId, String idUser) {
        ApiService apiService = ApiConfigServer.getApiService();
        apiService.updateRegID(regId, idUser)
                .enqueue(new Callback<ResponseErrorModel>() {
                    @Override
                    public void onResponse(Call<ResponseErrorModel> call, Response<ResponseErrorModel> response) {
                        if (response.isSuccessful()){

                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseErrorModel> call, Throwable t) {
                        Toast.makeText(LoginActivity.this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

    }


    @AfterPermissionGranted(RC_CAMERA_AND_LOCATION)
    private void methodRequiresTwoPermission() {
        String[] perms = {Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if (EasyPermissions.hasPermissions(this, perms)) {
            // Already have permission, do the thing
            // ...
        } else {
            // Do not have permissions, request them now
            EasyPermissions.requestPermissions(this, getString(R.string.app_name),
                    RC_CAMERA_AND_LOCATION, perms);
        }
    }

    private void initView() {
        edtUsername = findViewById(R.id.edt_username);
        edtPassword = findViewById(R.id.edt_password);
        btnLogin = findViewById(R.id.btn_login);
        tvRegister = findViewById(R.id.tv_register);
    }
}
