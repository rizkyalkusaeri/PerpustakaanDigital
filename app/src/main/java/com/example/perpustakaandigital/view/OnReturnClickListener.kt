package com.example.perpustakaandigital.view

import com.example.perpustakaandigital.model.Peminjaman

interface OnReturnClickListener {
    fun onReturnClickListener(idPeminjaman: Int, fileName: String, directoryName: String)

    fun onOpenClickListener(fileName: String,password: String,halaman: String,directoryName: String)
}