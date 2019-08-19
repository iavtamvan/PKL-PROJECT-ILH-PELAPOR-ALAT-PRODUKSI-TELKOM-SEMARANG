package com.ilh.alpro_telkom.ui.pelapor;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ilh.alpro_telkom.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class HistoriPelaporFragment extends Fragment {


    public HistoriPelaporFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_histori_pelapor, container, false);
    }

}
