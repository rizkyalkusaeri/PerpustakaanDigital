package com.example.perpustakaandigital.model

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class UsersResponse(
    @SerializedName("data")
    @Expose
    val data: ArrayList<Users>
):Parcelable

@Parcelize
data class Users(
    @SerializedName("id_anggota")
    @Expose
    val id_anggota: String? = null,

    @SerializedName("nim")
    @Expose
    val nim: String? = null,

    @SerializedName("nama")
    @Expose
    val nama: String? = null,

    @SerializedName("jk")
    @Expose
    val jk: String? = null,

    @SerializedName("alamat")
    @Expose
    val alamat: String? = null,

    @SerializedName("email")
    @Expose
    val email: String? = null,

    @SerializedName("tgl_daftar")
    @Expose
    val tgl_daftar: String? = null,

    @SerializedName("id_petugas")
    @Expose
    val id_petugas: String? = null,

    @SerializedName("akses")
    @Expose
    val akses: String? = null
):Parcelable