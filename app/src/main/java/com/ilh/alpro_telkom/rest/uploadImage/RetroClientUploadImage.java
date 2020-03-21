package com.ilh.alpro_telkom.rest.uploadImage;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class RetroClientUploadImage {



    private static final String ROOT_URL = "http://sandec.org/iav/image/";

    public RetroClientUploadImage(){
    }

    private static Retrofit getRetrofitClient(){
        return new Retrofit.Builder()
                .baseUrl(ROOT_URL)
                .addConverterFactory(GsonConverterFactory.create()).build();

    }

    public static APIServiceUploadImage getService(){
        return getRetrofitClient().create(APIServiceUploadImage.class);
    }
}
