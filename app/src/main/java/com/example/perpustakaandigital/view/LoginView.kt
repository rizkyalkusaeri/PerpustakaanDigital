package com.example.perpustakaandigital.view

import com.example.perpustakaandigital.model.LoginResponse

class LoginView {

    interface View{
        fun showProgressBar()
        fun hideProgressBar()
        fun onSuccess(login: LoginResponse)
        fun onFailure(message: String)
        fun noInternetConnection(message: String)
        fun handleError(t:Throwable?)
    }

    interface Presenter{
        fun onLoginButtonClick(
            apiKey: String,
            nim: String,
            password: String
        )

        fun onDestroy()
    }

}