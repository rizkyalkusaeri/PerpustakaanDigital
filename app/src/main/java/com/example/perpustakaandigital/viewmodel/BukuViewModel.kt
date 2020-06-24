package com.example.perpustakaandigital.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.perpustakaandigital.model.BukuResponse
import com.example.perpustakaandigital.model.DataBuku
import com.example.perpustakaandigital.repository.MahasiswaImp
import com.example.perpustakaandigital.view.BukuView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subscribers.ResourceSubscriber

class BukuViewModel: ViewModel(),BukuView.ViewModel {

    private val listBuku = MutableLiveData<ArrayList<DataBuku>>()

    private val disposables = CompositeDisposable()

    fun getDataBuku(): LiveData<ArrayList<DataBuku>>{
        return listBuku
    }

    override fun setDataBuku(
        apiKey: String,
        page: Int,
        view: BukuView.View,
        buku: MahasiswaImp
    ) {
        view.showProgressBar()

        disposables.add(
            buku.getDataBuku(apiKey,page)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribeWith(object : ResourceSubscriber<BukuResponse>() {
                    override fun onComplete() {
                        view.hideProgressBar()
                    }

                    override fun onNext(t: BukuResponse?) {
                        t?.let {
                            view.getBukuData(it)
                        }
                        listBuku.postValue(t?.data)
                    }

                    override fun onError(t: Throwable?) {
                        view.hideProgressBar()
                        view.handleError(t)
                    }

                })
        )
    }

    override fun onDestroy() {
        disposables.dispose()
    }
}