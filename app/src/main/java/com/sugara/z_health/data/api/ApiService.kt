package com.sugara.z_health.data.api

import android.net.Uri
import com.sugara.z_health.data.model.Journal
import com.sugara.z_health.data.model.JournalAllModel
import com.sugara.z_health.data.model.JournalModel
import com.sugara.z_health.data.model.JournalWeekModel
import com.sugara.z_health.data.model.LoginModel
import com.sugara.z_health.data.model.ProfileModel
import com.sugara.z_health.data.model.RegisterModel
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path

interface ApiService {

    @FormUrlEncoded
    @POST("register")
    fun register(
        @Field("fullname") fullname: String,
        @Field("email") email: String,
        @Field("birthdate") birthdate: String,
        @Field("profile_img") profile_img: String,
        @Field("password") password: String,
        @Field("confirmPass") confirmPass: String
    ): Call<RegisterModel>

    @FormUrlEncoded
    @POST("login")
    fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<LoginModel>

    @FormUrlEncoded
    @POST("journal")
    fun insertJournal(
        @Field("user_id") user_id: String,
        @Field("waktu_tidur") waktu_tidur: Double,
        @Field("aktivitas_fisik") aktivitas_fisik: Double,
        @Field("aktivitas_sosial") aktivitas_sosial: Double,
        @Field("waktu_belajar") waktu_belajar: Double,
        @Field("waktu_belajar_tambahan") waktu_belajar_tambahan: Double,
        @Field("jurnal_harian") jurnal_harian: String
    ): Call<JournalModel>

    @GET("journal/latest/{user_id}")
    fun getLatestJournal(
        @Path("user_id") user_id: String
    ): Call<JournalModel>

    @Multipart
    @PUT("profile/{user_id}")
    fun updateProfile(
        @Part("fullname") fullname: RequestBody,
        @Part("email") email: RequestBody,
        @Part("birthdate") birthdate: RequestBody,
        @Part profile_img: MultipartBody.Part?,
        @Part("password") password: RequestBody,
        @Part("confirmPass") confirmPass: RequestBody,
        @Header("Authorization") token: String,
        @Path("user_id") user_id: String
    ): Call<LoginModel>

    @GET("journal/latest-week/{user_id}")
    fun getLatestWeekJournal(
        @Path("user_id") user_id: String
    ): Call<JournalWeekModel>

    @GET("journal/all/{user_id}")
    fun getAllJournal(
        @Path("user_id") user_id: String
    ): Call<JournalAllModel>


}