package com.example.perpustakaandigital.presenter

import com.example.perpustakaandigital.model.LoginResponse
import com.example.perpustakaandigital.repository.MahasiswaImplementation
import com.example.perpustakaandigital.view.LoginView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subscribers.ResourceSubscriber

class LoginPresenter(
    private val view: LoginView.View,
    private val login: MahasiswaImplementation?
): LoginView.Presenter {

    private val compositeDisposable = CompositeDisposable()

    override fun onLoginButtonClick(apiKey: String, email: String, password: String) {
        view.showProgressBar()
        if(email.isNullOrEmpty() || password.isNullOrEmpty()){
            view.onFailure("Invalid email or password")
            return
        }

        login?.getDataLogin(apiKey, email, password)
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribeOn(Schedulers.io())
            ?.subscribeWith(object : ResourceSubscriber<LoginResponse>(){
                override fun onComplete() {
                    view.hideProgressBar()
                }

                override fun onNext(t: LoginResponse?) {
                    t?.let { view.onSuccess(it) }
                }

                override fun onError(t: Throwable?) {
                    view.hideProgressBar()
                    view.handleError(t)
                }

            })?.let {
                compositeDisposable.add(it)
            }
    }


    override fun onDestroy() {
        compositeDisposable.dispose()
    }

}