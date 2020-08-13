package com.example.perpustakaandigital.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.perpustakaandigital.model.Data
import com.example.perpustakaandigital.model.Peminjaman
import com.example.perpustakaandigital.model.PeminjamanResponse
import com.example.perpustakaandigital.repository.PerpusImp
import com.example.perpustakaandigital.view.PeminjamanView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subscribers.ResourceSubscriber

class PeminjamanViewModel : ViewModel(), PeminjamanView.ViewModel {

    private val listPeminjaman = MutableLiveData<ArrayList<Peminjaman>>()

    private val disposables = CompositeDisposable()
    fun getDataPeminjaman(): LiveData<ArrayList<Peminjaman>> {
        return listPeminjaman
    }

    override fun setDataPeminjaman(
        apiKey: String,
        idAnggota: String,
        stat: String,
        page: Int,
        view: PeminjamanView.View,
        perpus: PerpusImp
    ) {
        view.showProgressBar()

        disposables.add(
            perpus.getDataPeminjaman(apiKey,idAnggota,stat,page)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribeWith(object : ResourceSubscriber<PeminjamanResponse>(){
                    override fun onComplete() {
                        view.hideProgressBar()
                    }

                    override fun onNext(t: PeminjamanResponse?) {

                        if (!t?.status!!) {
                            t.message?.let { view.onFailure(it) }
                        } else{
                            t.let {
                                view.getPeminjamanData(it) }
                            listPeminjaman.postValue(t.data)
                            t.message?.let { view.onFailure(it) }
                        }

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