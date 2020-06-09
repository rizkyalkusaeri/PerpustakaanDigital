package com.example.perpustakaandigital.repository

import com.example.perpustakaandigital.model.*
import com.example.perpustakaandigital.network.HomeDataSource
import io.reactivex.Flowable

class MahasiswaImplementation(private val dataSource: HomeDataSource): MahasiswaRepository {

    override fun getDataMahasiswa(apiKey: String, page: Int): Flowable<HomeResponse> =
            dataSource.dataMahasiswa(apiKey,page)

    override fun getDataLogin(
        apiKey: String,
        email: String,
        password: String
    ): Flowable<LoginResponse> = dataSource.userLogin(apiKey, email, password)


}