package com.ilh.alpro_telkom.ui.validator.presenter;

import android.content.Context;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ilh.alpro_telkom.model.PelaporModel;
import com.ilh.alpro_telkom.rest.ApiConfigServer;
import com.ilh.alpro_telkom.rest.ApiService;
import com.ilh.alpro_telkom.ui.validator.adapter.ValidatorAdapter;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DataPelaporPresenter {
    private ArrayList<PelaporModel> pelaporModels;
    private ValidatorAdapter validatorAdapter;
    public void getData(final Context context, final RecyclerView rv) {
        pelaporModels = new ArrayList<>();
        ApiService apiService = ApiConfigServer.getApiService();
        apiService.getAllData("dataPelapor").enqueue(new Callback<ArrayList<PelaporModel>>() {
            @Override
            public void onResponse(Call<ArrayList<PelaporModel>> call, Response<ArrayList<PelaporModel>> response) {
                if (response.isSuccessful()){
                    pelaporModels = response.body();
                    rv.setLayoutManager(new LinearLayoutManager(context));
                    validatorAdapter = new ValidatorAdapter(context, pelaporModels);
                    validatorAdapter.notifyDataSetChanged();
                    rv.setAdapter(validatorAdapter);
                }
            }

            @Override
            public void onFailure(Call<ArrayList<PelaporModel>> call, Throwable t) {
                Toast.makeText(context, "Periksa Koneksi Anda", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
