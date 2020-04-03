package com.ilh.alpro_telkom.ui.validator.adapter;

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
import com.ilh.alpro_telkom.ui.validator.activity.ValidatorNavActivity;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ValidatorAdapter extends RecyclerView.Adapter<ValidatorAdapter.ViewHolder> {
    private Context context;
    private ArrayList<PelaporModel> pelaporModels;
    private ResponseErrorModel responseErrorModels;
    private String idValidator;
    private String idAkun;
    private String statusNotif;
    private String messageNotif;

    public ValidatorAdapter(Context context, ArrayList<PelaporModel> pelaporModels) {
        this.context = context;
        this.pelaporModels = pelaporModels;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_validator, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        idValidator = sharedPreferences.getString(Config.SHARED_PREF_ID, "");
        idAkun = pelaporModels.get(position).getIdUserAkun();
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
                updateStatusValidatorKeTeknisi(pelaporModels.get(position).getIdPelapor(),idValidator,"Disetujui Validator");
                statusNotif = "Disetujui ";
                messageNotif = "Disetujui Milik Alat Produksi PT. TELKOM INDONESIA.";
//                Toast.makeText(context, "Disetujui", Toast.LENGTH_SHORT).show();
//                Toast.makeText(context, "id pelapor: " + pelaporModels.get(position).getIdPelapor() + "id validator: " + idValidator, Toast.LENGTH_SHORT).show();
            }
        });

        holder.btnTidak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateStatusValidatorKePelapor(pelaporModels.get(position).getIdPelapor(),idValidator, "Ditolak Validator");
                statusNotif = "Ditolak ";
                messageNotif = "Ditolak Bukan Milik Alat Produksi PT. TELKOM INDONESIA.";
            }
        });

    }

    private void updateStatusValidatorKeTeknisi(String idPelapor, String idValidator, String status) {
        ApiService apiService = ApiConfigServer.getApiService();
        apiService.updateStatusValidator(idPelapor, idValidator, status)
                .enqueue(new Callback<ResponseErrorModel>() {
                    @Override
                    public void onResponse(Call<ResponseErrorModel> call, Response<ResponseErrorModel> response) {
                        if (response.isSuccessful()){
                            responseErrorModels = response.body();
//                            Toast.makeText(context, "" + responseErrorModels.getErrorMsg(), Toast.LENGTH_SHORT).show();
                            getRegID(idAkun);
                            ((ValidatorNavActivity)context).setState();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseErrorModel> call, Throwable t) {
                        Toast.makeText(context, "updateValidator: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
    private void updateStatusValidatorKePelapor(String idPelapor, String idValidator, String status) {
        ApiService apiService = ApiConfigServer.getApiService();
        apiService.updateStatusValidator(idPelapor, idValidator, status)
                .enqueue(new Callback<ResponseErrorModel>() {
                    @Override
                    public void onResponse(Call<ResponseErrorModel> call, Response<ResponseErrorModel> response) {
                        if (response.isSuccessful()){
                            responseErrorModels = response.body();
//                            Toast.makeText(context, "" + responseErrorModels.getErrorMsg(), Toast.LENGTH_SHORT).show();
                            getRegID(idAkun);
                        }
                    }
                    @Override
                    public void onFailure(Call<ResponseErrorModel> call, Throwable t) {
                        Toast.makeText(context, "updateValidator: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void getRegID(String idAkun) {
        ApiService apiService = ApiConfigServer.getApiService();
        apiService.getRegID("getTokenRegID", idAkun)
                .enqueue(new Callback<ResponseErrorModel>() {
                    @Override
                    public void onResponse(Call<ResponseErrorModel> call, Response<ResponseErrorModel> response) {
                        if (response.isSuccessful()){
                            responseErrorModels = response.body();
                            Config.pushNotif(context,  statusNotif+"oleh validator",
                                    messageNotif,
                                    "individual",responseErrorModels.getRegID());
                            ((ValidatorNavActivity)context).setState();
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

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            ivItemValidator = itemView.findViewById(R.id.iv_item_validator);
            tvDeskValidator = itemView.findViewById(R.id.tv_desk_validator);
            tvAlamatValidator = itemView.findViewById(R.id.tv_desk_alamat);
            btnYa = itemView.findViewById(R.id.btn_ya);
            btnTidak = itemView.findViewById(R.id.btn_tidak);
        }
    }
}
