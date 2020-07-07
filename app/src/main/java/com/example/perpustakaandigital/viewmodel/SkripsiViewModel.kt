package com.example.perpustakaandigital.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.perpustakaandigital.model.Data
import com.example.perpustakaandigital.model.SkripsiResponse
import com.example.perpustakaandigital.repository.PerpusImp
import com.example.perpustakaandigital.view.SkripsiView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subscribers.ResourceSubscriber

class SkripsiViewModel : ViewModel(), SkripsiView.ViewModel {

    private val listSkripsi = MutableLiveData<ArrayList<Data>>()

    private val disposables = CompositeDisposable()
    fun getDataSkripsi(): LiveData<ArrayList<Data>>{
        return listSkripsi
    }

    override fun setDataSkripsi(apiKey: String, page:Int, view: SkripsiView.View, perpus: PerpusImp) {
        view.showProgressBar()

        disposables.add(
            perpus.getDataSkripsi(apiKey,page)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribeWith(object : ResourceSubscriber<SkripsiResponse>() {
                        override fun onComplete() {
                            view.hideProgressBar()
                        }

                        override fun onNext(t: SkripsiResponse?) {
                            t?.let {
                                view.getSkripsiData(it) }
                            listSkripsi.postValue(t?.data)
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