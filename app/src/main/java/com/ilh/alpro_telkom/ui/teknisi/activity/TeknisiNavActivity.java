package com.ilh.alpro_telkom.ui.teknisi.activity;

import android.content.SharedPreferences;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.view.View;

import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;

import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;
import com.ilh.alpro_telkom.R;
import com.ilh.alpro_telkom.helper.Config;
import com.ilh.alpro_telkom.rest.ApiConfigServer;
import com.ilh.alpro_telkom.rest.ApiService;
import com.ilh.alpro_telkom.ui.teknisi.fragment.HistoryTeknisiFragment;
import com.ilh.alpro_telkom.ui.teknisi.fragment.PekerjaanFragment;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import android.view.Menu;
import android.widget.FrameLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TeknisiNavActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private FragmentManager fragmentManager;
    private FrameLayout fmViewPagerNav;
    private ViewPager viewpager;

    private String idUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teknisi_nav);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        SharedPreferences sharedPreferences = getSharedPreferences(Config.SHARED_PREF_NAME, MODE_PRIVATE);
        idUser = sharedPreferences.getString(Config.SHARED_PREF_ID, "");
        View headView = navigationView.getHeaderView(0);
        final TextView tvPenilaianDari = headView.findViewById(R.id.tv_penilaian_dari);
        final TextView tvPredikat = headView.findViewById(R.id.tv_predikat);
        final RatingBar ratingBar = headView.findViewById(R.id.rb_teknisi);


        //                    totalFeedback = (float) (lamaPengerjaanBobot + kesesuaianBobot);
//
//                    proFuzzyLamaPengerjaan = (float) (lamaPengerjaanBobot/totalFeedback * 100);
//                    proFuzzyKesesuaian = (float) (kesesuaianBobot/totalFeedback * 100);
//
//                    grafikLamaPengerjaan = (float) (proFuzzyLamaPengerjaan * 0.2 / 100);
//                    grafikKesesuaianPengerjaan = (float) (proFuzzyKesesuaian * 0.6 / 100);
//
////                    if (proFuzzyLamaPengerjaan == 0) grafikLamaPengerjaan = 1;
////                    else grafikLamaPengerjaan =  (proFuzzyLamaPengerjaan * 0.2 / 100);
//
//                    decimalLamaPengerjaan = Float.parseFloat(new DecimalFormat("#.##").format(grafikLamaPengerjaan));
//                    decimalKesesuaianPengerjaan = Float.parseFloat(new DecimalFormat("#.##").format(grafikKesesuaianPengerjaan));
//
//                    totalFeedbackFuzzy = decimalLamaPengerjaan / decimalKesesuaianPengerjaan;
//
//                    tvNamaTeknisi.setText("Sangat Bagus " + totalFeedbackFuzzy + " || " + decimalLamaPengerjaan + " || " + decimalKesesuaianPengerjaan);



        ApiService apiService = ApiConfigServer.getApiService();
        apiService.getDataFeedbackTeknisi("getFeedbackTeknisi", idUser)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            try {
                                JSONObject jsonObject = new JSONObject(response.body().string());
                                String total_pointRating = jsonObject.optString("total_point");
                                String total_fedback_teknisi = jsonObject.optString("total_fedback_teknisi");
                                String total_pelapor = jsonObject.optString("total_pelapor");

                                if (total_pointRating.equalsIgnoreCase("")) {
                                    ratingBar.setRating(0);
                                    tvPenilaianDari.setText("Belum ada penilaian");
//                                    Toast.makeText(TeknisiNavActivity.this, "Kosong", Toast.LENGTH_SHORT).show();
                                } else {
                                    ratingBar.setRating(Float.valueOf(total_pointRating));
//                                    tvPenilaianDari.setText("Penilaian " + total_fedback_teknisi + " dari " + total_pelapor + " Seluruh Pelapor " + "Poin anda " + total_pointRating);
                                    tvPenilaianDari.setText("Poin anda " + total_pointRating);

                                    // TODO 6 Disini tempatnya algoritma Fuzzy Logic rule 2
                                    double ratingToDouble = Double.parseDouble(total_pointRating);
                                    int totalPelaporToInt = Integer.parseInt(total_fedback_teknisi);

                                    if (ratingToDouble <= 0.9 || totalPelaporToInt <= 10) {
                                        tvPredikat.setText("Buruk");
                                    }

                                    else if (ratingToDouble >= 1 && ratingToDouble <=1.9 || totalPelaporToInt <= 20 ) {
                                        tvPredikat.setText("Buruk");
                                    }

                                    else if (ratingToDouble >2 && ratingToDouble <= 3.9 || totalPelaporToInt > 21 && totalPelaporToInt <= 30) {
                                        tvPredikat.setText("Bagus");
                                    }

                                    else if (ratingToDouble >4 && ratingToDouble <= 4.9 || totalPelaporToInt > 31 && totalPelaporToInt < 50){
                                        tvPredikat.setText("Sangat Bagus");
                                    }

                                    else {
                                        tvPredikat.setText("Sangat Bagus");
                                    }


                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Toast.makeText(TeknisiNavActivity.this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.fm_view_pager_nav, new PekerjaanFragment()).commit();
        getSupportActionBar().setTitle("Pekerjan ");
    }

    public void setState() {
        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.fm_view_pager_nav, new PekerjaanFragment()).commit();
        getSupportActionBar().setTitle("Pekerjan ");
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.teknisi_nav, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.data_teknisi) {
            fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.fm_view_pager_nav, new PekerjaanFragment()).commit();
            getSupportActionBar().setTitle("Pekerjan ");
        } else if (id == R.id.histori) {
            fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.fm_view_pager_nav, new HistoryTeknisiFragment()).commit();
            getSupportActionBar().setTitle("Histori ");
        } else if (id == R.id.logout) {
            Config.logout(TeknisiNavActivity.this);
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
