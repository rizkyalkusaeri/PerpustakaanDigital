package com.example.perpustakaandigital.view

import com.example.perpustakaandigital.model.PeminjamanResponse
import com.example.perpustakaandigital.repository.PerpusImp

class PeminjamanView {

    interface View{
        fun getPeminjamanData(peminjaman: PeminjamanResponse)
        fun noInternetConnection(message: String)
        fun onFailure(message: String)
        fun showProgressBar()
        fun hideProgressBar()
        fun handleError(t:Throwable?)
    }

    interface ViewModel{
        fun setDataPeminjaman(
            apiKey: String,
            idAnggota: String,
            stat: String,
            page: Int,
            view: View,
            perpus: PerpusImp
        )

        fun onDestroy()
    }
}