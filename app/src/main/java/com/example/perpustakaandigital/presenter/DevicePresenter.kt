package com.example.perpustakaandigital.presenter

import androidx.lifecycle.ViewModel
import com.example.perpustakaandigital.database.SkripsiDao
import com.example.perpustakaandigital.database.SkripsiEntity
import com.example.perpustakaandigital.view.DeviceView
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class DevicePresenter(private val skripsiDao: SkripsiDao) : ViewModel(), DeviceView.DevicePresenter {

    private val disposables = CompositeDisposable()

    override fun insertSkripsi(skripsi: SkripsiEntity) {
        disposables.add(Observable.fromCallable {skripsiDao.insertSkripsi(skripsi)}
            .subscribeOn(Schedulers.computation())
            .subscribe())
    }

    override fun deleteSkripsi(skripsiId: String) {
        disposables.add(Observable.fromCallable {skripsiDao.deleteSkripsi(skripsiId)}
            .subscribeOn(Schedulers.computation())
            .subscribe())
    }

    override fun onDestroy() {
        disposables.dispose()
    }
}