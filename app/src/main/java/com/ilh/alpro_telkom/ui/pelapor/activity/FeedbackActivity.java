package com.ilh.alpro_telkom.ui.pelapor.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
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

import java.text.DecimalFormat;

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


    private double lamaPengerjaanBobot, kesesuaianBobot;
    private float proFuzzyLamaPengerjaan;
    private float proFuzzyKesesuaian;
    private float grafikLamaPengerjaan;
    private float grafikKesesuaianPengerjaan;
    private float decimalLamaPengerjaan;
    private float decimalKesesuaianPengerjaan;
    private float totalFeedback;
    private float totalFeedbackFuzzy;
    private int lamaPengerjaan, kesesuaian;
    private TextView tvDecLama;
    private TextView tvDecKes;
    private TextView tvTotal;

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        getSupportActionBar().setTitle("Feedback Pelapor");
        initView();
        id_user = getIntent().getStringExtra("ID_USER");
        id_teknisi = getIntent().getStringExtra("ID_TEKNISI");
        id_pelapor = getIntent().getStringExtra("ID_PELAPOR");
//        Toast.makeText(this, "id User || " + id_user + "id teknisi || "+ id_teknisi + "id pelapor || " + id_pelapor, Toast.LENGTH_SHORT).show();

        // TODO 1 memberikan Bobot pada variabel yang telah di tetapkan
        btnFdb2jam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnFdb2jam.setTextColor(R.color.colorBtn);
                btnFdb7jam.setVisibility(View.GONE);
                btnFdb1hari.setVisibility(View.GONE);
                lamaPengerjaanBobot = 0.99; // bobot
                lamaPengerjaan = 2; // jam
                Log.d("fuzzy", "Kriteria Lama Pengerjaan : " + lamaPengerjaan);
                Log.d("fuzzy", "Pembobotan Lama Pengerjaan : " + lamaPengerjaanBobot);
            }
        });
        btnFdb7jam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnFdb7jam.setTextColor(R.color.colorBtn);
                btnFdb2jam.setVisibility(View.GONE);
                btnFdb1hari.setVisibility(View.GONE);
                lamaPengerjaanBobot = 0.77; // bobot
                lamaPengerjaan = 7;// jam
                Log.d("fuzzy", "Kriteria Lama Pengerjaan : " + lamaPengerjaan);
                Log.d("fuzzy", "Pembobotan Lama Pengerjaan : " + lamaPengerjaanBobot);
            }
        });
        btnFdb1hari.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnFdb1hari.setTextColor(R.color.colorBtn);
                btnFdb2jam.setVisibility(View.GONE);
                btnFdb7jam.setVisibility(View.GONE);
                lamaPengerjaanBobot = 0.55; // bobot
                lamaPengerjaan = 1; // hari
                Log.d("fuzzy", "Kriteria Lama Pengerjaan : " + lamaPengerjaan);
                Log.d("fuzzy", "Pembobotan Lama Pengerjaan : " + lamaPengerjaanBobot);
            }
        });

        btnFdbSesuai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnFdbSesuai.setTextColor(R.color.colorBtn);
                btnFdbTdkSesuai.setVisibility(View.GONE);
                kesesuaianBobot = 0.44; // bobot
                kesesuaian = 1; // sesuai
                Log.d("fuzzy", "Kriteria Lama Pengerjaan : " + lamaPengerjaan);
                Log.d("fuzzy", "Pembobotan Lama Pengerjaan : " + lamaPengerjaanBobot);

            }
        });
        btnFdbTdkSesuai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnFdbTdkSesuai.setTextColor(R.color.colorBtn);
                btnFdbSesuai.setVisibility(View.GONE);
                kesesuaianBobot = 0.88; // bobot
                kesesuaian = 0; // tidak sesuai
                Log.d("fuzzy", "Kriteria Lama Pengerjaan : " + lamaPengerjaan);
                Log.d("fuzzy", "Pembobotan Lama Pengerjaan : " + lamaPengerjaanBobot);
            }
        });

        // TODO 2 menentukan Aturan/rule pada Algoritma fuzzy logic
        btnKirimFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (String.valueOf(lamaPengerjaanBobot).isEmpty() || String.valueOf(kesesuaianBobot).isEmpty()) {
                    Toast.makeText(FeedbackActivity.this, "Cek Feedback Anda", Toast.LENGTH_SHORT).show();
                } else {
                    if (lamaPengerjaan == 2 && kesesuaian == 1) {
                        fuzzyLogic();
                        tvNamaTeknisi.setText("Sangat Bagus");
                        Log.d("fuzzy", "Aturan/Rule 1 : Sangat Bagus");
                    } else if (lamaPengerjaan == 2 && kesesuaian == 0) {
                        fuzzyLogic();
                        tvNamaTeknisi.setText("Buruk");
                        Log.d("fuzzy", "Aturan/Rule 1 : Buruk");
                    } else if (lamaPengerjaan == 7 && kesesuaian == 1) {
                        fuzzyLogic();
                        tvNamaTeknisi.setText("Bagus");
                        Log.d("fuzzy", "Aturan/Rule 1 : Bagus");
                    } else if (lamaPengerjaan == 7 && kesesuaian == 0) {
                        fuzzyLogic();
                        tvNamaTeknisi.setText("Buruk");
                        Log.d("fuzzy", "Aturan/Rule 1 : Buruk");
                    } else if (lamaPengerjaan == 1 && kesesuaian == 1) {
                        fuzzyLogic();
                        tvNamaTeknisi.setText("Bagus");
                        Log.d("fuzzy", "Aturan/Rule 1 : Bagus");
                    } else if (lamaPengerjaan == 1 && kesesuaian == 0) {
                        fuzzyLogic();
                        tvNamaTeknisi.setText("Buruk");
                        Log.d("fuzzy", "Aturan/Rule 1 : Buruk");
                    }
                }
            }
        });

    }

    // TODO 3 menghitung variable yang sebelumnya sudah ditentukan dengan menggunakan grafik
    private void fuzzyLogic() {

        // Rumus 1
        totalFeedback = (float) (lamaPengerjaanBobot + kesesuaianBobot);
        Log.d("fuzzy", "Total Feedback (LamaPengerjaanBobot + KesesuaianBobot : " + totalFeedback);

        // Rumus 2
        proFuzzyLamaPengerjaan = (float) (lamaPengerjaanBobot / totalFeedback * 100); // 100 adalah persen
        proFuzzyKesesuaian = (float) (kesesuaianBobot / totalFeedback * 100); // 100 adalah persen
        Log.d("fuzzy", "proFuzzyLamaPengerjaan (lamaPengerjaanBobot / totalFeedback * 100) : " + proFuzzyLamaPengerjaan);
        Log.d("fuzzy", "proFuzzyKesesuaian (kesesuaianBobot / totalFeedback * 100) : " + proFuzzyKesesuaian);

        // Rumus 3
        grafikLamaPengerjaan = (float) (proFuzzyLamaPengerjaan * 0.22 / 100); // 0.2 adalah range per bobot || 100 adalah persen
        grafikKesesuaianPengerjaan = (float) (proFuzzyKesesuaian * 0.22 / 100); // 0.2 adalah range per bobot || 100 adalah persen
        Log.d("fuzzy", "GrafikLamaPengerjaan (proFuzzyLamaPengerjaan * 0.22 / 100) : " + grafikLamaPengerjaan);
        Log.d("fuzzy", "GrafikKesesuaianPengerjaan (proFuzzyLamaPengerjaan * 0.22 / 100) : " + grafikKesesuaianPengerjaan);

        // TODO 4 hasil dar perhitungan variabel menggunakan pendekatan grafik
        decimalLamaPengerjaan = Float.parseFloat(new DecimalFormat("#.##").format(grafikLamaPengerjaan));
        decimalKesesuaianPengerjaan = Float.parseFloat(new DecimalFormat("#.##").format(grafikKesesuaianPengerjaan));
        Log.d("fuzzy", "OutputDecimalLamaPengerjaan : " + decimalLamaPengerjaan);
        Log.d("fuzzy", "OutputDecimalKesesuaianPengerjaan : " + decimalKesesuaianPengerjaan);

        tvDecKes.setText(String.valueOf(decimalKesesuaianPengerjaan));
        tvDecLama.setText(String.valueOf(decimalLamaPengerjaan));

        // TODO 5 OUTPUT dari fuzzy Logic.
        totalFeedbackFuzzy = Float.parseFloat(new DecimalFormat("#.##").format(decimalLamaPengerjaan / decimalKesesuaianPengerjaan));
        tvTotal.setText(String.valueOf(totalFeedbackFuzzy));

        posFeedback(tvTotal.getText().toString().trim(), tvDecLama.getText().toString().trim() , tvDecKes.getText().toString().trim());
    }

    private void posFeedback(String totalFeedback, String decLamaPengerjaan, String decKesesuaianPengerjaan) {
        ApiService apiService = ApiConfigServer.getApiService();
        apiService.postFeedback(id_user, id_teknisi, id_pelapor, totalFeedback, decLamaPengerjaan, decKesesuaianPengerjaan, "")
                .enqueue(new Callback<ResponseErrorModel>() {
                    @Override
                    public void onResponse(Call<ResponseErrorModel> call, Response<ResponseErrorModel> response) {
                        if (response.isSuccessful()) {
                            SharedPreferences sharedPreferences = getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString(Config.SHARED_PREF_FEEDBACK, "Sukses");
                            editor.apply();
                            Toast.makeText(FeedbackActivity.this, "Sukses Mengirim Feedback", Toast.LENGTH_SHORT).show();
                            finishAffinity();
                            startActivity(new Intent(getApplicationContext(), PelaporNavActivity.class));
                        } else {
                            Toast.makeText(FeedbackActivity.this, "Gagal Mengirim Feedback", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseErrorModel> call, Throwable t) {
                        Toast.makeText(FeedbackActivity.this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
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
        tvDecLama = findViewById(R.id.tv_dec_lama);
        tvDecKes = findViewById(R.id.tv_dec_kes);
        tvTotal = findViewById(R.id.tv_total);
    }
}
