package com.example.perpustakaandigital.view

import com.example.perpustakaandigital.model.HomeResponse
import com.example.perpustakaandigital.repository.MahasiswaImp

class HomeView {

    interface View{
        fun getMahasiswaData(data: HomeResponse)
        fun noInternetConnection(message: String)
        fun showProgressBar()
        fun hideProgressBar()
        fun handleError(t:Throwable?)
    }

    interface ViewModel{
        fun setDataMahasiswa(
            apiKey: String,
            page: Int,
            view: View,
            mahasiswa: MahasiswaImp
        )

        fun onDestroy()
    }


}