package com.example.perpustakaandigital.model

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

data class PinjamResponse(
    @SerializedName("status")
    @Expose
    val status : Boolean,

    @SerializedName("message")
    @Expose
    val message : String,

    @SerializedName("data")
    @Expose
    val pinjam : Pinjam
)

@Parcelize
data class Pinjam(
    @SerializedName("id_skripsi")
    @Expose
    val id_skripsi: String? = null,

    @SerializedName("id_anggota")
    @Expose
    val id_anggota: String? = null,

    @SerializedName("tanggal_pinjam")
    @Expose
    val tanggal_pinjam: String? = null,

    @SerializedName("tanggal_pengembalian")
    @Expose
    val tanggal_pengembalian: String? = null
) : Parcelable