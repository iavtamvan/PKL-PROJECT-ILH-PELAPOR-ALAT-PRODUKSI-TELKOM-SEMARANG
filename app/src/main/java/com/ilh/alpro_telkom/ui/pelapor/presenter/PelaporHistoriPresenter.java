package com.ilh.alpro_telkom.ui.pelapor.presenter;

import android.content.Context;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ilh.alpro_telkom.ui.pelapor.adapter.PelaporHistoryAdapter;
import com.ilh.alpro_telkom.model.PelaporModel;
import com.ilh.alpro_telkom.rest.ApiConfigServer;
import com.ilh.alpro_telkom.rest.ApiService;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PelaporHistoriPresenter {
    private ArrayList<PelaporModel> pelaporModels;
    private PelaporHistoryAdapter pelaporHistoryAdapter;

    public void getHistoriPelapor(final Context context, String change, String idUserAkun, final RecyclerView recyclerView){
        pelaporModels = new ArrayList<>();
        ApiService apiService = ApiConfigServer.getApiService();
        apiService.getHistoriPelapor(change, idUserAkun)
                .enqueue(new Callback<ArrayList<PelaporModel>>() {
                    @Override
                    public void onResponse(Call<ArrayList<PelaporModel>> call, Response<ArrayList<PelaporModel>> response) {
                        if (response.isSuccessful()){
                            pelaporModels = response.body();
                            pelaporHistoryAdapter = new PelaporHistoryAdapter(context, pelaporModels);
                            recyclerView.setLayoutManager(new LinearLayoutManager(context));
                            recyclerView.setAdapter(pelaporHistoryAdapter);
                            pelaporHistoryAdapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onFailure(Call<ArrayList<PelaporModel>> call, Throwable t) {
                        Toast.makeText(context, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

    }

}
