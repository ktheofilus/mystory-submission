package com.example.myapplication.api

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    @POST("/v1/register")
    fun register(
        @Body register: Register
        ):Call<ActionResponse>


    @POST("/v1/login")
    fun login(
        @Body login: Login
    ):Call<LoginResponse>

    @GET("/v1/stories")
    fun getStories(
        @Header("Authorization") token: String?,
        @Query("page") page: Int?,
        @Query("size") size: Int?
    ):ListStoryResponse

    @Multipart
    @POST("/v1/stories")
    fun uploadImage(
        @Header("Authorization") token: String?,
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody,
    ): Call<ActionResponse>


}

data class Register(val name:String,val email:String,val password: String)

data class Login(val email:String,val password: String)


