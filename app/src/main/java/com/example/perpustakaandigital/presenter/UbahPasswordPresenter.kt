package com.example.perpustakaandigital.presenter

import com.example.perpustakaandigital.model.UbahPasswordResponse
import com.example.perpustakaandigital.repository.MahasiswaImp
import com.example.perpustakaandigital.view.UbahPasswordView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subscribers.ResourceSubscriber

class UbahPasswordPresenter(
    private val view: UbahPasswordView.View,
    private val ubah: MahasiswaImp?
): UbahPasswordView.Presenter {

    private val compositeDisposables = CompositeDisposable()

    override fun onUbahButtonClick(
        apiKey: String,
        id_anggota: String,
        password: String
    ) {
        view.showProgressBar()
        if(password.isEmpty()){
            view.onFailure("Invalid password")
            return
        }  else {
            ubah?.getDataUbahPassword(apiKey, id_anggota, password)
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribeOn(Schedulers.io())
                ?.subscribeWith(object : ResourceSubscriber<UbahPasswordResponse>() {
                    override fun onComplete() {
                        view.hideProgressBar()
                    }

                    override fun onNext(t: UbahPasswordResponse?) {
                        if (!t?.status!!) {
                            view.onFailure(t.message)
                        } else {
                            t.let { view.onSuccess(it) }
                        }
                    }

                    override fun onError(t: Throwable?) {
                        view.hideProgressBar()
                        view.handleError(t)
                    }

                })?.let { compositeDisposables.add(it) }
        }
    }

    override fun onDestroy() {
        compositeDisposables.dispose()
    }
}