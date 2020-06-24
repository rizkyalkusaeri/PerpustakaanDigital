package com.example.perpustakaandigital.presenter

import com.example.perpustakaandigital.view.HomeView


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