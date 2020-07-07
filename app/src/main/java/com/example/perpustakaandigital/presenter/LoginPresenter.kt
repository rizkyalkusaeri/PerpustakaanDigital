package com.example.perpustakaandigital.presenter

import com.example.perpustakaandigital.model.LoginResponse
import com.example.perpustakaandigital.repository.PerpusImp
import com.example.perpustakaandigital.view.LoginView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subscribers.ResourceSubscriber

class LoginPresenter(
    private val view: LoginView.View,
    private val login: PerpusImp?
): LoginView.Presenter {


    private val compositeDisposable = CompositeDisposable()

    override fun onLoginButtonClick(apiKey: String, email: String, password: String) {
        view.showProgressBar()
        if(email.isEmpty() || password.isEmpty()){
            view.onFailure("Invalid email or password")
            return
        }  else {
            login?.getDataLogin(apiKey, email, password)
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribeOn(Schedulers.io())
                ?.subscribeWith(object : ResourceSubscriber<LoginResponse>() {
                    override fun onComplete() {
                        view.hideProgressBar()
                    }

                    override fun onNext(t: LoginResponse?) {
                        if (!t?.status!!){
                            view.onFailure(t.message)
                        } else{
                            t.let { view.onSuccess(it) }
                            view.onFailure(t.message)
                        }
                    }

                    override fun onError(t: Throwable?) {
                        view.hideProgressBar()
                        view.handleError(t)
                    }

                })?.let {
                    compositeDisposable.add(it)
                }
        }
    }


    override fun onDestroy() {
        compositeDisposable.dispose()
    }

}