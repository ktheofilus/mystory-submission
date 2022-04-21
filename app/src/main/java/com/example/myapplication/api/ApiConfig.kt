package com.example.myapplication.api

import android.util.Log
import com.example.myapplication.BuildConfig.DICODING_ENDPOINT
import com.example.myapplication.api.ApiConfig.baseUrl
import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object ApiConfig {

    var baseUrl :String = DICODING_ENDPOINT

    val loggingInterceptor =
        HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

    val client = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .build()

    val retrofit by lazy { Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .build()}

    fun getApiService(): ApiService = retrofit.create(ApiService::class.java)
}



