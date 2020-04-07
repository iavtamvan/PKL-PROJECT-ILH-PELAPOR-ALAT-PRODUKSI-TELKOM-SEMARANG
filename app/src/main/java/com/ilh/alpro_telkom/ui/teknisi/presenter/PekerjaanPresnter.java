package com.ilh.alpro_telkom.ui.teknisi.presenter;

import android.content.Context;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ilh.alpro_telkom.ui.teknisi.adapter.TeknisiAdapter;
import com.ilh.alpro_telkom.model.PelaporModel;
import com.ilh.alpro_telkom.rest.ApiConfigServer;
import com.ilh.alpro_telkom.rest.ApiService;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PekerjaanPresnter {
    private ArrayList<PelaporModel> pelaporModels;
    private TeknisiAdapter teknisiAdapter;


    public void getData(final Context context, final RecyclerView rv){
        pelaporModels = new ArrayList<>();
        ApiService apiService = ApiConfigServer.getApiService();
        apiService.getAllDataDisetujui("disetujuiValidator").enqueue(new Callback<ArrayList<PelaporModel>>() {
            @Override
            public void onResponse(Call<ArrayList<PelaporModel>> call, Response<ArrayList<PelaporModel>> response) {
                if (response.isSuccessful()){
                    pelaporModels = response.body();
                    rv.setLayoutManager(new LinearLayoutManager(context));
                    teknisiAdapter = new TeknisiAdapter(context, pelaporModels);
                    teknisiAdapter.notifyDataSetChanged();
                    rv.setAdapter(teknisiAdapter);
                }
            }

            @Override
            public void onFailure(Call<ArrayList<PelaporModel>> call, Throwable t) {
                Toast.makeText(context, "Periksa Koneksi Anda", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
