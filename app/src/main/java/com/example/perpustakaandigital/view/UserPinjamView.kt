package com.example.perpustakaandigital.view

import com.example.perpustakaandigital.model.Response

class UserPinjamView {
    interface View{
        fun showProgressBar()
        fun hideProgressBar()
        fun onSuccessPinjam(pinjam: Response)
        fun onFailure(message: String)
        fun noInternetConnection(message: String)
        fun handleError(t:Throwable?)
    }

    interface Presenter{
        fun onPinjamButtonClick(
            apiKey: String,
            id_skripsi: String,
            id_anggota: String,
            tanggal_pinjam: String,
            tanggal_pengembalian: String
        )

        fun onDestroy()
    }
}