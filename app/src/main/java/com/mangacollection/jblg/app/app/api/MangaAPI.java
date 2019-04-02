package com.mangacollection.jblg.app.app.api;


import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class MangaAPI {
   private static Retrofit retrofit=null;

   public static Retrofit getRetrofit(){
       HttpLoggingInterceptor loggingInterceptor=new HttpLoggingInterceptor();
       loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
       OkHttpClient client=new OkHttpClient.Builder().connectTimeout(10, TimeUnit.SECONDS)
               .readTimeout(10, TimeUnit.SECONDS).addInterceptor(loggingInterceptor).build();

       retrofit=new Retrofit.Builder()
               .baseUrl("https://api.jikan.moe/v3/")
               .addConverterFactory(GsonConverterFactory.create())
               .client(client)
               .build();

       return retrofit;
   }


}
