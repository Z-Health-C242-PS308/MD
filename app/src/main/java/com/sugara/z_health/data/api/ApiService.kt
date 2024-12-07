package com.sugara.z_health.data.api

import com.sugara.z_health.data.model.RegisterModel
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

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
}