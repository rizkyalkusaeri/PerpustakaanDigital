package com.example.perpustakaandigital.network

import com.example.perpustakaandigital.BuildConfig
import com.example.perpustakaandigital.model.DetailHomeResponse
import com.example.perpustakaandigital.model.HomeResponse
import com.example.perpustakaandigital.model.LoginResponse
import io.reactivex.Flowable
import retrofit2.Call
import retrofit2.http.*

interface HomeDataSource {

    @GET("/api/mahasiswa")
    fun dataMahasiswa(
            @Query("X-API-KEY")
            apiKey : String,
            @Query("page") page: Int
    ) : Flowable<HomeResponse>

    @FormUrlEncoded
    @POST("/api/login")
    fun userLogin(
        @Query("X-API-KEY")
        apiKey : String,
        @Field("email") email : String,
        @Field("password") password : String
    ): Flowable<LoginResponse>
}