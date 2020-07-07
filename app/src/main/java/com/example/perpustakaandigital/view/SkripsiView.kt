package com.example.perpustakaandigital.view

import com.example.perpustakaandigital.model.SkripsiResponse
import com.example.perpustakaandigital.repository.PerpusImp

class SkripsiView {


    interface View{
        fun getSkripsiData(data: SkripsiResponse)
        fun noInternetConnection(message: String)
        fun showProgressBar()
        fun hideProgressBar()
        fun handleError(t:Throwable?)
    }

    interface ViewModel{
        fun setDataSkripsi(
            apiKey: String,
            page: Int,
            view: View,
            perpus: PerpusImp
        )

        fun onDestroy()
    }


}