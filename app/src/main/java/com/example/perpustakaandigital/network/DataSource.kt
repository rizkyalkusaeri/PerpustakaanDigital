package com.example.perpustakaandigital.network

import com.example.perpustakaandigital.BuildConfig
import com.example.perpustakaandigital.model.*
import io.reactivex.Flowable
import retrofit2.Call
import retrofit2.http.*

interface DataSource {

    @GET("/api/skripsi")
    fun dataMahasiswa(
        @Query("X-API-KEY")
        apiKey : String,
        @Query("page") page: Int
    ) : Flowable<HomeResponse>

    @GET("/api/buku")
    fun dataBuku(
        @Query("X-API-KEY")
        apiKey : String,
        @Query("page") page: Int
    ) : Flowable<BukuResponse>

    @FormUrlEncoded
    @POST("/api/login")
    fun userLogin(
        @Field("X-API-KEY")
        apiKey : String,
        @Field("email") email : String,
        @Field("password") password : String
    ): Flowable<LoginResponse>

    @FormUrlEncoded
    @POST("/api/anggota")
    fun ubahPassword(
        @Field("X-API-KEY")
        apiKey : String,
        @Field("id_anggota") id_anggota : String,
        @Field("password") password : String
    ): Flowable<UbahPasswordResponse>

    @FormUrlEncoded
    @POST("/api/peminjaman")
    fun userPinjam(
        @Field("X-API-KEY")
        apiKey : String,
        @Field("id_skripsi") id_skripsi : String,
        @Field("id_anggota") id_anggota : String,
        @Field("tanggal_pinjam") tanggal_pinjam : String,
        @Field("tanggal_pengembalian") tanggal_pengembalian : String
    ): Flowable<PinjamResponse>
}