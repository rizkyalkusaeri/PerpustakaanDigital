package com.example.perpustakaandigital.network

import com.example.perpustakaandigital.model.*
import io.reactivex.Flowable
import retrofit2.http.*

interface DataSource {

    @GET("/api/skripsi")
    fun dataSkripsi(
        @Query("X-API-KEY")
        apiKey : String,
        @Query("page") page: Int
    ) : Flowable<SkripsiResponse>

//    @GET("/api/buku")
//    fun dataBuku(
//        @Query("X-API-KEY")
//        apiKey : String,
//        @Query("page") page: Int
//    ) : Flowable<BukuResponse>

    @GET("/api/peminjaman")
    fun dataPeminjaman(
        @Query("X-API-KEY")
        apiKey : String,
        @Query("id_anggota") idAnggota: String,
        @Query("stat") stat : String,
        @Query("page") page: Int
    ) : Flowable<PeminjamanResponse>

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
    ): Flowable<Response>

    @FormUrlEncoded
    @POST("/api/peminjaman")
    fun userPinjam(
        @Field("X-API-KEY")
        apiKey : String,
        @Field("id_skripsi") id_skripsi : String,
        @Field("id_anggota") id_anggota : String,
        @Field("tanggal_pinjam") tanggal_pinjam : String,
        @Field("tanggal_pengembalian") tanggal_pengembalian : String
    ): Flowable<Response>

    @FormUrlEncoded
    @POST("/api/pengembalian")
    fun userReturn(
        @Field("X-API-KEY")
        apiKey : String,
        @Field("id_peminjaman") id_peminjaman : Int
    ): Flowable<Response>
}