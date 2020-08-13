package com.example.perpustakaandigital.utils

import com.example.perpustakaandigital.BuildConfig
import java.util.*

class ConstantUtils {

    companion object {
        const val KEY_FRAGMENT = "fragment";

        const val BASE_URL: String = BuildConfig.BASE_URL
        const val PDF_URL: String = BuildConfig.PDF_URL
        const val API_KEY: String = BuildConfig.API_KEY
        const val MAHASISWA_EXTRA: String = "mahasiswa_data"
        const val STATE_SAVED: String = "state_saved"

        const val SAVE_JUDUL: String = "save_judul"
        const val SAVE_HALAMAN: String = "save_halaman"
        const val SAVE_PENULIS: String = "save_penulis"
        const val SAVE_FILE: String = "save_file"
        const val SAVE_KEILMUAN: String = "save_keilmuan"
        const val SAVE_PASS: String = "save_pass"

    }
}