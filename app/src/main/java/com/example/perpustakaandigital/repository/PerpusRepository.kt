package com.example.perpustakaandigital.repository

import com.example.perpustakaandigital.model.*
import io.reactivex.Flowable

interface PerpusRepository {
    fun getDataSkripsi(apiKey: String, page:Int): Flowable<SkripsiResponse>

//    fun getDataBuku(apiKey: String, page:Int): Flowable<BukuResponse>

    fun getDataPeminjaman(apiKey: String,idAnggota: String,stat: String ,page:Int): Flowable<PeminjamanResponse>

    fun getDataLogin(apiKey: String, email: String, password:String): Flowable<LoginResponse>

    fun getDataUbahPassword(apiKey: String, id_anggota: String, password: String): Flowable<Response>

    fun postPinjam(apiKey: String, id_skripsi: String, id_anggota: String, tanggal_pinjam: String, tanggal_pengembalian: String): Flowable<Response>

    fun postReturn(apiKey: String, id_peminjaman: Int): Flowable<Response>


}