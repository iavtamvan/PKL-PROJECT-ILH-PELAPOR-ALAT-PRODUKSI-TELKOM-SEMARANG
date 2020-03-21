package com.ilh.alpro_telkom.rest;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiConfigServer {

    public static ApiService getApiService(){
        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl("http://sig.upgris.ac.id/api_iav/alpro/")
                .baseUrl("http://192.168.43.76/~mac/alpro/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiService service =retrofit.create(ApiService.class);
        return service;
    }
}
