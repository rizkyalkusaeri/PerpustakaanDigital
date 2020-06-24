package com.example.perpustakaandigital.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.perpustakaandigital.model.Data
import com.example.perpustakaandigital.model.HomeResponse
import com.example.perpustakaandigital.repository.MahasiswaImp
import com.example.perpustakaandigital.view.HomeView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subscribers.ResourceSubscriber

class SkripsiViewModel : ViewModel(), HomeView.ViewModel {

    private val listMahasiswa = MutableLiveData<ArrayList<Data>>()

    private val disposables = CompositeDisposable()
    fun getDataMahasiswa(): LiveData<ArrayList<Data>>{
        return listMahasiswa
    }

    override fun setDataMahasiswa(apiKey: String,page:Int, view: HomeView.View, mahasiswa: MahasiswaImp) {
        view.showProgressBar()

        disposables.add(
            mahasiswa.getDataMahasiswa(apiKey,page)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribeWith(object : ResourceSubscriber<HomeResponse>() {
                        override fun onComplete() {
                            view.hideProgressBar()
                        }

                        override fun onNext(t: HomeResponse?) {
                            t?.let {
                                view.getMahasiswaData(it) }
                            listMahasiswa.postValue(t?.data)
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