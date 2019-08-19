package com.ilh.alpro_telkom.ui.pelapor;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.ilh.alpro_telkom.R;
import com.ilh.alpro_telkom.helper.Config;
import com.ilh.alpro_telkom.model.ResponseErrorModel;
import com.ilh.alpro_telkom.rest.ApiConfigServer;
import com.ilh.alpro_telkom.rest.ApiService;
import com.ilh.alpro_telkom.rest.uploadImage.APIServiceUploadImage;
import com.ilh.alpro_telkom.rest.uploadImage.Result;
import com.ilh.alpro_telkom.rest.uploadImage.RetroClientUploadImage;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import in.mayanknagwanshi.imagepicker.ImageSelectActivity;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class PelaporUploadFragment extends Fragment {


    private ImageView ivImagePealpor;
    private TextView tvKeluar;
    private TextView tvUsername;
    private EditText edtDeskripsi;
    private EditText edtLokasi;
    private Button btnKirimPelapor;

    private String imagePath;
    private String getNameImage;
    private String idAkun;

    private ProgressDialog p;
    private static final String TAG = "ILH";

    public PelaporUploadFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_pelapor_upload, container, false);
        initView(view);
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        idAkun = sharedPreferences.getString(Config.SHARED_PREF_ID, "");
        tvUsername.setText(sharedPreferences.getString(Config.SHARED_PREF_USERNAME, ""));
        ivImagePealpor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ImageSelectActivity.class);
                intent.putExtra(ImageSelectActivity.FLAG_COMPRESS, false);//default is true
                intent.putExtra(ImageSelectActivity.FLAG_CAMERA, true);//default is true
                intent.putExtra(ImageSelectActivity.FLAG_GALLERY, true);//default is true
                startActivityForResult(intent, 1213);
            }
        });

        btnKirimPelapor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edtDeskripsi.getText().toString().isEmpty()) {
                    edtDeskripsi.setError("Isi deskripsi");
                } else {
                    p = new ProgressDialog(getActivity());
                    p.setMessage("Upload foto");
                    p.show();
                    uploadImage();
                }

            }
        });

        tvKeluar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finishAffinity();
                Config.logout(getActivity());
            }
        });

        return view;
    }

    private void uploadImage() {
        APIServiceUploadImage s = RetroClientUploadImage.getService();

        File f = new File(imagePath);
        if (f == null) {
            Log.d(TAG, "uploadImage: erroor files");
        }
        getNameImage = f.getName();
        Log.d("", "uploadImage: " + f.getName());

        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), f);

        MultipartBody.Part part = MultipartBody.Part.createFormData("uploaded_file", f.getName(), requestFile);
        Call<Result> resultCAll = s.postIMmage(part);
        resultCAll.enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {
                if (response.isSuccessful()) {
                    if (response.body().getResult().equals("success")) {
                        Toast.makeText(getActivity(), "uploaded successfully", Toast.LENGTH_SHORT).show();
                        sendData();

                    } else {

                    }
                } else {
                    Toast.makeText(getActivity(), "Upload Image Gagal", Toast.LENGTH_SHORT).show();
                }

                imagePath = "";
//                te.setVisibility(View.VISIBLE);
//                imageVi.setVisibility(View.INVISIBLE);

            }

            @Override
            public void onFailure(Call<Result> call, Throwable t) {
                Toast.makeText(getActivity(), "" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void sendData() {
        ApiService apiService = ApiConfigServer.getApiService();
        apiService.postDataPelapor("http://devlop.can.web.id/uploads/client_profile_images/3/" + getNameImage,
                edtDeskripsi.getText().toString().trim(),
                edtLokasi.getText().toString().trim(), idAkun)
                .enqueue(new Callback<ResponseErrorModel>() {
                    @Override
                    public void onResponse(Call<ResponseErrorModel> call, Response<ResponseErrorModel> response) {
                        if (response.isSuccessful()) {
                            p.dismiss();
                            Toast.makeText(getActivity(), "Sukses", Toast.LENGTH_SHORT).show();
//                            edtDeskripsi.setText("http://devlop.can.web.id/uploads/client_profile_images/3/" + getNameImage);
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseErrorModel> call, Throwable t) {
                        Toast.makeText(getActivity(), "" + t.getMessage(), Toast.LENGTH_SHORT).show();
                        p.dismiss();
                    }
                });

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1213 && resultCode == Activity.RESULT_OK) {
            String filePath = data.getStringExtra(ImageSelectActivity.RESULT_FILE_PATH);
            Bitmap selectedImage = BitmapFactory.decodeFile(filePath);

            String root = Environment.getExternalStorageDirectory().toString();
            File myDir = new File(root + "/ALPRO-TELKOM");
            myDir.mkdirs();

            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String fname = "alpro_" + timeStamp + ".jpg";

            File file = new File(myDir, fname);
            imagePath = String.valueOf(file);
            if (file.exists()) file.delete();
            try {
                FileOutputStream out = new FileOutputStream(file);
                selectedImage.compress(Bitmap.CompressFormat.JPEG, 100, out);
                out.flush();
                out.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
//            edtNamaKeluhan.setText(String.valueOf("" + file));
            ivImagePealpor.setImageBitmap(selectedImage);
        }
    }

    private void initView(View view) {
        ivImagePealpor = view.findViewById(R.id.iv_image_pelapor);
        edtDeskripsi = view.findViewById(R.id.edt_deskripsi);
        edtLokasi = view.findViewById(R.id.edt_Lokasi);
        btnKirimPelapor = view.findViewById(R.id.btn_kirim_pelapor);
        tvKeluar = view.findViewById(R.id.tv_keluar);
        tvUsername = view.findViewById(R.id.tv_username);
    }
}
