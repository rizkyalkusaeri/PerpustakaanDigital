package com.example.perpustakaandigital.model

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class BukuResponse(

    @SerializedName("page")
    @Expose
    val page: Int? = null,

    @SerializedName("total_page")
    @Expose
    val totalPages: Int? = null,

    @SerializedName("data")
    @Expose
    val data: ArrayList<DataBuku>
): Parcelable

@Parcelize
data class DataBuku(

    @SerializedName("id")
    @Expose
    val id: String? = null,

    @SerializedName("kode_buku")
    @Expose
    val kode_buku : String? = null,

    @SerializedName("judul")
    @Expose
    val judul : String? = null,

    @SerializedName("penulis")
    @Expose
    val penulis : String? = null,

    @SerializedName("penerbit")
    val penerbit : String? = null,

    @SerializedName("edisi")
    val edisi : String? = null,

    @SerializedName("jumlah_buku")
    val jumlah_buku : String? = null
): Parcelable