package com.example.perpustakaandigital.network

import com.example.perpustakaandigital.BuildConfig
import com.example.perpustakaandigital.model.DetailHomeResponse
import com.example.perpustakaandigital.model.HomeResponse
import com.example.perpustakaandigital.model.LoginResponse
import com.example.perpustakaandigital.model.UsersResponse
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
        @Field("X-API-KEY")
        apiKey : String,
        @Field("email") email : String,
        @Field("password") password : String
    ): Flowable<LoginResponse>
}