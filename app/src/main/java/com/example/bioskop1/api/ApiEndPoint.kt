package com.example.bioskop1.api

import android.provider.ContactsContract.CommonDataKinds.Email
import com.example.bioskop1.model.MovieModel
import com.example.bioskop1.model.OrderModel
import com.example.bioskop1.model.ResponseModel
import com.example.bioskop1.model.Schedulee
import com.example.bioskop1.model.Seat
import com.example.bioskop1.model.SeatModel
import com.example.bioskop1.model.UserModel
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query
import java.util.Date

interface ApiEndPoint {

    @FormUrlEncoded
    @POST("login")
    fun login(@Field("email") email: String, @Field("password") password: String): Call<UserModel>

    @FormUrlEncoded
    @GET("refresh")
    fun refresh(): Call<UserModel>

    @GET("movie")
    fun getMovie(): Call<MovieModel>

    @GET("movie/{id}")
    fun showMovie(@Path("id") id: Int): Call<MovieModel>

    @GET("seat")
    fun getSeat(@Query("movie_schedule_id") id: Int, @Query("screening_date") date: String): Call<SeatModel>

    @POST("order")
    fun createOrder(): Call<OrderModel>

    @FormUrlEncoded
    @POST("order/detail")
    fun createdOrderDetail(@Field("order_id") order: Int, @Field
        ("movie_schedule_id") schedulee: Int, @Field("seat_id") seat: Int, @Field
        ("movie_screening") date: String): Call<ResponseModel>
}