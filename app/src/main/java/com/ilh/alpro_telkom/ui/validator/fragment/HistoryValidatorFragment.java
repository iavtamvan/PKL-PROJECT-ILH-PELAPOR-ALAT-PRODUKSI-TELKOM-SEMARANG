package com.ilh.alpro_telkom.ui.validator.fragment;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.ilh.alpro_telkom.R;
import com.ilh.alpro_telkom.ui.validator.adapter.ValidatorHistoryAdapter;
import com.ilh.alpro_telkom.helper.Config;
import com.ilh.alpro_telkom.model.PelaporModel;
import com.ilh.alpro_telkom.rest.ApiConfigServer;
import com.ilh.alpro_telkom.rest.ApiService;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class HistoryValidatorFragment extends Fragment {
    private RecyclerView rv;
    private ArrayList<PelaporModel> pelaporModels;
    private ValidatorHistoryAdapter validatorHistoryAdapter;

    private String idUser;

    public HistoryValidatorFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_history_validator, container, false);
        initView(view);
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        idUser = sharedPreferences.getString(Config.SHARED_PREF_ID,"");
        getData();
        return view;
    }


    public void getData() {
        ApiService apiService = ApiConfigServer.getApiService();
        apiService.getHistory("onProgress", idUser).enqueue(new Callback<ArrayList<PelaporModel>>() {
            @Override
            public void onResponse(Call<ArrayList<PelaporModel>> call, Response<ArrayList<PelaporModel>> response) {
                if (response.isSuccessful()){
                    pelaporModels = response.body();
                    rv.setLayoutManager(new LinearLayoutManager(getActivity()));
                    validatorHistoryAdapter = new ValidatorHistoryAdapter(getActivity(), pelaporModels);
                    validatorHistoryAdapter.notifyDataSetChanged();
                    rv.setAdapter(validatorHistoryAdapter);
                }
            }

            @Override
            public void onFailure(Call<ArrayList<PelaporModel>> call, Throwable t) {
                Toast.makeText(getActivity(), "" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void initView(View view) {
        rv = view.findViewById(R.id.rv);
    }

}
