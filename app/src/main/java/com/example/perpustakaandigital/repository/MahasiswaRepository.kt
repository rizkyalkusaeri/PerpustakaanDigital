package com.example.perpustakaandigital.repository

import com.example.perpustakaandigital.model.*
import io.reactivex.Flowable

interface MahasiswaRepository {
    fun getDataMahasiswa(apiKey: String, page:Int): Flowable<HomeResponse>

    fun getDataBuku(apiKey: String, page:Int): Flowable<BukuResponse>

    fun getDataLogin(apiKey: String, email: String, password:String): Flowable<LoginResponse>

    fun getDataUbahPassword(apiKey: String, id_anggota: String, password: String): Flowable<UbahPasswordResponse>

    fun postPinjam(apiKey: String, id_skripsi: String, id_anggota: String, tanggal_pinjam: String, tanggal_pengembalian: String): Flowable<PinjamResponse>


}