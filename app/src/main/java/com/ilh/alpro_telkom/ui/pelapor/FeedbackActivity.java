package com.ilh.alpro_telkom.ui.pelapor;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.ilh.alpro_telkom.R;
import com.ilh.alpro_telkom.helper.Config;
import com.ilh.alpro_telkom.model.ResponseErrorModel;
import com.ilh.alpro_telkom.rest.ApiConfigServer;
import com.ilh.alpro_telkom.rest.ApiService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FeedbackActivity extends AppCompatActivity {

    private TextView tvNamaTeknisi;
    private Button btnFdb2jam;
    private Button btnFdb7jam;
    private Button btnFdb1hari;
    private Button btnFdbSesuai;
    private Button btnFdbTdkSesuai;
    private Button btnKirimFeedback;

    private String id_user;
    private String id_teknisi;
    private String id_pelapor;


    private double lamaPengerjaan, kesesuaian, totalFeedback;
    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        getSupportActionBar().setTitle("ISMIKUH :*");
        initView();
        id_user = getIntent().getStringExtra("ID_USER");
        id_teknisi = getIntent().getStringExtra("ID_TEKNISI");
        id_pelapor = getIntent().getStringExtra("ID_PELAPOR");
//        Toast.makeText(this, "id User > " + id_user + "id teknisi > "+ id_teknisi + "id pelapor > " + id_pelapor, Toast.LENGTH_SHORT).show();

        btnFdb2jam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnFdb2jam.setTextColor(R.color.colorBtn);
                btnFdb7jam.setVisibility(View.GONE);
                btnFdb1hari.setVisibility(View.GONE);
                lamaPengerjaan = 0.9;
            }
        });
        btnFdb7jam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnFdb7jam.setTextColor(R.color.colorBtn);
                btnFdb2jam.setVisibility(View.GONE);
                btnFdb1hari.setVisibility(View.GONE);
                lamaPengerjaan = 0.7;
            }
        });
        btnFdb1hari.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnFdb1hari.setTextColor(R.color.colorBtn);
                btnFdb2jam.setVisibility(View.GONE);
                btnFdb7jam.setVisibility(View.GONE);
                lamaPengerjaan = 0.3;
            }
        });

        btnFdbSesuai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnFdbSesuai.setTextColor(R.color.colorBtn);
                btnFdbTdkSesuai.setVisibility(View.GONE);
                kesesuaian = 1;

            }
        });
        btnFdbTdkSesuai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnFdbTdkSesuai.setTextColor(R.color.colorBtn);
                btnFdbSesuai.setVisibility(View.GONE);
                kesesuaian = 0.5;

            }
        });

        btnKirimFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                totalFeedback = lamaPengerjaan + kesesuaian;
                posFeedback(totalFeedback);
            }
        });

    }

    private void posFeedback(double totalFeedback) {
        ApiService apiService = ApiConfigServer.getApiService();
        apiService.postFeedback(id_user, id_teknisi, id_pelapor, totalFeedback)
                .enqueue(new Callback<ResponseErrorModel>() {
                    @Override
                    public void onResponse(Call<ResponseErrorModel> call, Response<ResponseErrorModel> response) {
                        if (response.isSuccessful()){
                            SharedPreferences sharedPreferences = getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString(Config.SHARED_PREF_FEEDBACK, "Sukses");
                            editor.apply();
                            Toast.makeText(FeedbackActivity.this, "Sukses Mengirim Feedback", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(FeedbackActivity.this, "Gagal Mengirim Feedback", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseErrorModel> call, Throwable t) {

                    }
                });
    }

    private void initView() {
        tvNamaTeknisi = findViewById(R.id.tv_nama_teknisi);
        btnFdb2jam = findViewById(R.id.btn_fdb_2jam);
        btnFdb7jam = findViewById(R.id.btn_fdb_7jam);
        btnFdb1hari = findViewById(R.id.btn_fdb_1hari);
        btnFdbSesuai = findViewById(R.id.btn_fdb_sesuai);
        btnFdbTdkSesuai = findViewById(R.id.btn_fdb_tdk_sesuai);
        btnKirimFeedback = findViewById(R.id.btn_kirim_feedback);
    }
}
