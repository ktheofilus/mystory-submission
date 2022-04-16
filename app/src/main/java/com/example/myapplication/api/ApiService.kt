package com.example.myapplication.api

import android.location.Location
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    @POST("register")
    fun register(
        @Body register: Register
        ):Call<ActionResponse>


    @POST("login")
    fun login(
        @Body login: Login
    ):Call<LoginResponse>

    @GET("stories")
    suspend fun getStories(
        @Header("Authorization") token: String,
        @Query("page") page :Int,
        @Query("size") size :Int
    ): ListStoryResponse

    @GET("stories")
    fun getStoriesLocation(
        @Header("Authorization") token: String,
        @Query("location") location: Int
    ): Call<ListStoryResponseLocation>

    @Multipart
    @POST("stories")
    fun uploadImage(
        @Header("Authorization") token: String?,
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody,
        @Part("lat") lat: RequestBody?,
        @Part("lon") lon: RequestBody?,
    ): Call<ActionResponse>


}

data class Register(val name:String,val email:String,val password: String)

data class Login(val email:String,val password: String)


