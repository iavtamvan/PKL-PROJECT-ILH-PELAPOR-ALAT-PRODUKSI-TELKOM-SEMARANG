package com.ilh.alpro_telkom.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ilh.alpro_telkom.R;
import com.ilh.alpro_telkom.helper.Config;
import com.ilh.alpro_telkom.model.PelaporModel;
import com.ilh.alpro_telkom.model.ResponseErrorModel;
import com.ilh.alpro_telkom.rest.ApiConfigServer;
import com.ilh.alpro_telkom.rest.ApiService;
import com.ilh.alpro_telkom.ui.teknisi.TeknisiNavActivity;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TeknisiAdapter extends RecyclerView.Adapter<TeknisiAdapter.ViewHolder> {
    private Context context;

    private ArrayList<PelaporModel> pelaporModels;
    private ResponseErrorModel responseErrorModels;

    private String idTeknisi;
    private String idAkun;
    private String idValidator;
    private String status;

//    private ValidatorActivity validatorActivity;

    public TeknisiAdapter(Context context, ArrayList<PelaporModel> pelaporModels) {
        this.context = context;
        this.pelaporModels = pelaporModels;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_teknisi, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        idTeknisi = sharedPreferences.getString(Config.SHARED_PREF_ID, "");
        idAkun = pelaporModels.get(position).getIdUserAkun();
        idValidator = pelaporModels.get(position).getIdUserValidator();
        status = pelaporModels.get(position).getStatus();
        if (status.contains("Sedang Dalam Perbaikan")){
            holder.btnYa.setVisibility(View.GONE);
            holder.btnSelesai.setVisibility(View.VISIBLE);
        } else {
            holder.btnYa.setVisibility(View.VISIBLE);
            holder.btnSelesai.setVisibility(View.GONE);
        }

        Glide.with(context)
                .load(pelaporModels.get(position)
                        .getUrlImage()).error(R.drawable.ic_launcher_background)
                .override(512, 512)
                .into(holder.ivItemValidator);
        holder.tvDeskValidator.setText(pelaporModels.get(position).getDeskripsi());
        holder.tvAlamatValidator.setText(pelaporModels.get(position).getAlamat());

        holder.btnYa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateStatusValidator(pelaporModels.get(position).getIdPelapor(), idTeknisi,"Sedang Dalam Perbaikan");
                ((TeknisiNavActivity)context).setState();
                Toast.makeText(context, "Disetujui", Toast.LENGTH_SHORT).show();
            }
        });

        holder.btnSelesai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateStatusValidator(pelaporModels.get(position).getIdPelapor(), idTeknisi, "Sudah Diselesaikan");
                ((TeknisiNavActivity)context).setState();
                Toast.makeText(context, "Sudah Diselesaikan", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void updateStatusValidator(String idPelapor, String idTeknisi, final String status) {
        ApiService apiService = ApiConfigServer.getApiService();
        apiService.updateStatusTeknisi(idPelapor, idTeknisi, status)
                .enqueue(new Callback<ResponseErrorModel>() {
                    @Override
                    public void onResponse(Call<ResponseErrorModel> call, Response<ResponseErrorModel> response) {
                        if (response.isSuccessful()) {
                            responseErrorModels = response.body();
//                            Toast.makeText(context, "" + responseErrorModels.getErrorMsg(), Toast.LENGTH_SHORT).show();

                            if (status.contains("Sedang Dalam Perbaikan")){
                                getRegID(idValidator, "Mulai pengerjaan oleh teknisi", "Segera di tindaklanjuti oleh Teknisi.");
                            } else if (status.contains("Sudah Diselesaikan")){
                                getRegID(idAkun,"Sudah dilakukan pengerjaan", "Selesai diperbaiki.");
                            }

                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseErrorModel> call, Throwable t) {
                        Toast.makeText(context, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void getRegID(String idAkun, final String tittle, final String message) {
        ApiService apiService = ApiConfigServer.getApiService();
        apiService.getRegID("getTokenRegID", idAkun)
                .enqueue(new Callback<ResponseErrorModel>() {
                    @Override
                    public void onResponse(Call<ResponseErrorModel> call, Response<ResponseErrorModel> response) {
                        if (response.isSuccessful()){
                            responseErrorModels = response.body();
                            Toast.makeText(context, "Mengirim notifikasi", Toast.LENGTH_SHORT).show();
                            Config.pushNotif(context, tittle, message, "individual"
                                    ,responseErrorModels.getRegID());

                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseErrorModel> call, Throwable t) {
                        Toast.makeText(context, "getRegID: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public int getItemCount() {
        return pelaporModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivItemValidator;
        private TextView tvDeskValidator;
        private TextView tvAlamatValidator;
        private Button btnYa;
        private Button btnTidak;
        private Button btnSelesai;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            ivItemValidator = itemView.findViewById(R.id.iv_item_validator);
            tvDeskValidator = itemView.findViewById(R.id.tv_desk_validator);
            tvAlamatValidator = itemView.findViewById(R.id.tv_desk_alamat);
            btnYa = itemView.findViewById(R.id.btn_ya);
            btnTidak = itemView.findViewById(R.id.btn_tidak);
            btnSelesai = itemView.findViewById(R.id.btn_selesai);
        }
    }
}
