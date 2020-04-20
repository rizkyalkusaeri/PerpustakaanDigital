package com.example.perpustakaandigital.presenter

import com.example.perpustakaandigital.view.HomeView
import com.example.perpustakaandigital.network.HomeDataSource
import com.example.perpustakaandigital.model.HomeResponse
import com.example.perpustakaandigital.network.NetworkProvider
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class HomePresenter(private val view: HomeView) {

//    fun dataMahasiswa(){
//        view.onShowLoading()
//
//        val dataSource = NetworkProvider.providesHttpAdapter().create(HomeDataSource::class.java)
//        dataSource.dataMahasiswa().enqueue(object : Callback<HomeResponse> {
//            override fun onFailure(call: Call<HomeResponse>?, t: Throwable) {
//                view.onHideLoading()
//                view.onFailure(t)
//            }
//
//            override fun onResponse(call: Call<HomeResponse>?, response: Response<HomeResponse>) {
//                view.onHideLoading()
//                view.onResponse(response.body()?.data ?: emptyList())
//            }
//
//        })
//    }

}