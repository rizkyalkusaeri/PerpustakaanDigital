package com.example.perpustakaandigital.view

import com.example.perpustakaandigital.model.LoginResponse
import com.example.perpustakaandigital.model.UbahPasswordResponse

class UbahPasswordView {

    interface View{
        fun showProgressBar()
        fun hideProgressBar()
        fun onSuccess(ubah: UbahPasswordResponse)
        fun onFailure(message: String)
        fun noInternetConnection(message: String)
        fun handleError(t:Throwable?)
    }

    interface Presenter{
        fun onUbahButtonClick(
            apiKey: String,
            id_anggota: String,
            password: String
        )

        fun onDestroy()
    }

}