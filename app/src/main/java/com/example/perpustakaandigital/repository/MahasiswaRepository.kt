package com.example.perpustakaandigital.repository

import com.example.perpustakaandigital.model.DetailHomeResponse
import com.example.perpustakaandigital.model.HomeResponse
import com.example.perpustakaandigital.model.LoginResponse
import io.reactivex.Flowable

interface MahasiswaRepository {
    fun getDataMahasiswa(apiKey: String, page:Int): Flowable<HomeResponse>

    fun getDataLogin(apiKey: String, email: String, password:String): Flowable<LoginResponse>
//    fun getDetailDataMahasiswa(apiKey: String, skripsiId: String): Flowable<DetailHomeResponse>
}