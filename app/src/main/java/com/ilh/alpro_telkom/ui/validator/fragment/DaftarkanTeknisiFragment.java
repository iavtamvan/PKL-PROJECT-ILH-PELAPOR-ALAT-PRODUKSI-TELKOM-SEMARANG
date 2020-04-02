package com.ilh.alpro_telkom.ui.validator.fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.ilh.alpro_telkom.R;
import com.ilh.alpro_telkom.model.ResponseErrorModel;
import com.ilh.alpro_telkom.rest.ApiConfigServer;
import com.ilh.alpro_telkom.rest.ApiService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class DaftarkanTeknisiFragment extends Fragment {


    private EditText edtNamaLengkap;
    private EditText edtUsername;
    private EditText edtPassword;
    private Button btnRegistrasi;
    private ResponseErrorModel responseErrorModel;

    public DaftarkanTeknisiFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_daftarkan_teknisi, container, false);
        initView(view);

        btnRegistrasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edtNamaLengkap.getText().toString().trim().isEmpty() || edtUsername.getText().toString().trim().isEmpty() || edtPassword.getText().toString().trim().isEmpty()) {
                    Toast.makeText(getActivity(), "Lengkapi data dahulu", Toast.LENGTH_SHORT).show();
                } else {

                    daftarkanTeknisi();
                }
            }
        });
        return view;
    }

    private void daftarkanTeknisi() {
        ApiService apiService = ApiConfigServer.getApiService();
        apiService.register(edtNamaLengkap.getText().toString().trim(), edtUsername.getText().toString().trim(), edtPassword.getText().toString().trim(), "", "user")
                .enqueue(new Callback<ResponseErrorModel>() {
                    @Override
                    public void onResponse(Call<ResponseErrorModel> call, Response<ResponseErrorModel> response) {
                        if (response.isSuccessful()) {
                            responseErrorModel = response.body();
                            if (responseErrorModel.getErrorMsg().equalsIgnoreCase("Gagal Mengirim")) {
                                Toast.makeText(getActivity(), "Gagal Mendaftar", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getActivity(), "Registrasi Sukses", Toast.LENGTH_SHORT).show();
                                edtNamaLengkap.setText("");
                                edtUsername.setText("");
                                edtPassword.setText("");
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseErrorModel> call, Throwable t) {
                        Toast.makeText(getActivity(), "" + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void initView(View view) {
        edtNamaLengkap = view.findViewById(R.id.edt_nama_lengkap);
        edtUsername = view.findViewById(R.id.edt_username);
        edtPassword = view.findViewById(R.id.edt_password);
        btnRegistrasi = view.findViewById(R.id.btn_registrasi);
    }
}
