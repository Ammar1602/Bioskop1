package com.example.bioskop1.api

import android.provider.ContactsContract.CommonDataKinds.Email
import com.example.bioskop1.model.UserModel
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiEndPoint {

    @FormUrlEncoded
    @POST("login")
    fun login(@Field("email") email: String, @Field("password") password: String): Call<UserModel>

    @FormUrlEncoded
    @GET("refresh")
    fun refresh(): Call<UserModel>
}