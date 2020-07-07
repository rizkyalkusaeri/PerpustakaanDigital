package com.example.perpustakaandigital.model

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class SkripsiResponse(

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
        val data: ArrayList<Data>
): Parcelable

@Parcelize
data class Data(

        @SerializedName("id_skripsi")
        @Expose
        val id: String? = null,

        @SerializedName("nim_penulis")
        @Expose
        val nim_penulis : String? = null,

        @SerializedName("nama_penulis")
        @Expose
        val penulis : String? = null,

        @SerializedName("id_penulis")
        @Expose
        val id_penulis : String? = null,

        @SerializedName("judul_skripsi")
        @Expose
        val judul_skripsi : String? = null,

        @SerializedName("kelompok_keilmuan")
        @Expose
        val kelompok_keilmuan : String? = null,

        @SerializedName("file_pdf")
        @Expose
        val file_pdf : String? = null,

        @SerializedName("pass_pdf")
        @Expose
        val pass_pdf : String? = null,

        @SerializedName("halaman")
        @Expose
        val halaman : String? = null
):Parcelable

