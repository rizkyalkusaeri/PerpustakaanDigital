package com.example.perpustakaandigital.model

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

data class LoginResponse(

    @SerializedName("status")
    @Expose
    val status: Boolean,

    @SerializedName("message")
    @Expose
    val message: String,

    @SerializedName("data")
    @Expose
    val users: Login
)

@Parcelize
data class Login(

    @SerializedName("id_anggota")
    @Expose
    val id_anggota: String? = null,

    @SerializedName("email")
    @Expose
    val email: String? = null,

    @SerializedName("nama")
    @Expose
    val nama: String? = null,

    @SerializedName("password")
    @Expose
    val password: String? = null,

    @SerializedName("hak_akses")
    @Expose
    val hak_akses: String? = null
) : Parcelable