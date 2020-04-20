package com.example.perpustakaandigital.repository

import com.example.perpustakaandigital.model.Data
import com.example.perpustakaandigital.model.DetailHomeResponse
import com.example.perpustakaandigital.model.HomeResponse
import com.example.perpustakaandigital.model.LoginResponse
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