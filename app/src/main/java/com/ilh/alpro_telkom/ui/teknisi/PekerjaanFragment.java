package com.ilh.alpro_telkom.ui.teknisi;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.ilh.alpro_telkom.R;
import com.ilh.alpro_telkom.adapter.TeknisiAdapter;
import com.ilh.alpro_telkom.model.PelaporModel;
import com.ilh.alpro_telkom.rest.ApiConfigServer;
import com.ilh.alpro_telkom.rest.ApiService;
import com.ilh.alpro_telkom.ui.teknisi.presenter.PekerjaanPresnter;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class PekerjaanFragment extends Fragment {
    private PekerjaanPresnter pekerjaanPresnter;

    private RecyclerView rv;
    private ArrayList<PelaporModel> pelaporModels;
    private TeknisiAdapter teknisiAdapter;

    public PekerjaanFragment() {
        // Required empty public constructor

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_pekerjaan, container, false);
        initView(view);
        pekerjaanPresnter = new PekerjaanPresnter();
        pekerjaanPresnter.getData(getActivity(), rv);
        return view;
    }
    
    private void initView(View view) {
        rv = view.findViewById(R.id.rv);
    }
}
