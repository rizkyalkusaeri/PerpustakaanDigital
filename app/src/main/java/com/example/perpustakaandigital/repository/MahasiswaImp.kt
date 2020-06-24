package com.example.perpustakaandigital.repository

import com.example.perpustakaandigital.model.*
import com.example.perpustakaandigital.network.DataSource
import io.reactivex.Flowable

class MahasiswaImp(private val dataSource: DataSource): MahasiswaRepository {

    override fun getDataMahasiswa(apiKey: String, page: Int): Flowable<HomeResponse> =
        dataSource.dataMahasiswa(apiKey,page)

    override fun getDataBuku(apiKey: String, page: Int): Flowable<BukuResponse> =
        dataSource.dataBuku(apiKey, page)


    override fun getDataLogin(
        apiKey: String,
        email: String,
        password: String
    ): Flowable<LoginResponse> = dataSource.userLogin(apiKey, email, password)

    override fun getDataUbahPassword(
        apiKey: String,
        id_anggota: String,
        password: String
    ): Flowable<UbahPasswordResponse> = dataSource.ubahPassword(apiKey,id_anggota,password)

    override fun postPinjam(
        apiKey: String,
        id_skripsi: String,
        id_anggota: String,
        tanggal_pinjam: String,
        tanggal_pengembalian: String
    ): Flowable<PinjamResponse> = dataSource.userPinjam(apiKey,id_skripsi,id_anggota,tanggal_pinjam,tanggal_pengembalian)


}