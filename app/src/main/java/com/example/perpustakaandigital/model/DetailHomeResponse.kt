package com.example.perpustakaandigital.model

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class DetailHomeResponse(
    @SerializedName("id_skripsi")
    @Expose
    val id: Int? = null,

    @SerializedName("nim_penulis")
    @Expose
    val nim_penulis : String? = null,

    @SerializedName("penulis")
    @Expose
    val penulis : String? = null,

    @SerializedName("judul_skripsi")
    @Expose
    val judul_skripsi : String? = null,

    @SerializedName("kelompok_keilmuan")
    val kelompok_keilmuan : String? = null,

    @SerializedName("file_pdf")
    val file_pdf : String? = null,

    @SerializedName("pass_pdf")
    val pass_pdf : String? = null
) : Parcelable