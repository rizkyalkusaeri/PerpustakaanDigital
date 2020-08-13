package com.example.perpustakaandigital.model

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PeminjamanResponse(
    @SerializedName("page")
    @Expose
    val page: Int? = null,

    @SerializedName("total_page")
    @Expose
    val totalPages: Int? = null,

    @SerializedName("status")
    @Expose
    val status: Boolean? = null,

    @SerializedName("message")
    @Expose
    val message: String? = null,

    @SerializedName("data")
    @Expose
    val data: ArrayList<Peminjaman>
):Parcelable

@Parcelize
data class Peminjaman(
    @SerializedName("id_peminjaman")
    @Expose
    val id_peminjaman: Int? = null,

    @SerializedName("id_anggota")
    @Expose
    val id_anggota: String? = null,

    @SerializedName("id_penulis")
    @Expose
    val id_penulis : String? = null,

    @SerializedName("penulis")
    @Expose
    val penulis : String? = null,

    @SerializedName("judul_skripsi")
    @Expose
    val judul_skripsi : String? = null,

    @SerializedName("kelompok_keilmuan")
    val kelompok_keilmuan : String? = null,

    @SerializedName("tanggal_pinjam")
    val tanggal_pinjam : String? = null,

    @SerializedName("tanggal_pengembalian")
    val tanggal_pengembalian : String? = null,

    @SerializedName("file_pdf")
    val file_pdf : String? = null,

    @SerializedName("pass_pdf")
    val pass_pdf : String? = null,

    @SerializedName("stat")
    val stat : String? = null,

    @SerializedName("halaman")
    @Expose
    val halaman : String? = null
) : Parcelable