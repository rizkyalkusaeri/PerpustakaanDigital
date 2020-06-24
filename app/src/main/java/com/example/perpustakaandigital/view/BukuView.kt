package com.example.perpustakaandigital.view

import com.example.perpustakaandigital.model.BukuResponse
import com.example.perpustakaandigital.repository.MahasiswaImp

class BukuView {

    interface View {
        fun getBukuData(data : BukuResponse)
        fun noInternetConnection(message: String)
        fun showProgressBar()
        fun hideProgressBar()
        fun handleError(t:Throwable?)
    }

    interface ViewModel{
        fun setDataBuku(
            apiKey: String,
            page: Int,
            view: BukuView.View,
            buku: MahasiswaImp
        )

        fun onDestroy()
    }
}