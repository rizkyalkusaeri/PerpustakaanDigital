package com.example.perpustakaandigital.view

import com.example.perpustakaandigital.database.SkripsiEntity

class DeviceView {

    interface DevicePresenter{
        fun insertSkripsi(skripsi: SkripsiEntity)
        fun deleteSkripsi(skripsiId: String)
        fun onDestroy()
    }
}