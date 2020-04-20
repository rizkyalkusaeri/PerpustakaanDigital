package com.example.perpustakaandigital.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @SerializedName("id")
    @Expose
    val id: String? = null,

    @SerializedName("id_anggota")
    @Expose
    val id_anggota: String? = null,

    @SerializedName("email")
    @Expose
    val email: String? = null,

    @SerializedName("password")
    @Expose
    val password: String? = null,

    @SerializedName("hak_akses")
    @Expose
    val hak_akses: String? = null
)