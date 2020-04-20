package com.example.perpustakaandigital.view

import com.example.perpustakaandigital.model.LoginResponse
import com.example.perpustakaandigital.repository.MahasiswaImplementation

class LoginView {

    interface View{
        fun showProgressBar()
        fun hideProgressBar()
        fun onSuccess(data: LoginResponse)
        fun onFailure(message: String)
        fun noInternetConnection(message: String)
        fun handleError(t:Throwable?)
    }

    interface Presenter{
        fun onLoginButtonClick(
            apiKey: String,
            email: String,
            password: String
        )

        fun onDestroy()
    }

    interface ViewModel{
        fun onLoginButtonClick(
            apiKey: String,
            email: String,
            password: String,
            view: View,
            mahasiswa: MahasiswaImplementation
        )

        fun onDestroy()
    }
}