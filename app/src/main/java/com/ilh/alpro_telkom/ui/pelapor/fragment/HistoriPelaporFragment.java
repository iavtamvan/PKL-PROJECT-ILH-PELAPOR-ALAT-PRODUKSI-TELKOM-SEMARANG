package com.ilh.alpro_telkom.ui.pelapor.fragment;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.ilh.alpro_telkom.R;
import com.ilh.alpro_telkom.helper.Config;
import com.ilh.alpro_telkom.ui.pelapor.presenter.PelaporHistoriPresenter;

/**
 * A simple {@link Fragment} subclass.
 */
public class HistoriPelaporFragment extends Fragment {

    private PelaporHistoriPresenter pelaporHistoriPresenter;
    private RecyclerView rv;


    public HistoriPelaporFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_histori_pelapor, container, false);
        initView(view);
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        String idUser = sharedPreferences.getString(Config.SHARED_PREF_ID, "");


        pelaporHistoriPresenter = new PelaporHistoriPresenter();
        pelaporHistoriPresenter.getHistoriPelapor(getActivity(), "historiPelapor", idUser, rv);


        return view;
    }

    private void initView(View view) {
        rv = view.findViewById(R.id.rv);
    }
}
