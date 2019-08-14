package com.ilh.alpro_telkom.rest;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiConfigServer {

    public static ApiService getApiService(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.81.11.13/~mac/pelapor-tiang-telkom/")
//                .baseUrl("http://10.81.11.13/~mac/pelapor-tiang-telkom/")
//                .baseUrl("http://sig.upgris.ac.id/api_iav/sertifikasi_android/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiService service =retrofit.create(ApiService.class);
        return service;
    }
}
