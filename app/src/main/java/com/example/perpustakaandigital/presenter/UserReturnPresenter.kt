package com.example.perpustakaandigital.presenter

import com.example.perpustakaandigital.model.Response
import com.example.perpustakaandigital.repository.PerpusImp
import com.example.perpustakaandigital.view.UserReturnView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subscribers.ResourceSubscriber

class UserReturnPresenter(
    private val view : UserReturnView.View,
    private val userReturn : PerpusImp?
): UserReturnView.Presenter {

    private val disposables = CompositeDisposable()

    override fun onReturnButtonClick(apiKey: String, id_peminjaman: Int) {
        view.showProgressBarReturn()

        userReturn?.postReturn(apiKey,id_peminjaman)
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribeOn(Schedulers.io())
            ?.subscribeWith(object : ResourceSubscriber<Response>() {
                override fun onComplete() {
                    view.hideProgressBarReturn()
                }

                override fun onNext(t: Response?) {
                    if (!t?.status!!) {
                        view.onFailureReturn(t.message)
                    } else {
                        t.let { view.onSuccessReturn(it) }
                    }
                }

                override fun onError(t: Throwable?) {
                    view.hideProgressBarReturn()
                    view.handleErrorReturn(t)
                }

            })?.let { disposables.add(it) }
    }

    override fun onDestroy() {
        disposables.dispose()
    }
}