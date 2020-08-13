package com.example.perpustakaandigital.view

import com.example.perpustakaandigital.model.Response

class UserReturnView {

    interface View{
        fun showProgressBarReturn()
        fun hideProgressBarReturn()
        fun onSuccessReturn(returnResponse: Response)
        fun onFailureReturn(message: String)
        fun noInternetConnectionReturn(message: String)
        fun handleErrorReturn(t:Throwable?)
    }

    interface Presenter{
        fun onReturnButtonClick(
            apiKey: String,
            id_peminjaman: Int
        )

        fun onDestroy()
    }
}