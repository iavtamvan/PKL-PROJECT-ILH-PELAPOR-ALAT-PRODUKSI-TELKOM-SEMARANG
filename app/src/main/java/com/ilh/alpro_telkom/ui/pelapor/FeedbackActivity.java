package com.ilh.alpro_telkom.ui.pelapor;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
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
    private float proFuzzyLamaPengerjaan, proFuzzyKesesuaian, grafikLamaPengerjaan, grafikKesesuaianPengerjaan, decimalLamaPengerjaan, decimalKesesuaianPengerjaan, totalFeedback, totalFeedbackFuzzy;
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

        btnFdb2jam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnFdb2jam.setTextColor(R.color.colorBtn);
                btnFdb7jam.setVisibility(View.GONE);
                btnFdb1hari.setVisibility(View.GONE);
                lamaPengerjaanBobot = 0.99; // bobot
                lamaPengerjaan = 2; // jam
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
            }
        });

        btnFdbSesuai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnFdbSesuai.setTextColor(R.color.colorBtn);
                btnFdbTdkSesuai.setVisibility(View.GONE);
                kesesuaianBobot = 0.44; // bobot
                kesesuaian = 1; // sesuai

            }
        });
        btnFdbTdkSesuai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnFdbTdkSesuai.setTextColor(R.color.colorBtn);
                btnFdbSesuai.setVisibility(View.GONE);
                kesesuaianBobot = 0.88; // bobot
                kesesuaian = 0; // tidak sesuai
            }
        });

        btnKirimFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (String.valueOf(lamaPengerjaanBobot).isEmpty() || String.valueOf(kesesuaianBobot).isEmpty()) {
                    Toast.makeText(FeedbackActivity.this, "Cek Feedback Anda", Toast.LENGTH_SHORT).show();
                } else {
                    if (lamaPengerjaan == 2 && kesesuaian == 1) {
                        fuzzyLogic();

//                        totalFeedback = (float) (lamaPengerjaanBobot + kesesuaianBobot);
//
//                        proFuzzyLamaPengerjaan = (float) (lamaPengerjaanBobot / totalFeedback * 100);
//                        proFuzzyKesesuaian = (float) (kesesuaianBobot / totalFeedback * 100);
//
//                        grafikLamaPengerjaan = (float) (proFuzzyLamaPengerjaan * 0.2 / 100);
//                        grafikKesesuaianPengerjaan = (float) (proFuzzyKesesuaian * 0.6 / 100);
//
////                    if (proFuzzyLamaPengerjaan == 0) grafikLamaPengerjaan = 1;
////                    else grafikLamaPengerjaan =  (proFuzzyLamaPengerjaan * 0.2 / 100);
//
//                        decimalLamaPengerjaan = Float.parseFloat(new DecimalFormat("#.##").format(grafikLamaPengerjaan));
//                        decimalKesesuaianPengerjaan = Float.parseFloat(new DecimalFormat("#.##").format(grafikKesesuaianPengerjaan));
//
//                        totalFeedbackFuzzy = decimalLamaPengerjaan / decimalKesesuaianPengerjaan;

                        tvNamaTeknisi.setText("Sangat Bagus");
                    } else if (lamaPengerjaan == 2 && kesesuaian == 0) {
                        fuzzyLogic();
//                        totalFeedback = (float) (lamaPengerjaanBobot + kesesuaianBobot);
//
//                        proFuzzyLamaPengerjaan = (float) (lamaPengerjaanBobot / totalFeedback * 100);
//                        proFuzzyKesesuaian = (float) (kesesuaianBobot / totalFeedback * 100);
//
//                        grafikLamaPengerjaan = (float) (proFuzzyLamaPengerjaan * 0.2 / 100);
//                        grafikKesesuaianPengerjaan = (float) (proFuzzyKesesuaian * 0.6 / 100);
//
////                    if (proFuzzyLamaPengerjaan == 0) grafikLamaPengerjaan = 1;
////                    else grafikLamaPengerjaan =  (proFuzzyLamaPengerjaan * 0.2 / 100);
//
//                        decimalLamaPengerjaan = Float.parseFloat(new DecimalFormat("#.##").format(grafikLamaPengerjaan));
//                        decimalKesesuaianPengerjaan = Float.parseFloat(new DecimalFormat("#.##").format(grafikKesesuaianPengerjaan));
//
//                        totalFeedbackFuzzy = decimalLamaPengerjaan / decimalKesesuaianPengerjaan;

                        tvNamaTeknisi.setText("Buruk");
                    } else if (lamaPengerjaan == 7 && kesesuaian == 1) {
                        fuzzyLogic();
//                        totalFeedback = (float) (lamaPengerjaanBobot + kesesuaianBobot);
//
//                        proFuzzyLamaPengerjaan = (float) (lamaPengerjaanBobot / totalFeedback * 100);
//                        proFuzzyKesesuaian = (float) (kesesuaianBobot / totalFeedback * 100);
//
//                        grafikLamaPengerjaan = (float) (proFuzzyLamaPengerjaan * 0.2 / 100);
//                        grafikKesesuaianPengerjaan = (float) (proFuzzyKesesuaian * 0.6 / 100);
//
////                    if (proFuzzyLamaPengerjaan == 0) grafikLamaPengerjaan = 1;
////                    else grafikLamaPengerjaan =  (proFuzzyLamaPengerjaan * 0.2 / 100);
//
//                        decimalLamaPengerjaan = Float.parseFloat(new DecimalFormat("#.##").format(grafikLamaPengerjaan));
//                        decimalKesesuaianPengerjaan = Float.parseFloat(new DecimalFormat("#.##").format(grafikKesesuaianPengerjaan));
//
//                        totalFeedbackFuzzy = decimalLamaPengerjaan / decimalKesesuaianPengerjaan;

                        tvNamaTeknisi.setText("Bagus");
                    } else if (lamaPengerjaan == 7 && kesesuaian == 0) {
                        fuzzyLogic();
//                        totalFeedback = (float) (lamaPengerjaanBobot + kesesuaianBobot);
//
//                        proFuzzyLamaPengerjaan = (float) (lamaPengerjaanBobot / totalFeedback * 100);
//                        proFuzzyKesesuaian = (float) (kesesuaianBobot / totalFeedback * 100);
//
//                        grafikLamaPengerjaan = (float) (proFuzzyLamaPengerjaan * 0.2 / 100);
//                        grafikKesesuaianPengerjaan = (float) (proFuzzyKesesuaian * 0.6 / 100);
//
////                    if (proFuzzyLamaPengerjaan == 0) grafikLamaPengerjaan = 1;
////                    else grafikLamaPengerjaan =  (proFuzzyLamaPengerjaan * 0.2 / 100);
//
//                        decimalLamaPengerjaan = Float.parseFloat(new DecimalFormat("#.##").format(grafikLamaPengerjaan));
//                        decimalKesesuaianPengerjaan = Float.parseFloat(new DecimalFormat("#.##").format(grafikKesesuaianPengerjaan));
//
//                        totalFeedbackFuzzy = decimalLamaPengerjaan / decimalKesesuaianPengerjaan;

                        tvNamaTeknisi.setText("Buruk");
                    } else if (lamaPengerjaan == 1 && kesesuaian == 1) {
                        fuzzyLogic();
//                        totalFeedback = (float) (lamaPengerjaanBobot + kesesuaianBobot);
//
//                        proFuzzyLamaPengerjaan = (float) (lamaPengerjaanBobot / totalFeedback * 100);
//                        proFuzzyKesesuaian = (float) (kesesuaianBobot / totalFeedback * 100);
//
//                        grafikLamaPengerjaan = (float) (proFuzzyLamaPengerjaan * 0.2 / 100);
//                        grafikKesesuaianPengerjaan = (float) (proFuzzyKesesuaian * 0.6 / 100);
//
////                    if (proFuzzyLamaPengerjaan == 0) grafikLamaPengerjaan = 1;
////                    else grafikLamaPengerjaan =  (proFuzzyLamaPengerjaan * 0.2 / 100);
//
//                        decimalLamaPengerjaan = Float.parseFloat(new DecimalFormat("#.##").format(grafikLamaPengerjaan));
//                        decimalKesesuaianPengerjaan = Float.parseFloat(new DecimalFormat("#.##").format(grafikKesesuaianPengerjaan));
//
//                        totalFeedbackFuzzy = decimalLamaPengerjaan / decimalKesesuaianPengerjaan;

                        tvNamaTeknisi.setText("Bagus");
                    } else if (lamaPengerjaan == 1 && kesesuaian == 0) {
                        fuzzyLogic();
//                        totalFeedback = (float) (lamaPengerjaanBobot + kesesuaianBobot);
//
//                        proFuzzyLamaPengerjaan = (float) (lamaPengerjaanBobot / totalFeedback * 100);
//                        proFuzzyKesesuaian = (float) (kesesuaianBobot / totalFeedback * 100);
//
//                        grafikLamaPengerjaan = (float) (proFuzzyLamaPengerjaan * 0.2 / 100);
//                        grafikKesesuaianPengerjaan = (float) (proFuzzyKesesuaian * 0.6 / 100);
//
////                    if (proFuzzyLamaPengerjaan == 0) grafikLamaPengerjaan = 1;
////                    else grafikLamaPengerjaan =  (proFuzzyLamaPengerjaan * 0.2 / 100);
//
//                        decimalLamaPengerjaan = Float.parseFloat(new DecimalFormat("#.##").format(grafikLamaPengerjaan));
//                        decimalKesesuaianPengerjaan = Float.parseFloat(new DecimalFormat("#.##").format(grafikKesesuaianPengerjaan));
//
//                        totalFeedbackFuzzy = decimalLamaPengerjaan / decimalKesesuaianPengerjaan;

                        tvNamaTeknisi.setText("Buruk");
                    }
                }

                // rumus fuzzynya

//                totalFeedback = Float.parseFloat(new DecimalFormat("#.##").format(lamaPengerjaanBobot + kesesuaianBobot));
            }
        });

    }

    private void fuzzyLogic() {
        totalFeedback = (float) (lamaPengerjaanBobot + kesesuaianBobot);

        proFuzzyLamaPengerjaan = (float) (lamaPengerjaanBobot / totalFeedback * 100);
        proFuzzyKesesuaian = (float) (kesesuaianBobot / totalFeedback * 100);

        grafikLamaPengerjaan = (float) (proFuzzyLamaPengerjaan * 0.22 / 100);
        grafikKesesuaianPengerjaan = (float) (proFuzzyKesesuaian * 0.22 / 100);

//                    if (proFuzzyLamaPengerjaan == 0) grafikLamaPengerjaan = 1;
//                    else grafikLamaPengerjaan =  (proFuzzyLamaPengerjaan * 0.2 / 100);

        decimalLamaPengerjaan = Float.parseFloat(new DecimalFormat("#.##").format(grafikLamaPengerjaan));
        decimalKesesuaianPengerjaan = Float.parseFloat(new DecimalFormat("#.##").format(grafikKesesuaianPengerjaan));


        tvDecKes.setText(String.valueOf(decimalKesesuaianPengerjaan));
        tvDecLama.setText(String.valueOf(decimalLamaPengerjaan));

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
