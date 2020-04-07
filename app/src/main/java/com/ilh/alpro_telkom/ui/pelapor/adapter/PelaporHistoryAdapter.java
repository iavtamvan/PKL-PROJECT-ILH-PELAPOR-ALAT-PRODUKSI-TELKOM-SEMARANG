package com.ilh.alpro_telkom.ui.pelapor.adapter;

import android.content.Context;
import android.content.Intent;
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
import com.ilh.alpro_telkom.ui.pelapor.activity.FeedbackActivity;

import java.util.ArrayList;

public class PelaporHistoryAdapter extends RecyclerView.Adapter<PelaporHistoryAdapter.ViewHolder> {
    private Context context;
    private ArrayList<PelaporModel> pelaporModels;
    private ResponseErrorModel responseErrorModels;
    private String idPelapor;

//    private ValidatorActivity validatorActivity;

    public PelaporHistoryAdapter(Context context, ArrayList<PelaporModel> pelaporModels) {
        this.context = context;
        this.pelaporModels = pelaporModels;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_pelapor_history, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
//        validatorActivity = new ValidatorActivity();
        Glide.with(context)
                .load(pelaporModels.get(position)
                        .getUrlImage()).error(R.drawable.ic_launcher_background)
                .override(512, 512)
                .into(holder.ivItemValidator);
        holder.tvDeskValidator.setText(pelaporModels.get(position).getDeskripsi());
        holder.tvAlamatValidator.setText(pelaporModels.get(position).getAlamat());
        holder.tvAlamatStatus.setText(pelaporModels.get(position).getStatus());
        final SharedPreferences sharedPreferences = context.getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);


        if (pelaporModels.get(position).getStatus_feedback().equalsIgnoreCase("Pending")){
            holder.btn_feedback.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (pelaporModels.get(position).getIdUserTeknisi() == null || holder.tvAlamatStatus.getText().toString().equalsIgnoreCase("Ditolak Validator") ||
                            holder.tvAlamatStatus.getText().toString().equalsIgnoreCase("Belum Disetujui") || holder.tvAlamatStatus.getText().toString().equalsIgnoreCase("Sedang Dalam Perbaikan")
                    || holder.tvAlamatStatus.getText().toString().equalsIgnoreCase("Disetujui Validator")){
//                        holder.btn_feedback.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View view) {
                                Toast.makeText(context, "Pelaporan Belum Selesai, coba lagi nanti", Toast.LENGTH_SHORT).show();
//                            }
//                        });
                    } else {
                        // Nanti disini ke halaman feedback

                        Intent intent = new Intent(context, FeedbackActivity.class);
                        intent.putExtra("ID_USER", sharedPreferences.getString(Config.SHARED_PREF_ID, ""));
                        intent.putExtra("ID_TEKNISI", pelaporModels.get(position).getIdUserTeknisi());
                        intent.putExtra("ID_PELAPOR", pelaporModels.get(position).getIdPelapor());
                        intent.putExtra("ALAMAT", holder.tvAlamatValidator.getText().toString());
                        context.startActivity(intent);
                    }
                }
            });
        } else {
            holder.btn_feedback.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return pelaporModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivItemValidator;
        private TextView tvDeskValidator;
        private TextView tvAlamatValidator;
        private TextView tvAlamatStatus;
        private Button btnYa;
        private Button btn_feedback;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            ivItemValidator = itemView.findViewById(R.id.iv_item_pelapor);
            tvDeskValidator = itemView.findViewById(R.id.tv_desk_validator);
            tvAlamatValidator = itemView.findViewById(R.id.tv_desk_alamat);
            tvAlamatStatus = itemView.findViewById(R.id.tv_desk_status);
            btnYa = itemView.findViewById(R.id.btn_ya);
            btn_feedback = itemView.findViewById(R.id.btn_feedback);
        }
    }
}
