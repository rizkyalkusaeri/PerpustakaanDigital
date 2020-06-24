package com.example.perpustakaandigital.presenter

import com.example.perpustakaandigital.model.PinjamResponse
import com.example.perpustakaandigital.repository.MahasiswaImp
import com.example.perpustakaandigital.view.UserPinjamView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subscribers.ResourceSubscriber

class UserPinjamPresenter(
    private val view : UserPinjamView.View,
    private val pinjam : MahasiswaImp?
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
            ?.subscribeWith(object : ResourceSubscriber<PinjamResponse>() {
                override fun onComplete() {
                    view.hideProgressBar()
                }

                override fun onNext(t: PinjamResponse?) {
                    if (!t?.status!!) {
                        view.onFailure(t.message)
                    } else {
                        t.let { view.onSuccess(it) }
                        view.onPinjam(t.message)
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