package com.example.perpustakaandigital.presenter

import com.example.perpustakaandigital.model.Response
import com.example.perpustakaandigital.repository.PerpusImp
import com.example.perpustakaandigital.view.UserPinjamView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subscribers.ResourceSubscriber

class UserPinjamPresenter(
    private val view : UserPinjamView.View,
    private val pinjam : PerpusImp?
): UserPinjamView.Presenter {

    private val disposables = CompositeDisposable()

    override fun onPinjamButtonClick(
        apiKey: String,
        id_skripsi: String,
        id_anggota: String,
        tanggal_pinjam: String,
        tanggal_pengembalian: String
    ) {
        view.showProgressBar()

        pinjam?.postPinjam(apiKey,id_skripsi,id_anggota,tanggal_pinjam,tanggal_pengembalian)
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribeOn(Schedulers.io())
            ?.subscribeWith(object : ResourceSubscriber<Response>() {
                override fun onComplete() {
                    view.hideProgressBar()
                }

                override fun onNext(t: Response?) {
                    if (!t?.status!!) {
                        view.onFailure(t.message)
                    } else {
                        t.let { view.onSuccessPinjam(it) }
                    }
                }

                override fun onError(t: Throwable?) {
                    view.hideProgressBar()
                    view.handleError(t)
                }

            })?.let { disposables.add(it) }
    }

    override fun onDestroy() {
        disposables.dispose()
    }
}