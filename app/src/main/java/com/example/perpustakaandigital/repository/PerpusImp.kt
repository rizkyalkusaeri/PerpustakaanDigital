package com.example.perpustakaandigital.repository

import com.example.perpustakaandigital.model.*
import com.example.perpustakaandigital.network.DataSource
import io.reactivex.Flowable

class PerpusImp(private val dataSource: DataSource): PerpusRepository {

    override fun getDataSkripsi(apiKey: String, page: Int): Flowable<SkripsiResponse> =
        dataSource.dataSkripsi(apiKey,page)

//    override fun getDataBuku(apiKey: String, page: Int): Flowable<BukuResponse> =
//        dataSource.dataBuku(apiKey, page)

    override fun getDataPeminjaman(
        apiKey: String,
        idAnggota: String,
        stat: String,
        page: Int
    ): Flowable<PeminjamanResponse> =
        dataSource.dataPeminjaman(apiKey, idAnggota, stat,page)


    override fun getDataLogin(
        apiKey: String,
        email: String,
        password: String
    ): Flowable<LoginResponse> = dataSource.userLogin(apiKey, email, password)

    override fun getDataUbahPassword(
        apiKey: String,
        id_anggota: String,
        password: String
    ): Flowable<Response> = dataSource.ubahPassword(apiKey,id_anggota,password)

    override fun postPinjam(
        apiKey: String,
        id_skripsi: String,
        id_anggota: String,
        tanggal_pinjam: String,
        tanggal_pengembalian: String
    ): Flowable<Response> = dataSource.userPinjam(apiKey,id_skripsi,id_anggota,tanggal_pinjam,tanggal_pengembalian)

    override fun postReturn(
        apiKey: String,
        id_peminjaman: Int
    ): Flowable<Response> = dataSource.userReturn(apiKey,id_peminjaman)


}